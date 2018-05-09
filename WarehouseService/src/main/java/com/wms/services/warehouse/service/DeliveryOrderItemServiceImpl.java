package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.DeliveryOrderItemDAO;
import com.wms.services.warehouse.datastructures.TransferStock;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.DeliveryOrderItemView;
import com.wms.utilities.model.DeliveryOrderItem;
import com.wms.utilities.model.DeliveryOrderView;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    @Autowired
    StockRecordService stockRecordService;

    @Override
    public int[] add(String accountBook, DeliveryOrderItem[] deliveryOrderItems) throws WMSServiceException {
        if (deliveryOrderItems.length == 0) return new int[]{};
        DeliveryOrderView deliveryOrderView = this.getDeliveryOrderView(accountBook,deliveryOrderItems);
        //验证字段
        this.validateEntities(accountBook,deliveryOrderItems);
        //修改库存
        Stream.of(deliveryOrderItems).forEach(deliveryOrderItem -> {
            TransferStock transferStock=new TransferStock();
            BigDecimal amount =new BigDecimal(0);
            BigDecimal realAmount=amount.subtract(deliveryOrderItem.getScheduledAmount());//计划出库数量or实际出库数量
            transferStock.setAmount(realAmount);
            transferStock.setSourceStorageLocationId(deliveryOrderItem.getSourceStorageLocationId());
            transferStock.setRelatedOrderNo(deliveryOrderView.getNo());
            transferStock.setSupplyId(deliveryOrderItem.getSupplyId());
            transferStock.setUnit(deliveryOrderItem.getUnit());
            transferStock.setUnitAmount(deliveryOrderItem.getUnitAmount());
            this.stockRecordService.modifyAvailableAmount(accountBook, transferStock);
        });
        //添加到数据库中
        int[] ids = this.deliveryOrderItemDAO.add(accountBook, deliveryOrderItems);
        return ids;//仅返回iDs
        //return this.deliveryOrderItemDAO.add(accountBook,deliveryOrderItems);
    }

    @Override
    public void update(String accountBook, DeliveryOrderItem[] deliveryOrderItems) throws WMSServiceException {
        DeliveryOrderView deliveryOrderView = this.getDeliveryOrderView(accountBook,deliveryOrderItems);
        //数据验证
        this.validateEntities(accountBook,deliveryOrderItems);

        Stream.of(deliveryOrderItems).forEach(deliveryOrderItem -> {
            DeliveryOrderItemView[] foundOriItems = this.deliveryOrderItemDAO.find(accountBook,new Condition().addCondition("id",deliveryOrderItem.getId()));
            if(foundOriItems.length == 0) throw new WMSServiceException(String.format("出库单条目不存在，请重新提交！",deliveryOrderItem.getId()));//排除异常
            DeliveryOrderItemView oriItemView = foundOriItems[0];

            BigDecimal deltaRealAmount = deliveryOrderItem.getRealAmount().subtract(oriItemView.getRealAmount());
            //修改实收数量，更新库存
            if(!deltaRealAmount.equals(new BigDecimal(0))){
                TransferStock transferStock=new TransferStock();
                BigDecimal amount =new BigDecimal(0);
                BigDecimal realAmount=amount.subtract(deliveryOrderItem.getRealAmount());
                transferStock.setAmount(realAmount);//TODO 待定
                transferStock.setSourceStorageLocationId(deliveryOrderItem.getSourceStorageLocationId());
                transferStock.setRelatedOrderNo(deliveryOrderView.getNo());
                transferStock.setSupplyId(deliveryOrderItem.getSupplyId());
                transferStock.setUnit(deliveryOrderItem.getUnit());
                transferStock.setUnitAmount(deliveryOrderItem.getUnitAmount());
                this.stockRecordService.RealTransformStock(accountBook, transferStock);
            }
        });
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
                        throw new WMSServiceException(String.format("出库单不存在，请重新提交！(%d)", deliveryOrderItem.getDeliveryOrderId()));
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

    private DeliveryOrderView getDeliveryOrderView(String accountBook,DeliveryOrderItem[] deliveryOrderItems){
        //验证各出库单条目，出库单号必须全部相同
        Stream.of(deliveryOrderItems).reduce((last, cur) -> {
            if (last.getDeliveryOrderId() != cur.getDeliveryOrderId())
                throw new WMSServiceException("出库单条目所属的出库单必须相同！");
            return cur;
        });
        //获取出库单
        int deliveryOrderId = deliveryOrderItems[0].getDeliveryOrderId();
        final DeliveryOrderView[] deliveryOrderViews = this.deliveryOrderService.find(accountBook, new Condition().addCondition("id", deliveryOrderId));
        if (deliveryOrderViews.length == 0)
            throw new WMSServiceException(String.format("出库单(%d)不存在，请重新提交！", deliveryOrderId));
        DeliveryOrderView deliveryOrderView = deliveryOrderViews[0];
        return deliveryOrderView;
    }
}
