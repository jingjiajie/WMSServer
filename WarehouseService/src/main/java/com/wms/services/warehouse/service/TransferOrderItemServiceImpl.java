package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.TransferOrderItemDAO;
import com.wms.services.warehouse.datastructures.TransferStock;
import com.wms.utilities.IDChecker;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wms.utilities.model.TransferOrderItem;
import com.wms.utilities.model.TransferOrderItemView;

import javax.enterprise.inject.New;
import java.math.BigDecimal;
import java.sql.Timestamp;
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
            TransferOrderView[] foundTransferOrders = this.transferOrderService.find(accountBook,new Condition().addCondition("id",new Integer[]{transferOrderId}));
            if(foundTransferOrders.length == 0){
                throw new WMSServiceException(String.format("移库单不存在，请重新提交！(%d)",transferOrderId));
            }

            //更新库存
            TransferStock transferStock = new TransferStock();
            transferStock.setModifyAvailableAmount(new BigDecimal(0).subtract(transferOrderItem.getScheduledAmount()));//计划数量
            transferStock.setSourceStorageLocationId(transferOrderItem.getSourceStorageLocationId());//修改源库位可用数量
            transferStock.setSupplyId(transferOrderItem.getSupplyId());
            transferStock.setUnit(transferOrderItem.getUnit());
            transferStock.setUnitAmount(transferOrderItem.getUnitAmount());
            //transferStock.setInventoryDate(new Timestamp(System.currentTimeMillis()));
            this.stockRecordService.modifyAvailableAmount(accountBook, transferStock);
            transferOrderItem.setState(TransferOrderItemService.STATE_IN_TRANSFER);

        });
        this.validateEntities(accountBook, transferOrderItems);
        //todo this.updateTransferOrder(accountBook,transferOrderItems[0].getTransferOrderId() ,transferOrderItems[0].getPersonId());
        return this.transferOrderItemDAO.add(accountBook, transferOrderItems);
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
            if(foundTransferOrders.length == 0){
                throw new WMSServiceException(String.format("移库单不存在，请重新提交！(%d)",transferOrderId));
            }
            TransferOrderView transferOrderView = foundTransferOrders[0];
            //无法中途修改目标库位
            if (transferOrderItem.getTargetStorageLocationId()!=oriItemViews[0].getTargetStorageLocationId())
            {
                throw new WMSServiceException("无法修改移库单条目的目标库位:(%d)，如要操作请新建移库单"+oriItemViews[0].getTargetStorageLocationId());
            }
            if (transferOrderItem.getRealAmount().compareTo(oriItemViews[0].getRealAmount())<0)
            {
                throw new WMSServiceException("无法修改移库单条目的实际移动数量，如要操作请新建移库单");
            }
            // TODO 如果计划移库数量发生变化,还是需要改进
            if (transferOrderItem.getScheduledAmount().equals(oriItemViews[0].getScheduledAmount()))//如果计划移库数量发生变化
            {
                if (transferOrderItem.getScheduledAmount().subtract(oriItemViews[0].getRealAmount()).compareTo(new BigDecimal(0))<=0)//如果新修改时计划数量小于当前实际已经移动的数量
                {
                    throw new WMSServiceException(String.format("无法修改移库单条目计划数量单号：(%s)，移库操作基本完成，如需操作请新建移库单作业",transferOrderView.getNo()));
                }
                //否则修改计划移库数量并同步到库存记录可用数量
                TransferStock fixTransferStock = new TransferStock();
                fixTransferStock.setModifyAvailableAmount(oriItemViews[0].getScheduledAmount().subtract(transferOrderItem.getScheduledAmount()));//计算要修改的计划移库数量
                fixTransferStock.setSourceStorageLocationId(transferOrderItem.getSourceStorageLocationId());//修改源库位
                fixTransferStock.setSupplyId(transferOrderItem.getSupplyId());
                fixTransferStock.setUnit(transferOrderItem.getUnit());
                fixTransferStock.setUnitAmount(transferOrderItem.getUnitAmount());
                this.stockRecordService.modifyAvailableAmount(accountBook, fixTransferStock);
            }

            //如果没有实际移库数量输入，直接跳过.有实际移库数量输入且数量变化才往下执行
            if(transferOrderItem.getRealAmount().compareTo(new BigDecimal(0))>0&&!transferOrderItem.getRealAmount().equals(oriItemViews[0].getRealAmount()))
            {
                //todo 是移库前先把当前一步实际数量加回去可用数量
                TransferStock fixTransferStock = new TransferStock();
                fixTransferStock.setModifyAvailableAmount(transferOrderItem.getRealAmount());//实际要移动的数量加回到可用数量
                fixTransferStock.setSourceStorageLocationId(transferOrderItem.getSourceStorageLocationId());//修改源库位
                fixTransferStock.setSupplyId(transferOrderItem.getSupplyId());
                fixTransferStock.setUnit(transferOrderItem.getUnit());
                fixTransferStock.setUnitAmount(transferOrderItem.getUnitAmount());
                this.stockRecordService.modifyAvailableAmount(accountBook, fixTransferStock);

                if (!transferOrderItem.getUnit().equals(oriItemViews[0].getUnit()))//如果传进来update的单位和原来条目单位不一致
                {
                    if (oriItemViews[0].getRealAmount().compareTo(new BigDecimal(0))!=0) //如果原来已经有移动的货物
                    {
                        //TODO 先把之前的货物在库存里移动回去
                        TransferStock reTransferStock = new TransferStock();
                        reTransferStock.setNewStorageLocationId(transferOrderItem.getSourceStorageLocationId());
                        reTransferStock.setAmount(oriItemViews[0].getRealAmount());//本来已经移动了的数量
                        reTransferStock.setSourceStorageLocationId(transferOrderItem.getTargetStorageLocationId());//目标库位反转
                        reTransferStock.setSupplyId(transferOrderItem.getSupplyId());
                        reTransferStock.setRelatedOrderNo(transferOrderView.getNo());//获取单号
                        reTransferStock.setInventoryDate(new Timestamp(System.currentTimeMillis()));
                        reTransferStock.setUnit(oriItemViews[0].getUnit());
                        reTransferStock.setUnitAmount(oriItemViews[0].getUnitAmount());
                        this.stockRecordService.RealTransferStockUnitFlexible(accountBook,reTransferStock);//使用更新单位的库存修改


                        //实际移库操作
                        TransferStock transferStock = new TransferStock();
                        transferStock.setNewStorageLocationId(transferOrderItem.getTargetStorageLocationId());
                        transferStock.setAmount(transferOrderItem.getRealAmount());//计划数量
                        transferStock.setSourceStorageLocationId(transferOrderItem.getSourceStorageLocationId());//修改源库位可用数量
                        transferStock.setSupplyId(transferOrderItem.getSupplyId());
                        transferStock.setRelatedOrderNo(transferOrderView.getNo());//获取单号
                        transferStock.setInventoryDate(new Timestamp(System.currentTimeMillis()));
                        transferStock.setUnit(oriItemViews[0].getUnit());
                        transferStock.setUnitAmount(oriItemViews[0].getUnitAmount());
                        transferStock.setNewUnit(transferOrderItem.getUnit());
                        transferStock.setNewUnitAmount(transferOrderItem.getUnitAmount());
                        this.stockRecordService.RealTransferStockUnitFlexible(accountBook, transferStock);//使用更新单位的库存修改
                    }
                } else {
                    //实际移库操作
                    TransferStock transferStock = new TransferStock();
                    transferStock.setNewStorageLocationId(transferOrderItem.getTargetStorageLocationId());
                    transferStock.setAmount(transferOrderItem.getRealAmount().subtract(oriItemViews[0].getRealAmount()));//计划数量
                    transferStock.setSourceStorageLocationId(transferOrderItem.getSourceStorageLocationId());//修改源库位可用数量
                    transferStock.setSupplyId(transferOrderItem.getSupplyId());
                    transferStock.setRelatedOrderNo(transferOrderView.getNo());//获取单号
                    transferStock.setInventoryDate(new Timestamp(System.currentTimeMillis()));
                    transferStock.setUnit(oriItemViews[0].getUnit());
                    transferStock.setUnitAmount(oriItemViews[0].getUnitAmount());
                    this.stockRecordService.RealTransformStock(accountBook, transferStock);
                }
                transferOrderItem.setState(1);
                if (transferOrderItem.getScheduledAmount().equals(transferOrderItem.getRealAmount())){
                    transferOrderItem.setState(2);
                }
            }

        }));
        //todo this.updateTransferOrder(accountBook,transferOrderItems[0].getTransferOrderId() ,transferOrderItems[0].getPersonId());
        this.transferOrderItemDAO.update(accountBook, transferOrderItems);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        for (int id : ids) {
            //todo idChecker.check(this.getClass(), accountBook, id, "删除的移库单条目");

            TransferOrderItemView[] oriItemViews = this.transferOrderItemDAO.find(accountBook, new Condition().addCondition("id", new Integer[]{id}));
            if (oriItemViews.length == 0) {
                throw new WMSServiceException(String.format("移库单条目不存在，删除失败(%d)", id));
            }
            TransferOrderItemView oriItemView=oriItemViews[0];
            if (oriItemView.getState()!=0)
            {
                throw new WMSServiceException(String.format("移库单条目正在作业，无法删除(%s)", oriItemView.getId()));
            }else
            {
                //删除了未经过操作的移库单，更新库存可用数量
                TransferStock transferStock = new TransferStock();
                transferStock.setModifyAvailableAmount(oriItemView.getScheduledAmount());//计划数量
                transferStock.setSourceStorageLocationId(oriItemView.getSourceStorageLocationId());//修改源库位可用数量
                transferStock.setSupplyId(oriItemView.getSupplyId());
                transferStock.setUnit(oriItemView.getUnit());
                transferStock.setUnitAmount(oriItemView.getUnitAmount());
                this.stockRecordService.modifyAvailableAmount(accountBook, transferStock);
            }
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


            //if (transferOrderItem.getPersonId() != null) {
             //   this.idChecker.check(PersonService.class, accountBook, transferOrderItem.getPersonId(), "作业人员");
            //}
        }));
    }


    @Override
    public long findCount(String accountBook, Condition cond) throws WMSServiceException{
        return this.transferOrderItemDAO.findCount(accountBook,cond);
    }

    private void updateTransferOrder( String accountBook,int transferOrderId ,int lastUpdatePersonId){
        //TODO idChecker.check(PersonService.class,accountBook,lastUpdatePersonId," 人员");
        TransferOrderView[] transferOrderViews= transferOrderService.find(accountBook,new Condition().addCondition("id",new Integer[]{transferOrderId}));
        if(transferOrderViews.length==0){
            throw new WMSServiceException("没有找到要更新的盘点单！");
        }
        TransferOrder transferOrder=new TransferOrder();
        transferOrder.setId(transferOrderViews[0].getId());
        transferOrder.setCreatePersonId(transferOrderViews[0].getCreatePersonId());
        transferOrder.setCreateTime(transferOrderViews[0].getCreateTime());
        transferOrder.setDescription(transferOrderViews[0].getDescription());
        transferOrder.setNo(transferOrderViews[0].getNo());
        transferOrder.setWarehouseId(transferOrderViews[0].getWarehouseId());
        transferOrder.setLastUpdatePersonId(""+lastUpdatePersonId);
        transferOrder.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
        transferOrder.setPrintTimes(transferOrderViews[0].getPrintTimes());
        transferOrder.setState(transferOrderViews[0].getState());
        transferOrderService.update(accountBook,new TransferOrder[]{transferOrder});
    }
}
