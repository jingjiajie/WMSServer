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

    private static final String PREFIX = "T";
    private static final String PREFIX1 = "P";

    @Override
    public int[] add(String accountBook, TransferOrder[] objs) throws WMSServiceException {
        Stream.of(objs).forEach(obj->{
            if(obj.getNo() == null || obj.getNo().isEmpty()) {
                if(obj.getType()==TransferOrderService.TYPE_PACKAGE){
                    obj.setNo(this.orderNoGenerator.generateNextNo(accountBook, PREFIX));
                }else if(obj.getType()==TransferOrderService.TYPE_PUT){
                    obj.setNo(this.orderNoGenerator.generateNextNo(accountBook, PREFIX1));
                }
            }
            obj.setState(TransferOrderItemService.STATE_IN_TRANSFER);//新建移库单记为状态0
            obj.setPrintTimes(new BigDecimal(0));
            obj.setCreateTime(new Timestamp(System.currentTimeMillis()));
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
                transferOrderItem.setRealAmount(transferOrderItem.getScheduledAmount());
            });
            //整单完成所有状态变更为移库完成 2
            //更新移库单状态,应该是只有一个单子


            Stream.of(transferOrderItems).forEach(transferOrderItem -> transferOrderItem.setState(TransferOrderItemService.STATE_ALL_FINISH));
            if(transferOrderItems.length>0){
            this.transferOrderItemService.update(accountBook, transferOrderItems);}
//            //更新移库单状态
//            Stream.of(transferOrders).forEach(transferOrder -> {
//                if (transferOrder.getState() ==TransferOrderItemService.STATE_ALL_FINISH) {
//                    throw new WMSServiceException(String.format("当前移库单（%s）已经完成移库，无法重复作业", transferOrder.getNo()));
//                }
//                if (transferOrder.getState() ==TransferOrderItemService.STATE_IN_TRANSFER) {
//                    transferOrder.setState(TransferOrderItemService.STATE_ALL_FINISH);
//                }
//                if (transferOrder.getState() ==TransferOrderItemService.STATE_PARTIAL_FINNISH) {
//                    transferOrder.setState(TransferOrderItemService.STATE_ALL_FINISH);
//                }
//                transferOrder.setLastUpdatePersonId(transferFinishArgs.getPersonId());
//
//            });
//            this.update(accountBook,transferOrders);
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


//            更新移库单状态,应该是只有一个单子
//            Stream.of(transferOrders).forEach(transferOrder -> {
//                boolean judgeState =true;
//                TransferOrderItem[] allItems = this.transferOrderItemDAO.findTable(accountBook,new Condition().addCondition("transferOrderId",transferOrder.getId(), ConditionItem.Relation.IN));
//                for (int i=0;i<allItems.length;i++){
//                    if (allItems[i].getState()==TransferOrderItemService.STATE_ALL_FINISH){
//
//                    }
//                    else {
//                        judgeState=false;
//                    }
//                }
//                if (judgeState){
//                    transferOrder.setState(TransferOrderItemService.STATE_ALL_FINISH);
//                }else{
//                    transferOrder.setState(TransferOrderItemService.STATE_PARTIAL_FINNISH);
//                }
//                transferOrder.setLastUpdatePersonId(transferFinishArgs.getPersonId());
//
//            });
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
            if (transferOrderItemViews[i].getState() !=TransferOrderItemService.STATE_ALL_FINISH) {
                TransferFinishItem transferFinishItem = new TransferFinishItem();
                transferFinishItem.setTransferOrderItemId(transferOrderItemViews[i].getId());
                transferFinishItem.setPersonId(transferOrderItemViews[i].getPersonId());
                transferFinishItemsList.add(transferFinishItem);
            }
        }
        TransferFinishItem[] transferFinishItems=null;
        transferFinishItems = (TransferFinishItem[]) Array.newInstance(TransferFinishItem.class,transferFinishItemsList.size());
        transferFinishItemsList.toArray(transferFinishItems);

        if (transferFinishItems.length==0) {
                throw new WMSServiceException("入库单条目均已经完成移库，请勿重复操作！");
        }


        transferFinishArgs.setTransferFinishItems(transferFinishItems);

        this.transferFinish(accountBook, transferFinishArgs);
    }

    private void validateEntities(String accountBook,TransferOrder[] transferOrders) throws WMSServiceException{
        Stream.of(transferOrders).forEach((transferOrder -> {
            new Validator("状态").min(0).max(2).validate(transferOrder.getState());
            new Validator("创建时间").notnull().validate(transferOrder.getCreateTime());
            new Validator("打印次数").min(0).validate(transferOrder.getPrintTimes());

            idChecker.check(WarehouseService.class,accountBook,transferOrder.getWarehouseId(),"仓库")
                    .check(PersonService.class,accountBook,transferOrder.getCreatePersonId(),"创建人员")
                    .check(SupplierServices.class,accountBook,transferOrder.getSupplierId(),"创建人员");

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
}
