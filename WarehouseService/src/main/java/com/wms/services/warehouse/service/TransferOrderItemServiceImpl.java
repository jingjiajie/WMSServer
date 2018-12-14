package com.wms.services.warehouse.service;

import com.wms.services.ledger.service.PersonService;
import com.wms.services.warehouse.dao.TransferOrderItemDAO;
import com.wms.services.warehouse.datastructures.ItemType;
import com.wms.services.warehouse.datastructures.TransferStock;
import com.wms.utilities.IDChecker;
import com.wms.utilities.ReflectHelper;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import com.wms.utilities.vaildator.Validator;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.stream.Stream;

@Service
@Transactional(isolation = Isolation.READ_UNCOMMITTED)
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
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    ItemRelatedRecordService itemRelatedRecordService;
    private static boolean autotransfer=false;

    public void autoTrans(boolean a){
        autotransfer=a;
    }


    @Override
    public int[] add(String accountBook, TransferOrderItem[] transferOrderItems) throws WMSServiceException {
        //根据每条移库单条目，更新移库单条目的移货数量
        //获取移库单
        int transferOrderId = transferOrderItems[0].getTransferOrderId();
        TransferOrderView[] foundTransferOrders = this.transferOrderService.find(accountBook,new Condition().addCondition("id",new Integer[]{transferOrderId}));
        if(foundTransferOrders.length == 0){
            throw new WMSServiceException(String.format("移库单不存在，请重新提交！(%d)",transferOrderId));
        }

        this.validateEntities(accountBook, transferOrderItems);
        int[] ids=new int[transferOrderItems.length];
        for (int i=0;i<ids.length;i++){
            int id=this.transferOrderItemDAO.add(accountBook, new TransferOrderItem[]{transferOrderItems[i]})[0];

            TransferStock tureTransferStock = new TransferStock();

            tureTransferStock.setAvailableAmount(transferOrderItems[i].getScheduledAmount());
            tureTransferStock.setAmount(transferOrderItems[i].getRealAmount());

            tureTransferStock.setSourceStorageLocationId(transferOrderItems[i].getSourceStorageLocationId());
            tureTransferStock.setNewStorageLocationId(transferOrderItems[i].getTargetStorageLocationId());

            tureTransferStock.setRelatedOrderNo(foundTransferOrders[0].getNo());
            tureTransferStock.setSupplyId(transferOrderItems[i].getSupplyId());
            tureTransferStock.setNewUnit(transferOrderItems[i].getUnit());
            tureTransferStock.setNewUnitAmount(transferOrderItems[i].getUnitAmount());
            tureTransferStock.setUnit(transferOrderItems[i].getSourceUnit());
            tureTransferStock.setUnitAmount(transferOrderItems[i].getSourceUnitAmount());

            this.stockRecordService.transferStock(accountBook, tureTransferStock,new TransferStock());//直接改数

            if (transferOrderItems[i].getScheduledAmount().compareTo(transferOrderItems[i].getRealAmount())==0){
                transferOrderItems[i].setState(TransferOrderItemService.STATE_ALL_FINISH);
            }
            ids[i]=id;
        }
        this.updateTransferOrder(accountBook, transferOrderItems[0].getTransferOrderId(), transferOrderItems[0].getPersonId());
        return ids;
    }

    @Override
    public void update(String accountBook, TransferOrderItem[] transferOrderItems) throws WMSServiceException {
        this.validateEntities(accountBook, transferOrderItems);
        Stream.of(transferOrderItems).forEach((transferOrderItem -> {


            //找出对应的移库单和移库单条目
            TransferOrderItemView[] oriItemViews = this.transferOrderItemDAO.find(accountBook, new Condition().addCondition("id", new Integer[]{transferOrderItem.getId()}));
            if (oriItemViews.length == 0) {
                throw new WMSServiceException(String.format("移库单条目不存在，修改失败(%d)", transferOrderItem.getId()));
            }

            int transferOrderId = transferOrderItem.getTransferOrderId();
            TransferOrderView[] foundTransferOrders = this.transferOrderService.find(accountBook,new Condition().addCondition("id",new Integer[]{transferOrderId}));
            if(foundTransferOrders.length == 0) {
                throw new WMSServiceException(String.format("移库单不存在，请重新提交！(%d)", transferOrderId));
            }
            TransferOrderView transferOrderView = foundTransferOrders[0];
            if (transferOrderItem.getScheduledAmount().subtract(transferOrderItem.getRealAmount()).compareTo(new BigDecimal(0)) < 0)//如果新修改时计划数量小于当前实际已经移动的数量
            {
                throw new WMSServiceException(String.format("移库单条目计划数量不能小于实际数量！单号：(%s)", transferOrderView.getNo()));
            }



            if (itemRelatedRecordService.findTable(accountBook,new Condition()
                    .addCondition("relatedItemId",transferOrderItem.getId())
                    .addCondition("itemType", ItemType.transferItem)).length > 0) {

                //实际移库操作
                TransferStock transferStock = new TransferStock();
                transferStock.setNewStorageLocationId(transferOrderItem.getTargetStorageLocationId());
                transferStock.setSourceStorageLocationId(transferOrderItem.getSourceStorageLocationId());
                transferStock.setAmount(transferOrderItem.getRealAmount());//实际数量
                transferStock.setAvailableAmount(transferOrderItem.getScheduledAmount());

                transferStock.setRelatedOrderNo(transferOrderView.getNo());//获取单号
                transferStock.setItemId(transferOrderItem.getId());
                transferStock.setItemType(ItemType.transferItem);

                transferStock.setSupplyId(transferOrderItem.getSupplyId());
                transferStock.setUnit(transferOrderItem.getSourceUnit());
                transferStock.setUnitAmount(transferOrderItem.getSourceUnitAmount());
                transferStock.setNewUnit(transferOrderItem.getUnit());
                transferStock.setNewUnitAmount(transferOrderItem.getUnitAmount());

                //旧的信息
                TransferStock transferStockRestore = new TransferStock();
                transferStockRestore.setNewStorageLocationId(oriItemViews[0].getTargetStorageLocationId());
                transferStockRestore.setSourceStorageLocationId(oriItemViews[0].getSourceStorageLocationId());

                transferStockRestore.setRelatedOrderNo(transferOrderView.getNo());//获取单号
                transferStockRestore.setItemId(transferOrderItem.getId());
                transferStockRestore.setItemType(ItemType.transferItem);

                transferStockRestore.setSupplyId(oriItemViews[0].getSupplyId());
                transferStockRestore.setUnit(oriItemViews[0].getUnit());
                transferStockRestore.setUnitAmount(oriItemViews[0].getUnitAmount());
                transferStockRestore.setNewUnit(oriItemViews[0].getSourceUnit());
                transferStockRestore.setNewUnitAmount(oriItemViews[0].getSourceUnitAmount());


                this.stockRecordService.transferStock(accountBook, transferStock,transferStockRestore);//使用更新单位的库存修改


            }else{
                //实际移库操作
                TransferStock transferStock = new TransferStock();
                transferStock.setNewStorageLocationId(transferOrderItem.getTargetStorageLocationId());
                transferStock.setSourceStorageLocationId(transferOrderItem.getSourceStorageLocationId());
                transferStock.setAmount(transferOrderItem.getRealAmount());//实际数量
                transferStock.setAvailableAmount(transferOrderItem.getScheduledAmount());

                transferStock.setRelatedOrderNo(transferOrderView.getNo());//获取单号
                transferStock.setItemId(transferOrderItem.getId());
                transferStock.setItemType(ItemType.transferItem);

                transferStock.setSupplyId(transferOrderItem.getSupplyId());
                transferStock.setUnit(transferOrderItem.getSourceUnit());
                transferStock.setUnitAmount(transferOrderItem.getSourceUnitAmount());
                transferStock.setNewUnit(transferOrderItem.getUnit());
                transferStock.setNewUnitAmount(transferOrderItem.getUnitAmount());

                this.stockRecordService.transferStock(accountBook, transferStock,new TransferStock());//使用更新单位的库存修改
            }

            //TODO 最后状态变更

            if (transferOrderItem.getScheduledAmount().compareTo(transferOrderItem.getRealAmount())==0){
                transferOrderItem.setState(TransferOrderItemService.STATE_ALL_FINISH);
            }
            else if (transferOrderItem.getRealAmount().compareTo(new BigDecimal(0))==0){
                transferOrderItem.setState(TransferOrderItemService.STATE_IN_TRANSFER);
            }else
            {transferOrderItem.setState(TransferOrderItemService.STATE_PARTIAL_FINNISH);}
        }));


        this.transferOrderItemDAO.update(accountBook, transferOrderItems);
        this.updateTransferOrder(accountBook,transferOrderItems[0].getTransferOrderId() ,transferOrderItems[0].getPersonId());
    }


    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        int curTransferOrderId=-1;
        int curPersonId=-1;
        for (int id : ids) {
            //todo idChecker.check(this.getClass(), accountBook, id, "删除的移库单条目");

            TransferOrderItemView[] oriItemViews = this.transferOrderItemDAO.find(accountBook, new Condition().addCondition("id", new Integer[]{id}));
            if (oriItemViews.length == 0) {
                throw new WMSServiceException(String.format("移库单条目不存在，删除失败(%d)", id));
            }
            TransferOrderItemView oriItemView=oriItemViews[0];
            curTransferOrderId=oriItemView.getTransferOrderId();
            curPersonId=oriItemView.getPersonId();
            if (oriItemView.getState()==0
//                    oriItemView.getScheduledAmount().compareTo(new BigDecimal(0))!=0&&
//                    oriItemView.getRealAmount().compareTo(new BigDecimal(0))==0
                    )
            {
                //删除了未经过操作的移库单，更新库存可用数量
                TransferStock transferStock = new TransferStock();
                transferStock.setModifyAvailableAmount(oriItemView.getScheduledAmount());//计划数量
                transferStock.setSourceStorageLocationId(oriItemView.getSourceStorageLocationId());//修改源库位可用数量
                transferStock.setSupplyId(oriItemView.getSupplyId());
                transferStock.setUnit(oriItemView.getSourceUnit());
                transferStock.setUnitAmount(oriItemView.getSourceUnitAmount());
                this.stockRecordService.modifyAvailableAmount(accountBook, transferStock);
            }
            else {
                //TODO 把之前的货物在库存里移动回去
                TransferStock reTransferStock = new TransferStock();
                reTransferStock.setNewStorageLocationId(oriItemView.getSourceStorageLocationId());
                reTransferStock.setAmount(oriItemView.getRealAmount());//本来已经移动了的数量
                reTransferStock.setSourceStorageLocationId(oriItemView.getTargetStorageLocationId());//目标库位反转
                reTransferStock.setSupplyId(oriItemView.getSupplyId());
                reTransferStock.setRelatedOrderNo(oriItemView.getTransferOrderNo());//获取单号
                reTransferStock.setInventoryDate(new Timestamp(System.currentTimeMillis()));
                reTransferStock.setUnit(oriItemView.getUnit());
                reTransferStock.setUnitAmount(oriItemView.getUnitAmount());
                reTransferStock.setNewUnit(oriItemView.getSourceUnit());
                reTransferStock.setNewUnitAmount(oriItemView.getSourceUnitAmount());
                this.deliveryOrderItemService.updateSleep();
                this.stockRecordService.RealTransferStockUnitFlexible(accountBook,reTransferStock);//使用更新单位的库存修改

                //todo 返回的可用数量变化跟上
                TransferStock fixTransferStock = new TransferStock();
                fixTransferStock.setModifyAvailableAmount(oriItemView.getScheduledAmount().subtract(oriItemView.getRealAmount()));//实际要移动的数量差值加回到可用数量
                fixTransferStock.setSourceStorageLocationId(oriItemView.getSourceStorageLocationId());//修改源库位
                fixTransferStock.setSupplyId(oriItemView.getSupplyId());
                fixTransferStock.setUnit(oriItemView.getSourceUnit());
                fixTransferStock.setUnitAmount(oriItemView.getSourceUnitAmount());
                this.deliveryOrderItemService.updateSleep();
                this.stockRecordService.modifyAvailableAmount(accountBook, fixTransferStock);

            }

        }
        this.transferOrderItemDAO.remove(accountBook, ids);
        this.updateTransferOrder(accountBook,curTransferOrderId ,curPersonId);
    }

    @Override
    public TransferOrderItemView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.transferOrderItemDAO.find(accountBook, cond);
    }

    private void validateEntities(String accountBook, TransferOrderItem[] transferOrderItems) {
        Stream.of(transferOrderItems).forEach((transferOrderItem -> {
            //数据验证
            new Validator("状态").min(0).max(2).validate(transferOrderItem.getState());
            new Validator("计划移位数量").greaterThan(0).validate(transferOrderItem.getScheduledAmount());
            new Validator("单位").notnull().validate(transferOrderItem.getUnit());
            new Validator("单位数量").greaterThan(0).validate(transferOrderItem.getUnitAmount());
            new Validator("源单位").notnull().validate(transferOrderItem.getSourceUnit());
            new Validator("源单位数量").greaterThan(0).validate(transferOrderItem.getSourceUnitAmount());
            if (transferOrderItem.getRealAmount() != null) {
                new Validator("实际移位数量").notEmpty().min(0).max(transferOrderItem.getScheduledAmount()).validate(transferOrderItem.getRealAmount());
            }

            //验证外键
            this.idChecker.check(TransferOrderService.class, accountBook, transferOrderItem.getTransferOrderId(), "关联移库单单");
            this.idChecker.check(StorageLocationService.class, accountBook, transferOrderItem.getTargetStorageLocationId(), "目标库位");
            this.idChecker.check(StorageLocationService.class, accountBook, transferOrderItem.getSourceStorageLocationId(), "源库位");
            this.idChecker.check(SupplyService.class, accountBook, transferOrderItem.getSupplyId(), "关联供货信息");
            this.idChecker.check(PersonService.class, accountBook, transferOrderItem.getPersonId(), "作业人员");

        }));
    }


    @Override
    public long findCount(String accountBook, Condition cond) throws WMSServiceException{
        return this.transferOrderItemDAO.findCount(accountBook,cond);
    }

    public void updateTransferOrder( String accountBook,int transferOrderId ,int lastUpdatePersonId){
        //TODO idChecker.check(PersonService.class,accountBook,lastUpdatePersonId," 人员");
        TransferOrderView[] transferOrderViews= transferOrderService.find(accountBook,new Condition().addCondition("id",new Integer[]{transferOrderId}));
        TransferOrder[] transferOrders = ReflectHelper.createAndCopyFields(transferOrderViews,TransferOrder.class);
        if(transferOrderViews.length==0){
            throw new WMSServiceException("没有找到要更新的移库单！");
        }
        TransferOrder transferOrder=transferOrders[0];
        if (lastUpdatePersonId!=-1) {
            transferOrder.setLastUpdatePersonId(lastUpdatePersonId);
        }

        transferOrder.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));

        TransferOrderItem[] allItems = this.transferOrderItemDAO.findTable(accountBook,new Condition().addCondition("transferOrderId",transferOrder.getId(), ConditionItem.Relation.IN));

        TransferOrderItem[] allFinishItems = this.transferOrderItemDAO.findTable(accountBook,new Condition()
                .addCondition("transferOrderId",transferOrder.getId(), ConditionItem.Relation.IN)
                .addCondition("state",TransferOrderItemService.STATE_ALL_FINISH, ConditionItem.Relation.IN));

        TransferOrderItem[] zeroItems = this.transferOrderItemDAO.findTable(accountBook,new Condition()
                .addCondition("transferOrderId",transferOrder.getId(), ConditionItem.Relation.IN).
                        addCondition("state",TransferOrderItemService.STATE_IN_TRANSFER, ConditionItem.Relation.IN));

        if (allItems.length>0&&allFinishItems.length==allItems.length){
                transferOrder.setState(TransferOrderItemService.STATE_ALL_FINISH);
        }else if (allItems.length==0||zeroItems.length==allItems.length){
            transferOrder.setState(TransferOrderItemService.STATE_IN_TRANSFER);
        }
        else{
            transferOrder.setState(TransferOrderItemService.STATE_PARTIAL_FINNISH);
        }
        transferOrderService.update(accountBook,new TransferOrder[]{transferOrder});
    }

}
