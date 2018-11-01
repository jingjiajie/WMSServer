package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.TransferOrderItemDAO;
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
        Stream.of(transferOrderItems).forEach(transferOrderItem -> {
            //更新库存


                //先改变可用数量，仅是想让提示看起来正常点
                TransferStock transferStock = new TransferStock();
                transferStock.setModifyAvailableAmount(new BigDecimal(0).subtract(transferOrderItem.getScheduledAmount()));//计划数量
                transferStock.setSourceStorageLocationId(transferOrderItem.getSourceStorageLocationId());//修改源库位可用数量
                transferStock.setSupplyId(transferOrderItem.getSupplyId());
                transferStock.setUnit(transferOrderItem.getSourceUnit());
                transferStock.setUnitAmount(transferOrderItem.getSourceUnitAmount());
                this.stockRecordService.modifyAvailableAmount(accountBook, transferStock);
                transferOrderItem.setState(TransferOrderItemService.STATE_IN_TRANSFER);

            if (transferOrderItem.getRealAmount().compareTo(new BigDecimal(0))!=0){

                //todo 仅是想让提示看起来正常点
                TransferStock fakeTransferStock= new TransferStock();
                fakeTransferStock.setModifyAvailableAmount(transferOrderItem.getScheduledAmount());//计划数量
                fakeTransferStock.setSourceStorageLocationId(transferOrderItem.getSourceStorageLocationId());//修改源库位可用数量
                fakeTransferStock.setSupplyId(transferOrderItem.getSupplyId());
                fakeTransferStock.setUnit(transferOrderItem.getSourceUnit());
                fakeTransferStock.setUnitAmount(transferOrderItem.getSourceUnitAmount());
                this.stockRecordService.modifyAvailableAmount(accountBook, fakeTransferStock);
                transferOrderItem.setState(TransferOrderItemService.STATE_IN_TRANSFER);

                //先把有数量变化的移动，这里默认都是变单位移动
                TransferStock tureTransferStock = new TransferStock();
                tureTransferStock.setAmount(transferOrderItem.getRealAmount());
                tureTransferStock.setSourceStorageLocationId(transferOrderItem.getSourceStorageLocationId());
                tureTransferStock.setNewStorageLocationId(transferOrderItem.getTargetStorageLocationId());
                tureTransferStock.setRelatedOrderNo(foundTransferOrders[0].getNo());
                tureTransferStock.setSupplyId(transferOrderItem.getSupplyId());
                tureTransferStock.setNewUnit(transferOrderItem.getUnit());
                tureTransferStock.setNewUnitAmount(transferOrderItem.getUnitAmount());
                tureTransferStock.setUnit(transferOrderItem.getSourceUnit());
                tureTransferStock.setUnitAmount(transferOrderItem.getSourceUnitAmount());
                tureTransferStock.setInventoryDate(new Timestamp(System.currentTimeMillis()));
                this.stockRecordService.RealTransferStockUnitFlexible(accountBook, tureTransferStock);//直接改数

                //再改可用数量
                TransferStock rdTransferStock = new TransferStock();
                rdTransferStock.setModifyAvailableAmount(transferOrderItem.getRealAmount().subtract(transferOrderItem.getScheduledAmount()));
                rdTransferStock.setSourceStorageLocationId(transferOrderItem.getSourceStorageLocationId());
                rdTransferStock.setSupplyId(transferOrderItem.getSupplyId());
                rdTransferStock.setUnit(transferOrderItem.getSourceUnit());
                rdTransferStock.setUnitAmount(transferOrderItem.getSourceUnitAmount());
                this.stockRecordService.modifyAvailableAmount(accountBook, rdTransferStock);//直接改可用数量
                transferOrderItem.setState(DeliveryOrderService.STATE_PARTIAL_LOADING);
                //如果计划数量和实际数量一样，状态变成完成
                if (transferOrderItem.getScheduledAmount().compareTo(transferOrderItem.getRealAmount())==0){
                    transferOrderItem.setState(TransferOrderItemService.STATE_ALL_FINISH);
                }
            }
//            this.updateTransferOrder(accountBook, transferOrderItem.getTransferOrderId(), transferOrderItem.getPersonId());

        });

        int[] ids =this.transferOrderItemDAO.add(accountBook, transferOrderItems);
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

            //TODO 如果传进来update的源库位目标库位什么都都都不一样
            if(transferOrderItem.getSourceStorageLocationId()!=oriItemViews[0].getSourceStorageLocationId()
                    ||transferOrderItem.getTargetStorageLocationId()!=oriItemViews[0].getTargetStorageLocationId()
                    ||transferOrderItem.getSourceUnitAmount().compareTo(oriItemViews[0].getSourceUnitAmount())!=0
                    ||!transferOrderItem.getSourceUnit().equals(oriItemViews[0].getSourceUnit()))
            {

                //TODO 把之前的货物在库存里移动回去
                TransferStock thereTransferStock = new TransferStock();
                thereTransferStock.setNewStorageLocationId(oriItemViews[0].getSourceStorageLocationId());
                thereTransferStock.setAmount(oriItemViews[0].getRealAmount());//本来已经移动了的数量
                thereTransferStock.setSourceStorageLocationId(oriItemViews[0].getTargetStorageLocationId());//目标库位反转
                thereTransferStock.setSupplyId(oriItemViews[0].getSupplyId());
                thereTransferStock.setRelatedOrderNo(oriItemViews[0].getTransferOrderNo());//获取单号
                thereTransferStock.setInventoryDate(new Timestamp(System.currentTimeMillis()));
                thereTransferStock.setUnit(oriItemViews[0].getUnit());
                thereTransferStock.setUnitAmount(oriItemViews[0].getUnitAmount());
                thereTransferStock.setNewUnit(oriItemViews[0].getSourceUnit());
                thereTransferStock.setNewUnitAmount(oriItemViews[0].getSourceUnitAmount());
                this.stockRecordService.RealTransferStockUnitFlexible(accountBook,thereTransferStock);//使用更新单位的库存修改

                //todo 返回的可用数量变化跟上
                TransferStock thefixTransferStock = new TransferStock();
                thefixTransferStock.setModifyAvailableAmount(oriItemViews[0].getScheduledAmount().subtract(oriItemViews[0].getRealAmount()));//实际差值加回到可用数量
                thefixTransferStock.setSourceStorageLocationId(oriItemViews[0].getSourceStorageLocationId());//修改源库位
                thefixTransferStock.setSupplyId(oriItemViews[0].getSupplyId());
                thefixTransferStock.setUnit(oriItemViews[0].getSourceUnit());
                thefixTransferStock.setUnitAmount(oriItemViews[0].getSourceUnitAmount());
                this.stockRecordService.modifyAvailableAmount(accountBook, thefixTransferStock);

                //todo 再一次更新库存
                    //没有实际数输入就只改变可用数量,都是针对源单位数量的
                    TransferStock seTransferStock = new TransferStock();
                    seTransferStock.setModifyAvailableAmount(new BigDecimal(0).subtract(transferOrderItem.getScheduledAmount()));//计划数量
                    seTransferStock.setSourceStorageLocationId(transferOrderItem.getSourceStorageLocationId());//修改源库位可用数量
                    seTransferStock.setUnit(transferOrderItem.getSourceUnit());
                    seTransferStock.setUnitAmount(transferOrderItem.getSourceUnitAmount());
                    seTransferStock.setSupplyId(transferOrderItem.getSupplyId());
                    this.stockRecordService.modifyAvailableAmount(accountBook, seTransferStock);
                    transferOrderItem.setState(TransferOrderItemService.STATE_IN_TRANSFER);

                if (transferOrderItem.getRealAmount().compareTo(new BigDecimal(0))!=0) {
                    //todo 为了提示
                    TransferStock fakeTransferStock = new TransferStock();
                    fakeTransferStock.setModifyAvailableAmount(new BigDecimal(0).subtract(transferOrderItem.getScheduledAmount()));//计划数量
                    fakeTransferStock.setSourceStorageLocationId(transferOrderItem.getSourceStorageLocationId());//修改源库位可用数量
                    fakeTransferStock.setUnit(transferOrderItem.getSourceUnit());
                    fakeTransferStock.setUnitAmount(transferOrderItem.getSourceUnitAmount());
                    fakeTransferStock.setSupplyId(transferOrderItem.getSupplyId());
                    this.stockRecordService.modifyAvailableAmount(accountBook, fakeTransferStock);
                    transferOrderItem.setState(TransferOrderItemService.STATE_IN_TRANSFER);

                    //先把有数量变化的移动，这里默认都是变单位移动
                    TransferStock transferStock = new TransferStock();
                    transferStock.setAmount(transferOrderItem.getRealAmount());
                    transferStock.setSourceStorageLocationId(transferOrderItem.getSourceStorageLocationId());
                    transferStock.setNewStorageLocationId(transferOrderItem.getTargetStorageLocationId());
                    transferStock.setRelatedOrderNo(foundTransferOrders[0].getNo());
                    transferStock.setSupplyId(transferOrderItem.getSupplyId());
                    transferStock.setNewUnit(transferOrderItem.getUnit());
                    transferStock.setUnit(transferOrderItem.getSourceUnit());
                    transferStock.setNewUnitAmount(transferOrderItem.getUnitAmount());
                    transferStock.setUnitAmount(transferOrderItem.getSourceUnitAmount());
                    transferStock.setInventoryDate(new Timestamp(System.currentTimeMillis()));
                    this.stockRecordService.RealTransferStockUnitFlexible(accountBook, transferStock);//直接改数

                    //再改可用数量
                    TransferStock rdTransferStock = new TransferStock();
                    rdTransferStock.setModifyAvailableAmount(transferOrderItem.getRealAmount().subtract(transferOrderItem.getScheduledAmount()));
                    rdTransferStock.setSourceStorageLocationId(transferOrderItem.getSourceStorageLocationId());
                    rdTransferStock.setSupplyId(transferOrderItem.getSupplyId());
                    rdTransferStock.setUnit(transferOrderItem.getSourceUnit());
                    rdTransferStock.setUnitAmount(transferOrderItem.getSourceUnitAmount());
                    this.stockRecordService.modifyAvailableAmount(accountBook, rdTransferStock);//直接改可用数量
                    transferOrderItem.setState(DeliveryOrderService.STATE_PARTIAL_LOADING);

                    if (transferOrderItem.getScheduledAmount().compareTo(transferOrderItem.getRealAmount()) == 0) {
                        transferOrderItem.setState(TransferOrderItemService.STATE_ALL_FINISH);
                    }
                }

            }
            else {


                //如果没有实际移库数量输入，直接跳过.有实际移库数量输入且数量变化才往下执行&&transferOrderItem.getRealAmount().compareTo(oriItemViews[0].getRealAmount())!=0
                if (transferOrderItem.getRealAmount().compareTo(new BigDecimal(0)) >= 0) {
                    //todo 是移库前先把当前一步实际数量加回去可用数量
                    TransferStock fixTransferStock = new TransferStock();
                    fixTransferStock.setModifyAvailableAmount(transferOrderItem.getRealAmount().subtract(oriItemViews[0].getRealAmount()));//实际要移动的数量差值加回到可用数量
                    fixTransferStock.setSourceStorageLocationId(transferOrderItem.getSourceStorageLocationId());//修改源库位
                    fixTransferStock.setSupplyId(transferOrderItem.getSupplyId());
                    fixTransferStock.setUnit(transferOrderItem.getSourceUnit());
                    fixTransferStock.setUnitAmount(transferOrderItem.getSourceUnitAmount());
                    this.stockRecordService.modifyAvailableAmount(accountBook, fixTransferStock);

                    //TODO 如果传进来update的单位和原来条目单位不一致
                    if (!transferOrderItem.getUnit().equals(oriItemViews[0].getUnit()) || transferOrderItem.getUnitAmount().compareTo(oriItemViews[0].getUnitAmount()) != 0) {
                        if (oriItemViews[0].getRealAmount().compareTo(new BigDecimal(0)) != 0) //如果原来已经有移动的货物
                        {
                            // 先把之前的货物在库存里移动回去
                            TransferStock reTransferStock = new TransferStock();
                            reTransferStock.setNewStorageLocationId(transferOrderItem.getSourceStorageLocationId());
                            reTransferStock.setAmount(oriItemViews[0].getRealAmount());//本来已经移动了的数量
                            reTransferStock.setSourceStorageLocationId(transferOrderItem.getTargetStorageLocationId());//目标库位反转
                            reTransferStock.setSupplyId(transferOrderItem.getSupplyId());
                            reTransferStock.setRelatedOrderNo(transferOrderView.getNo());//获取单号
                            reTransferStock.setInventoryDate(new Timestamp(System.currentTimeMillis()));
                            reTransferStock.setUnit(oriItemViews[0].getUnit());
                            reTransferStock.setUnitAmount(oriItemViews[0].getUnitAmount());
                            reTransferStock.setNewUnit(transferOrderItem.getSourceUnit());
                            reTransferStock.setNewUnitAmount(transferOrderItem.getSourceUnitAmount());
                            this.stockRecordService.RealTransferStockUnitFlexible(accountBook, reTransferStock);//使用更新单位的库存修改


                            //实际移库操作
                            TransferStock transferStock = new TransferStock();
                            transferStock.setNewStorageLocationId(transferOrderItem.getTargetStorageLocationId());
                            transferStock.setAmount(transferOrderItem.getRealAmount());//计划数量
                            transferStock.setSourceStorageLocationId(transferOrderItem.getSourceStorageLocationId());//修改源库位可用数量
                            transferStock.setSupplyId(transferOrderItem.getSupplyId());
                            transferStock.setRelatedOrderNo(transferOrderView.getNo());//获取单号
                            transferStock.setInventoryDate(new Timestamp(System.currentTimeMillis()));
                            transferStock.setUnit(transferOrderItem.getSourceUnit());
                            transferStock.setUnitAmount(transferOrderItem.getSourceUnitAmount());
                            transferStock.setNewUnit(transferOrderItem.getUnit());
                            transferStock.setNewUnitAmount(transferOrderItem.getUnitAmount());
                            this.stockRecordService.RealTransferStockUnitFlexible(accountBook, transferStock);//使用更新单位的库存修改
                        }
                    } else {
                        //实际移库操作
                        TransferStock transferStock = new TransferStock();
                        transferStock.setNewStorageLocationId(transferOrderItem.getTargetStorageLocationId());
                        transferStock.setAmount(transferOrderItem.getRealAmount().subtract(oriItemViews[0].getRealAmount()));
                        transferStock.setSourceStorageLocationId(transferOrderItem.getSourceStorageLocationId());//修改源库位可用数量
                        transferStock.setSupplyId(transferOrderItem.getSupplyId());
                        transferStock.setRelatedOrderNo(transferOrderView.getNo());//获取单号
                        transferStock.setInventoryDate(new Timestamp(System.currentTimeMillis()));
                        transferStock.setUnit(transferOrderItem.getSourceUnit());
                        transferStock.setUnitAmount(transferOrderItem.getSourceUnitAmount());
                        transferStock.setNewUnit(transferOrderItem.getUnit());
                        transferStock.setNewUnitAmount(transferOrderItem.getUnitAmount());
                        this.stockRecordService.RealTransferStockUnitFlexible(accountBook, transferStock);
                    }

                }

                // TODO 如果计划移库数量发生变化,还是需要改进
                if (transferOrderItem.getScheduledAmount().compareTo(oriItemViews[0].getScheduledAmount()) != 0)//如果计划移库数量发生变化
                {

                    //否则修改计划移库数量并同步到库存记录可用数量
                    TransferStock fixTransferStock = new TransferStock();
                    fixTransferStock.setModifyAvailableAmount(oriItemViews[0].getScheduledAmount().subtract(transferOrderItem.getScheduledAmount()));//计算要修改的计划移库数量
                    fixTransferStock.setSourceStorageLocationId(transferOrderItem.getSourceStorageLocationId());//修改源库位
                    fixTransferStock.setSupplyId(transferOrderItem.getSupplyId());
                    fixTransferStock.setUnit(transferOrderItem.getSourceUnit());
                    fixTransferStock.setUnitAmount(transferOrderItem.getSourceUnitAmount());
                    this.stockRecordService.modifyAvailableAmount(accountBook, fixTransferStock);
                }
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


            //if (transferOrderItem.getPersonId() != null) {
             //   this.idChecker.check(PersonService.class, accountBook, transferOrderItem.getPersonId(), "作业人员");
            //}
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
