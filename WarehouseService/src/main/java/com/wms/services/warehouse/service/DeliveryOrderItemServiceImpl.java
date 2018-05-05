package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.DeliveryOrderItemDAO;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.DeliveryOrderItemView;
import com.wms.utilities.model.DeliveryOrderItem;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Service
@Transactional
public class DeliveryOrderItemServiceImpl implements DeliveryOrderItemService{
    @Autowired
    DeliveryOrderItemDAO deliveryOrderItemDAO;
    @Autowired
    DeliveryOrderService deliveryOrderService;
    @Autowired
    SupplyService supplyService;
    @Autowired
    StorageLocationService storageLocationService;
    @Autowired
    PersonService personService;

    @Override
    public int[] add(String accountBook, DeliveryOrderItem[] deliveryOrderItems) throws WMSServiceException {
        this.validateEntities(accountBook,deliveryOrderItems);
        return this.deliveryOrderItemDAO.add(accountBook,deliveryOrderItems);
    }

    @Override
    public void update(String accountBook, DeliveryOrderItem[] deliveryOrderItems) throws WMSServiceException {
        this.validateEntities(accountBook,deliveryOrderItems);
        this.deliveryOrderItemDAO.update(accountBook,deliveryOrderItems);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        try {
            this.deliveryOrderItemDAO.remove(accountBook, ids);
        } catch (Throwable ex) {
            throw new WMSServiceException("删除失败，如果条目已经被引用，需要先删除引用项目");
        }
    }

    @Override
    public DeliveryOrderItemView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.deliveryOrderItemDAO.find(accountBook, cond);
    }

    private void validateEntities(String accountBook,DeliveryOrderItem[] deliveryOrderItems){
        //数据验证
        Stream.of(deliveryOrderItems).forEach(
                (deliveryOrderItem)->{
                    new Validator("计划装车数量（个）").min(0).validate(deliveryOrderItem.getScheduledAmount());
                    new Validator("实际装车数量（个）").min(0).max(deliveryOrderItem.getScheduledAmount().intValue()).validate(deliveryOrderItem.getRealAmount());
                    new Validator("单位数量").min(0).validate(deliveryOrderItem.getUnitAmount());
                    new Validator("状态").min(0).max(5).validate(deliveryOrderItem.getState());
                }
        );

        //外键验证
        Stream.of(deliveryOrderItems).forEach(
                (deliveryOrderItem) -> {
                    if (this.deliveryOrderService.find(accountBook,
                            new Condition().addCondition("id", deliveryOrderItem.getDeliveryOrderId())).length == 0) {
                        throw new WMSServiceException(String.format("入库单不存在，请重新提交！(%d)", deliveryOrderItem.getDeliveryOrderId()));
                    } else if (supplyService.find(accountBook,
                            new Condition().addCondition("id", deliveryOrderItem.getSupplyId())).length == 0) {
                        throw new WMSServiceException(String.format("供货信息不存在，请重新提交！(%d)", deliveryOrderItem.getSupplyId()));
                    } else if (storageLocationService.find(accountBook,
                            new Condition().addCondition("id", deliveryOrderItem.getSourceStorageLocationId())).length == 0) {
                        throw new WMSServiceException(String.format("库位不存在，请重新提交！(%d)", deliveryOrderItem.getSourceStorageLocationId()));
                    } else if (deliveryOrderItem.getPersonId() != null && personService.find(accountBook,
                            new Condition().addCondition("id", deliveryOrderItem.getPersonId())).length == 0) {
                        throw new WMSServiceException(String.format("作业人员不存在，请重新提交！(%d)", deliveryOrderItem.getPersonId()));
                    }
                }
        );
    }
}
