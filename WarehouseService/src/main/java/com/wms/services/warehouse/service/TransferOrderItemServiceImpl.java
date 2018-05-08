package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.TransferOrderItemDAO;
import com.wms.services.warehouse.datastructures.TransferStock;
import com.wms.utilities.IDChecker;
import com.wms.utilities.ReflectHelper;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wms.utilities.model.TransferOrderItem;
import com.wms.utilities.model.TransferOrderItemView;

import java.math.BigDecimal;
import java.util.stream.Stream;

@Service
@Transactional
public class TransferOrderItemServiceImpl implements TransferOrderItemService{
    @Autowired
    TransferOrderItemDAO transferOrderItemDAO;
    @Autowired
    DeliveryOrderItemService deliveryOrderItemService;
    @Autowired
    TransferOrderService transferOrderService;
    @Autowired
    StockRecordService stockRecordService;
    @Autowired
    IDChecker idChecker;

    @Override
    public int[] add(String accountBook, TransferOrderItem[] transferOrderItems) throws WMSServiceException {
        //根据每条移库单条目，更新移库单条目的移货数量
        Stream.of(transferOrderItems).forEach(transferOrderItem -> {


            //获取移库单
            int transferOrderId = transferOrderItem.getTransferOrderId();
            TransferOrderView[] foundTransferOrders = this.transferOrderService.find(accountBook,new Condition().addCondition("id",transferOrderId));
            if(foundTransferOrders.length == 0){
                throw new WMSServiceException(String.format("移库单不存在，请重新提交！(%d)",transferOrderId));
            }
            TransferOrderView transferOrderView = foundTransferOrders[0];

            //更新库存
            TransferStock transferStock = new TransferStock();
            transferStock.setAmount(transferOrderItem.getRealAmount());
            transferStock.setSourceStorageLocationId(transferOrderItem.getSourceStorageLocationId());
            transferStock.setNewStorageLocationId(transferOrderItem.getTargetStorageLocationId());
            transferStock.setRelatedOrderNo(transferOrderView.getNo());
            transferStock.setSupplyId(transferOrderItem.getSupplyId());
            transferStock.setUnit(transferOrderItem.getUnit());
            transferStock.setUnitAmount(transferOrderItem.getUnitAmount());
            this.stockRecordService.transformStock(accountBook, transferStock);
        });

        this.validateEntities(accountBook, transferOrderItems);
        return this.transferOrderItemDAO.add(accountBook, transferOrderItems);
    }

    @Override
    public void update(String accountBook, TransferOrderItem[] transferOrderItems) throws WMSServiceException {
        this.validateEntities(accountBook, transferOrderItems);
        Stream.of(transferOrderItems).forEach((transferOrderItem -> {
            //如果送检数量变化，反映到库存上
            //TODO 库存API还没做
            TransferOrderItemView[] oriItemViews = this.transferOrderItemDAO.find(accountBook, new Condition().addCondition("id", transferOrderItem.getId()));
            if (oriItemViews.length == 0) {
                throw new WMSServiceException(String.format("移库单条目不存在，修改失败(%d)", transferOrderItem.getId()));
            }
            //如果返回数量变化，反映到库存中
            //TODO 库存API还没做


        }));
        this.transferOrderItemDAO.update(accountBook, transferOrderItems);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        for (int id : ids) {
            idChecker.check(this.getClass(), accountBook, id, "删除的送检单条目");
        }
        this.transferOrderItemDAO.remove(accountBook, ids);
    }

    @Override
    public TransferOrderItemView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.transferOrderItemDAO.find(accountBook, cond);
    }

    private void validateEntities(String accountBook, TransferOrderItem[] transferOrderItems) {
        Stream.of(transferOrderItems).forEach((transferOrderItem -> {
            //数据验证
            new Validator("状态").min(0).max(2).validate(transferOrderItem.getState());
            new Validator("计划移位数量").min(0).validate(transferOrderItem.getScheduledAmount());
            new Validator("单位").notnull().validate(transferOrderItem.getUnit());
            new Validator("单位数量").min(0).validate(transferOrderItem.getUnitAmount());
            if (transferOrderItem.getRealAmount() != null) {
                new Validator("实际移位数量").min(0).max(transferOrderItem.getScheduledAmount()).validate(transferOrderItem.getRealAmount());
            }

            //验证外键
            this.idChecker.check(TransferOrderService.class, accountBook, transferOrderItem.getTransferOrderId(), "关联送检单");
            this.idChecker.check(StorageLocationService.class, accountBook, transferOrderItem.getTargetStorageLocationId(), "目标库位");
            this.idChecker.check(SupplyService.class, accountBook, transferOrderItem.getSupplyId(), "关联供货信息");


            if (transferOrderItem.getPersonId() != null) {
                this.idChecker.check(PersonService.class, accountBook, transferOrderItem.getPersonId(), "作业人员");
            }
        }));
    }
}
