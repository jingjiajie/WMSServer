package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.DeliveryOrderDAO;
import com.wms.services.warehouse.datastructures.TransferAuto;
import com.wms.utilities.IDChecker;
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

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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
    IDChecker idChecker;
    @Autowired
    TransferOrderService transferOrderService;
    @Autowired
    TransferOrderItemService transferOrderItemService;
    @Autowired
    SafetyStockService safetyStockService;
    @Autowired
    StockRecordService stockRecordService;
    @Autowired
    SupplyService supplyService;


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

            int newTransferOrderID = this.transferOrderService.add(accountBook, new TransferOrder[]{transferOrder})[0];

            //按照安全库存信息，生成移库单条目
            TransferOrderItem[] transferOrderItems=transferItem.getTransferOrderItems();
            Stream.of(transferOrderItems).forEach((transferOrderItem)->{
                //创建新的移库单条目
                // todo 等数据库修改确定
                //找到对应供货对应库位的安全库存信息
                if(transferArgs.isAutoCommit()&&transferOrderItem.getScheduledAmount().equals(new BigDecimal(0))) {
                    SafetyStockView[] safetyStockViews = safetyStockService.find(accountBook,
                            new Condition().addCondition("targetStorageLocationId", new Integer[]{transferOrderItem.getTargetStorageLocationId()}).addCondition("supplyId", new Integer[]{transferOrderItem.getSupplyId()}));
                    StockRecordView[] stockRecordViews = stockRecordService.find(accountBook,
                            new Condition().addCondition("storageLocationId", new Integer[]{transferOrderItem.getTargetStorageLocationId()}).addCondition("supplyId", new Integer[]{transferOrderItem.getSupplyId()}));
                    StockRecordView[] sourseStockRecordViews = stockRecordService.find(accountBook,
                            new Condition().addCondition("storageLocationId", new Integer[]{transferOrderItem.getSourceStorageLocationId()}).addCondition("supplyId", new Integer[]{transferOrderItem.getSupplyId()}));
                    if (stockRecordViews[0].getAmount().compareTo(safetyStockViews[0].getAmount()) == -1)//如果库存数量小于安全库存数量
                    {
                        transferOrderItem.setScheduledAmount(safetyStockViews[0].getAmount().subtract(stockRecordViews[0].getAmount()));//设置计划数量为与安全库存的差值
                        transferOrderItem.setTransferOrderId(newTransferOrderID);
                        transferOrderItem.setState(0);
                        this.transferOrderItemService.add(accountBook, new TransferOrderItem[]{transferOrderItem});
                    } else {
                        //库存充足取消备货
                        throw new WMSServiceException(String.format("当前备货区(%s)库存充足，不需要备货", safetyStockViews[0].getTargetStorageLocationName()));
                    }
                }else{
                    transferOrderItem.setState(0);
                    transferOrderItem.setTransferOrderId(newTransferOrderID);
                    this.transferOrderItemService.add(accountBook, new TransferOrderItem[]{transferOrderItem});
                }
            });
        });
    }

    public void transferAuto(String accountBook, TransferAuto TransferAuto) {
        new Validator("人员").notnull().validate(TransferAuto.getPersonId());
        idChecker.check(com.wms.services.warehouse.service.WarehouseService.class, accountBook, TransferAuto.getWarehouseId(), " 仓库");
        SafetyStockView[] safetyStockViews=safetyStockService.find(accountBook,new Condition().addCondition("warehouseId",new Integer[]{TransferAuto.getWarehouseId()}));
        if(safetyStockViews.length==0){throw new WMSServiceException("当前仓库无任何安全库存记录，无法自动添加移库作业单单条目！");}
        TransferArgs transferArgs=new TransferArgs();
        TransferItem transferItem=new TransferItem();

        TransferOrder transferOrder=new TransferOrder();
        transferOrder.setWarehouseId(TransferAuto.getWarehouseId());
        transferOrder.setCreatePersonId(TransferAuto.getPersonId());

        //新建列表存放条目
        List<TransferOrderItem> transferOrderItemsList=new ArrayList();
        for(int i=0;i<safetyStockViews.length;i++){
            TransferOrderItem transferOrderItem=new TransferOrderItem();
            transferOrderItem.setTargetStorageLocationId(safetyStockViews[i].getTargetStorageLocationId());
            transferOrderItem.setSourceStorageLocationId(safetyStockViews[i].getSourceStorageLocationId());
            transferOrderItem.setUnit(safetyStockViews[i].getUnit());
            transferOrderItem.setUnitAmount(safetyStockViews[i].getUnitAmount());
            transferOrderItem.setSupplyId(safetyStockViews[i].getSupplyId());
            transferOrderItem.setScheduledAmount(new BigDecimal(0));
            transferOrderItemsList.add(transferOrderItem);
        }
        TransferOrderItem[] transferOrderItems=null;
        transferOrderItems = (TransferOrderItem[]) Array.newInstance(TransferOrderItem.class,transferOrderItemsList.size());
        transferOrderItemsList.toArray(transferOrderItems);

        transferItem.setTransferOrder(transferOrder);
        transferItem.setTransferOrderItems(transferOrderItems);

        transferArgs.setTransferItems(new TransferItem[]{transferItem});
        transferArgs.setAutoCommit(true);
        this.transferPakage(accountBook, transferArgs);

    }


    @Override
    public long findCount(String accountBook, Condition cond) throws WMSServiceException{
        return this.deliveryOrderDAO.findCount(accountBook,cond);
    }
}
