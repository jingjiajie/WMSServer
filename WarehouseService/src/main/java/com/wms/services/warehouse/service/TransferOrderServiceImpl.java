package com.wms.services.warehouse.service;

import com.wms.services.ledger.service.PersonService;
import com.wms.services.warehouse.dao.TransferOrderDAO;
import com.wms.services.warehouse.dao.TransferOrderItemDAO;
import com.wms.services.warehouse.datastructures.*;
import com.wms.utilities.IDChecker;
import com.wms.utilities.OrderNoGenerator;
import com.wms.utilities.ReflectHelper;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wms.utilities.model.TransferOrder;
import com.wms.utilities.model.TransferOrderView;


import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class TransferOrderServiceImpl implements TransferOrderService{
    @Autowired
    TransferOrderDAO transferOrderDAO;
    @Autowired
    TransferOrderItemService transferOrderItemService;
    @Autowired
    IDChecker idChecker;
    @Autowired
    OrderNoGenerator orderNoGenerator;
    @Autowired
    PersonService personService;
    @Autowired
    TransferOrderItemDAO transferOrderItemDAO;
    @Autowired
    DeliveryOrderService deliveryOrderService;
    @Autowired
    DeliveryOrderItemService deliveryOrderItemService;
    @Autowired
    StockRecordService stockRecordService;

    private static final String PREFIX = "T";
    private static final String PREFIX1 = "P";

    @Override
    public int[] add(String accountBook, TransferOrder[] objs) throws WMSServiceException {
        Stream.of(objs).forEach(obj->{
            if(obj.getNo() == null || obj.getNo().isEmpty()) {
                if(obj.getType()==TransferOrderService.TYPE_PACKAGE){
                    obj.setNo(this.orderNoGenerator.generateNextNo(accountBook, PREFIX,obj.getWarehouseId()));
                }else if(obj.getType()==TransferOrderService.TYPE_PUT){
                    obj.setNo(this.orderNoGenerator.generateNextNo(accountBook, PREFIX1,obj.getWarehouseId()));
                }
            }
            obj.setState(TransferOrderItemService.STATE_IN_TRANSFER);//新建移库单记为状态0
            obj.setPrintTimes(new BigDecimal(0));
            obj.setCreateTime(new Timestamp(System.currentTimeMillis()));
            obj.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
            obj.setLastUpdatePersonId(obj.getCreatePersonId());
        });
        this.validateEntities(accountBook,objs);
        int[] ids= transferOrderDAO.add(accountBook,objs);
        return ids;
    }

    @Override
    public void update(String accountBook, TransferOrder[] objs) throws WMSServiceException {
        Stream.of(objs).forEach((obj)->{
            new Validator("最后更新人员").notnull().validate(obj.getLastUpdatePersonId());
            obj.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
        });
        this.validateEntities(accountBook,objs);
        this.transferOrderDAO.update(accountBook,objs);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        for(int id : ids){
            if(this.transferOrderDAO.find(accountBook,new Condition().addCondition("id",id)).length == 0){
                throw new WMSServiceException(String.format("移库单不存在，请重新查询(%d)",id));
            }
            TransferOrderView[] oriViews = this.transferOrderDAO.find(accountBook, new Condition().addCondition("id",new Integer[]{ id}));
            if (oriViews.length == 0) {
                throw new WMSServiceException(String.format("移库单不存在，删除失败(%d)", id));
            }
            TransferOrderView oriView=oriViews[0];
            if (oriView.getState()!=0)
            {
                throw new WMSServiceException(String.format("移库单正在作业，无法删除(%s)", oriView.getId()));
            }
        }

        try {
            this.transferOrderDAO.remove(accountBook,ids);
        } catch (Throwable ex) {
            throw new WMSServiceException("删除出库单失败，如果出库单已经被引用，需要先删除引用该出库单的内容，才能删除该出库单");
        }
    }

    @Override
    public TransferOrderView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.transferOrderDAO.find(accountBook,cond);
    }

    @Override
    public void transferFinish(String accountBook, TransferFinishArgs transferFinishArgs) throws WMSServiceException{
        TransferOrderView[] transferOrderViews = this.find(accountBook,new Condition().addCondition("id",new Integer[]{transferFinishArgs.getTransferOrderId()}));
        TransferOrder[] transferOrders = ReflectHelper.createAndCopyFields(transferOrderViews,TransferOrder.class);

        if(transferFinishArgs.isAllFinish()){ //整单完成
            TransferOrderItemView[] transferOrderItemViews = this.transferOrderItemService.find(accountBook,new Condition().addCondition("transferOrderId",new Integer[]{transferFinishArgs.getTransferOrderId()}).addCondition("state",TransferOrderItemService.STATE_ALL_FINISH, ConditionItem.Relation.NOT_EQUAL));
            TransferOrderItem[] transferOrderItems = ReflectHelper.createAndCopyFields(transferOrderItemViews,TransferOrderItem.class);


            //如果设置了人员，将每个条目的人员设置为相应人员。否则遵循各个条目原设置
            if(transferFinishArgs.getPersonId() != -1){
                idChecker.check(PersonService.class,accountBook,transferFinishArgs.getPersonId(),"作业人员");//外检检测
                Stream.of(transferOrderItems).forEach(transferOrderItem -> transferOrderItem.setPersonId(transferFinishArgs.getPersonId()));
            }
            //将每一条的实际移动数量同步成计划移动数量
            Stream.of(transferOrderItems).forEach(transferOrderItem -> {
                if (transferOrderItem.getState() ==TransferOrderItemService.STATE_IN_TRANSFER){
                    transferOrderItem.setRealAmount(transferOrderItem.getScheduledAmount());
                }
                transferOrderItem.setState(TransferOrderItemService.STATE_ALL_FINISH);
            });


            if(transferOrderItems.length>0){
            this.transferOrderItemService.update(accountBook, transferOrderItems);
            }
        }
        else { //部分完成
            TransferFinishItem[] transferFinishItems = transferFinishArgs.getTransferFinishItems();
            Stream.of(transferFinishItems).forEach(transferFinishItem -> {
                //检查有没有移库单条目
                //this.idChecker.check(TransferOrderItemService.class, accountBook, transferFinishArgs.getTransferOrderId(), "移库单条目");

                TransferOrderItemView transferOrderItemView = this.transferOrderItemService.find(accountBook, new Condition().addCondition("id",new Integer[]{ transferFinishItem.getTransferOrderItemId()}))[0];
                TransferOrderItem transferOrderItem = ReflectHelper.createAndCopyFields(transferOrderItemView, TransferOrderItem.class);

                //把请求移动的数量加上已经移动的实际数量
                transferOrderItem.setRealAmount(transferOrderItem.getScheduledAmount());

                if(transferFinishItem.getPersonId() != -1){
                    transferOrderItem.setPersonId(transferFinishItem.getPersonId());
                }
                //部分完成移库操作
                transferOrderItem.setState(TransferOrderItemService.STATE_ALL_FINISH);

                this.transferOrderItemService.update(accountBook, new TransferOrderItem[]{transferOrderItem});
            });

            this.update(accountBook, transferOrders);
        }
    }

    public void transferSome(String accountBook, List<Integer> ids,int personId) {
        if (ids.size() == 0) {
            throw new WMSServiceException("请选择至少一个移库单条目！");
        }
        TransferOrderItemView[] transferOrderItemViews = this.transferOrderItemService.find(accountBook, new Condition().addCondition("id", ids.toArray(), ConditionItem.Relation.IN));
        if (transferOrderItemViews.length == 0) return;

        //todo 更新移库单状态
        //this.update(accountBook, transferOrder);
        TransferFinishArgs transferFinishArgs=new TransferFinishArgs();
        transferFinishArgs.setPersonId(personId);
        transferFinishArgs.setAllFinish(false);
        transferFinishArgs.setTransferOrderId(transferOrderItemViews[0].getTransferOrderId());

        List<TransferFinishItem> transferFinishItemsList=new ArrayList();
        for(int i=0;i<transferOrderItemViews.length;i++){
            if (transferOrderItemViews[i].getState() ==TransferOrderItemService.STATE_IN_TRANSFER) {
                TransferFinishItem transferFinishItem = new TransferFinishItem();
                transferFinishItem.setTransferOrderItemId(transferOrderItemViews[i].getId());
                transferFinishItem.setPersonId(transferOrderItemViews[i].getPersonId());
                transferFinishItemsList.add(transferFinishItem);
            }
        }
        TransferFinishItem[] transferFinishItems=null;
        transferFinishItems = (TransferFinishItem[]) Array.newInstance(TransferFinishItem.class,transferFinishItemsList.size());
        transferFinishItemsList.toArray(transferFinishItems);

        if (transferFinishItems.length==0) return;

        transferFinishArgs.setTransferFinishItems(transferFinishItems);

        this.transferFinish(accountBook, transferFinishArgs);
    }

    private void validateEntities(String accountBook,TransferOrder[] transferOrders) throws WMSServiceException{
        Stream.of(transferOrders).forEach((transferOrder -> {
            new Validator("状态").min(0).max(2).validate(transferOrder.getState());
            new Validator("创建时间").notnull().validate(transferOrder.getCreateTime());
            new Validator("打印次数").min(0).validate(transferOrder.getPrintTimes());

            idChecker.check(WarehouseService.class,accountBook,transferOrder.getWarehouseId(),"仓库")
                    .check(PersonService.class,accountBook,transferOrder.getCreatePersonId(),"创建人员");

            if (transferOrder.getLastUpdatePersonId() != null && this.personService.find(accountBook,
                    new Condition().addCondition("id", transferOrder.getLastUpdatePersonId())).length == 0) {
                throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)", transferOrder.getLastUpdatePersonId()));
            }
        }));

    }

    public void transfer(String accountBook, TransferArgs transferArgs) {
        TransferItem[] transferItems = transferArgs.getTransferItems();
        Stream.of(transferItems).forEach((transferItem) -> {
            //创建新的移库单
            TransferOrder transferOrder = transferItem.getTransferOrder();
            new Validator("移库单信息").notnull().validate(transferOrder);

            int newTransferOrderID = this.add(accountBook, new TransferOrder[]{transferOrder})[0];

            //按照安全库存信息，生成移库单条目
            TransferOrderItem[] transferOrderItems=transferItem.getTransferOrderItems();
            Stream.of(transferOrderItems).forEach((transferOrderItem)->{
                //创建新的移库单条目
                //找到对应供货对应库位的安全库存信息
                    transferOrderItem.setRealAmount(new BigDecimal(0));
                    transferOrderItem.setComment("成功创建移库");
                    transferOrderItem.setOperateTime(new Timestamp(System.currentTimeMillis()));
                    transferOrderItem.setState(0);
                    transferOrderItem.setTransferOrderId(newTransferOrderID);
            });
            this.transferOrderItemService.add(accountBook, transferOrderItems);
            //TODO 尝试更新移库单时间
            //transferOrderItemService.updateTransferOrder(accountBook,newTransferOrderID ,transferArgs.getTransferItems()[0].getTransferOrder().getCreatePersonId());
        });
    }

    @Override
    public long findCount(String accountBook, Condition cond) throws WMSServiceException{
        return this.transferOrderDAO.findCount(accountBook,cond);
    }

    @Override
    public List<TransferOrderAndItems> getPreviewData(String accountBook, List<Integer> transferOrderIDs) throws WMSServiceException{
        TransferOrderView[] transferOrderViews = this.transferOrderDAO.find(accountBook,new Condition().addCondition("id",transferOrderIDs.toArray(), ConditionItem.Relation.IN));
        TransferOrderItemView[] itemViews = this.transferOrderItemService.find(accountBook,new Condition().addCondition("transferOrderId",transferOrderIDs.toArray(), ConditionItem.Relation.IN));
        List<TransferOrderAndItems> result = new ArrayList<>();
        for(TransferOrderView transferOrderView : transferOrderViews){
            TransferOrderAndItems transferOrderAndItems = new TransferOrderAndItems();
            transferOrderAndItems.setTransferOrder(transferOrderView);
            transferOrderAndItems.setTransferOrderItems(new ArrayList<>());
            result.add(transferOrderAndItems);
            for(TransferOrderItemView itemView : itemViews){
                if(itemView.getTransferOrderId() == transferOrderView.getId()){
                    transferOrderAndItems.getTransferOrderItems().add(itemView);
                }
            }
        }
        return result;
    }

    @Override
    public List<DeliveryOrderItemView> orderToDelivery(String accountBook, DeliveryByTransferOrder deliveryByTransferOrder) {

        TransferOrderView[] transferOrderViews=this.find(accountBook,new Condition().addCondition("id",deliveryByTransferOrder.getTransferOrderId()));
        if (transferOrderViews[0].getState()!=2){
            throw new WMSServiceException(String.format("当前备货单未整单完成，备货单号（%S），无法创建出库单", transferOrderViews[0].getNo()));
        }

        TransferOrderItemView[] itemViews = this.transferOrderItemService.find(accountBook,new Condition().addCondition("transferOrderId",deliveryByTransferOrder.getTransferOrderId(), ConditionItem.Relation.IN));
        if (itemViews.length == 0) {
            throw new WMSServiceException(String.format("当前备货单未包括条目信息，备货单名称（%S），无法创建出库单", itemViews[0].getTransferOrderNo()));
        }

        //新建出库单
        DeliveryOrder deliveryOrder = new DeliveryOrder();
        deliveryOrder.setCreatePersonId(deliveryByTransferOrder.getPersonId());
        deliveryOrder.setWarehouseId(deliveryByTransferOrder.getWarehouseId());
        deliveryOrder.setState(DeliveryOrderService.STATE_IN_LOADING);
        deliveryOrder.setDescription("备货单直接发货");
        int curDeliveryOrderId = this.deliveryOrderService.add(accountBook, new DeliveryOrder[]{deliveryOrder})[0];

        List<DeliveryOrderItem> deliveryOrderItemList=new ArrayList();
        List<DeliveryOrderItemView> falseDeliveryOrderItemList=new ArrayList();
        for(int i=0;i<itemViews.length;i++){
            StockRecordViewNewest[] stockRecordViews = stockRecordService.findNewest(accountBook,
                    new Condition().addCondition("storageLocationId", new Integer[]{itemViews[i].getTargetStorageLocationId()})
                            .addCondition("supplyId", new Integer[]{itemViews[i].getSupplyId()})
                            .addCondition("unitAmount", new BigDecimal[]{itemViews[i].getUnitAmount()})
                            .addCondition("unit", new String[]{itemViews[i].getUnit()}));

            BigDecimal sourceAmount= new BigDecimal(0);
            for(int j=0;j<stockRecordViews.length;j++) {
                sourceAmount=sourceAmount.add(stockRecordViews[j].getAvailableAmount());
            }

            if (stockRecordViews.length!=0&&sourceAmount.compareTo(itemViews[i].getRealAmount()) >=0) {
                DeliveryOrderItem deliveryOrderItem = new DeliveryOrderItem();
                deliveryOrderItem.setSourceStorageLocationId(itemViews[i].getTargetStorageLocationId());
                deliveryOrderItem.setUnit(itemViews[i].getUnit());
                deliveryOrderItem.setUnitAmount(itemViews[i].getUnitAmount());
                deliveryOrderItem.setSupplyId(itemViews[i].getSupplyId());
                deliveryOrderItem.setScheduledAmount(itemViews[i].getRealAmount());
                deliveryOrderItem.setPersonId(deliveryByTransferOrder.getPersonId());
                deliveryOrderItem.setDeliveryOrderId(curDeliveryOrderId);
                deliveryOrderItem.setRealAmount(BigDecimal.ZERO);
                deliveryOrderItem.setComment("发货");
                deliveryOrderItemList.add(deliveryOrderItem);

            }
            else{
                DeliveryOrderItemView falseDeliveryOrderItemView = new DeliveryOrderItemView();
                falseDeliveryOrderItemView.setSourceStorageLocationId(itemViews[i].getTargetStorageLocationId());
                falseDeliveryOrderItemView.setSourceStorageLocationName(itemViews[i].getTargetStorageLocationName());
                falseDeliveryOrderItemView.setUnit(itemViews[i].getUnit());
                falseDeliveryOrderItemView.setUnitAmount(itemViews[i].getUnitAmount());
                falseDeliveryOrderItemView.setSupplyId(itemViews[i].getSupplyId());

                falseDeliveryOrderItemView.setSupplierName(itemViews[i].getSupplierName());
                falseDeliveryOrderItemView.setSupplierNo(itemViews[i].getSupplierNo());
                falseDeliveryOrderItemView.setMaterialName(itemViews[i].getMaterialName());
                falseDeliveryOrderItemView.setMaterialNo(itemViews[i].getMaterialNo());
                falseDeliveryOrderItemView.setMaterialProductLine(itemViews[i].getMaterialProductLine());

                falseDeliveryOrderItemView.setScheduledAmount(itemViews[i].getRealAmount());
                falseDeliveryOrderItemView.setRealAmount(sourceAmount);
                falseDeliveryOrderItemView.setComment("失败发货项");
                falseDeliveryOrderItemList.add(falseDeliveryOrderItemView);
            }

        }
        DeliveryOrderItem[] deliveryOrderItems=null;
        deliveryOrderItems = (DeliveryOrderItem[]) Array.newInstance(DeliveryOrderItem.class,deliveryOrderItemList.size());
        deliveryOrderItemList.toArray(deliveryOrderItems);
        if (deliveryOrderItems.length==0) {
            throw new WMSServiceException(String.format("当前备货单无可直接正常发货项，备货单号（%S），无法创建出库单，请检查后再试！", itemViews[0].getTransferOrderNo()));
        }
        this.deliveryOrderItemService.add(accountBook,deliveryOrderItems);
        return falseDeliveryOrderItemList;
    }
}
