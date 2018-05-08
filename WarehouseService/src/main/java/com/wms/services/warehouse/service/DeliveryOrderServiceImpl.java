package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.DeliveryOrderDAO;
import com.wms.utilities.OrderNoGenerator;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.DeliveryOrderView;
import com.wms.utilities.model.DeliveryOrder;
import com.wms.utilities.vaildator.Validator;
import com.wms.utilities.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wms.services.warehouse.datastructures.TransferArgs;
import com.wms.services.warehouse.datastructures.TransferItem;

import java.sql.Timestamp;
import java.util.stream.Stream;

@Service
@Transactional
public class DeliveryOrderServiceImpl implements DeliveryOrderService{
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    DeliveryOrderDAO deliveryOrderDAO;
    @Autowired
    OrderNoGenerator orderNoGenerator;
    @Autowired
    PersonService personService;
    @Autowired
    TransferOrderService transferOrderService;
    @Autowired
    TransferOrderItemService transferOrderItemService;

    private static final String NO_PREFIX = "D";

    @Override
    public int[] add(String accountBook, DeliveryOrder[] deliveryOrders) throws WMSServiceException {
        //验证结构
        this.validateEntities(accountBook,deliveryOrders);
        //生成创建时间
        Stream.of(deliveryOrders).forEach((deliveryOrder) -> deliveryOrder.setCreateTime(new java.sql.Timestamp(System.currentTimeMillis())));

        //生成/检测单号
        Stream.of(deliveryOrders).forEach((deliveryOrder) -> {
            //如果单号留空则自动生成
            if (deliveryOrder.getNo() == null) {
                deliveryOrder.setNo(this.orderNoGenerator.generateNextNo(accountBook, DeliveryOrderServiceImpl.NO_PREFIX));
            } else { //否则检查单号是否重复
                Condition cond = new Condition();
                cond.addCondition("no", new String[]{deliveryOrder.getNo()});
                if (deliveryOrderDAO.find(accountBook, cond).length > 0) {
                    throw new WMSServiceException("出库单单号重复：" + deliveryOrder.getNo());
                }
            }
        });
        return deliveryOrderDAO.add(accountBook, deliveryOrders);
    }

    @Override
    public void update(String accountBook, DeliveryOrder[] deliveryOrders) throws WMSServiceException {
        //数据验证
        Stream.of(deliveryOrders).forEach((deliveryOrder) -> {
            new Validator("出库单单号").notEmpty().validate(deliveryOrder.getNo());
        });

        //名称查重
        for (int i = 0; i < deliveryOrders.length; i++) {
            Condition cond = new Condition();
            cond.addCondition("no", new String[]{deliveryOrders[i].getNo()});
            cond.addCondition("id", new Integer[]{deliveryOrders[i].getId()}, ConditionItem.Relation.NOT_EQUAL);
            if (deliveryOrderDAO.find(accountBook, cond).length > 0) {
                throw new WMSServiceException("出库单单号重复：" + deliveryOrders[i].getNo());
            }
        }
        Stream.of(deliveryOrders).forEach((deliveryOrder -> {
            deliveryOrder.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
        }));


        deliveryOrderDAO.update(accountBook, deliveryOrders);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        for (int id : ids) {
            if (deliveryOrderDAO.find(accountBook, new Condition().addCondition("id", id)).length == 0) {
                throw new WMSServiceException(String.format("删除出库单不存在，请重新查询！(%d)", id));
            }
        }

        try {
            deliveryOrderDAO.remove(accountBook, ids);
        } catch (Throwable ex) {
            throw new WMSServiceException("删除出库单失败，如果出库单已经被引用，需要先删除引用该出库单的内容，才能删除该出库单");
        }
    }

    @Override
    public DeliveryOrderView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return deliveryOrderDAO.find(accountBook, cond);
    }

    private void validateEntities(String accountBook, DeliveryOrder[] deliveryOrders) {
        //数据验证
        Stream.of(deliveryOrders).forEach(
                (deliveryOrder) -> {
                    new Validator("状态").min(0).max(5).validate(deliveryOrder.getState());
                }
        );

        //外键检测
        Stream.of(deliveryOrders).forEach(
                (deliveryOrder) -> {
                    if (this.warehouseService.find(accountBook,
                            new Condition().addCondition("id", deliveryOrder.getWarehouseId())).length == 0) {
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)", deliveryOrder.getWarehouseId()));
                    }else if (this.personService.find(accountBook,
                            new Condition().addCondition("id", deliveryOrder.getCreatePersonId())).length == 0) {
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)", deliveryOrder.getCreatePersonId()));
                    }if (deliveryOrder.getLastUpdatePersonId() != null && this.personService.find(accountBook,
                            new Condition().addCondition("id", deliveryOrder.getLastUpdatePersonId())).length == 0) {
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)", deliveryOrder.getLastUpdatePersonId()));
                    }
                }
        );
    }

    //翻包备货作业，生成移库单
    public void transferPakage(String accountBook, TransferArgs transferArgs) {
        TransferItem[] transferItems = transferArgs.getTransferItems();
        Stream.of(transferItems).forEach((transferItem) -> {
            //创建新的移库单
            TransferOrder transferOrder = transferItem.getTransferOrder();
            new Validator("移库单信息").notnull().validate(transferOrder);

            int newInspectionNoteID = this.transferOrderService.add(accountBook, new TransferOrder[]{transferOrder})[0];

            //按照安全库存信息，生成移库单条目
            Stream.of(transferItem.getTransferOrderItems()).forEach((transferOrderItem)->{
                //创建新的移库单条目
                // todo （会按照安全库存自动更新移库数量）
                transferOrderItem.setTransferOrderId(newInspectionNoteID);
                this.transferOrderItemService.add(accountBook,new TransferOrderItem[]{transferOrderItem});
            });
        });
    }
}
