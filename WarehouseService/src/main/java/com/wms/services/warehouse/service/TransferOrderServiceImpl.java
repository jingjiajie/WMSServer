package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.InspectionNoteDAO;
import com.wms.services.warehouse.dao.TransferOrderDAO;
import com.wms.services.warehouse.datastructures.TransferFinishArgs;
import com.wms.services.warehouse.datastructures.TransferFinishItem;
import com.wms.utilities.IDChecker;
import com.wms.utilities.OrderNoGenerator;
import com.wms.utilities.ReflectHelper;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
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

    private static final String PREFIX = "T";

    @Override
    public int[] add(String accountBook, TransferOrder[] objs) throws WMSServiceException {
        Stream.of(objs).forEach(obj->{
            if(obj.getNo() == null || obj.getNo().isEmpty()) {
                obj.setNo(this.orderNoGenerator.generateNextNo(accountBook, PREFIX));
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
            TransferOrderView[] oriViews = this.transferOrderDAO.find(accountBook, new Condition().addCondition("id", id));
            if (oriViews.length == 0) {
                throw new WMSServiceException(String.format("移库单不存在，删除失败(%d)", id));
            }
            TransferOrderView oriView=oriViews[0];
            if (oriView.getState()!=0)
            {
                throw new WMSServiceException(String.format("移库单正在作业，无法删除(%s)", oriView.getId()));
            }
        }
        this.transferOrderDAO.remove(accountBook,ids);
    }

    @Override
    public TransferOrderView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.transferOrderDAO.find(accountBook,cond);
    }

    @Override
    public void transferFinish(String accountBook, TransferFinishArgs transferFinishArgs) throws WMSServiceException{
        if(transferFinishArgs.isAllFinish()){ //整单完成
            TransferOrderItemView[] transferOrderItemViews = this.transferOrderItemService.find(accountBook,new Condition().addCondition("inspectionNoteId",transferFinishArgs.getTransferOrderId()));
            TransferOrderItem[] transferOrderItems = ReflectHelper.createAndCopyFields(transferOrderItemViews,TransferOrderItem.class);

            TransferFinishItem[] transferFinishItems = transferFinishArgs.getTransferFinishItems();
            Stream.of(transferFinishItems).forEach(transferFinishItem -> {
                //检查有没有移库单条目,设置目标库位和源库位
                this.idChecker.check(TransferOrderItemService.class, accountBook, transferFinishArgs.getTransferOrderId(), "移库单条目");

                TransferOrderItemView transferOrderItemView = this.transferOrderItemService.find(accountBook, new Condition().addCondition("id", transferFinishItem.getTransferOrderItemId()))[0];
                TransferOrderItem transferOrderItem = ReflectHelper.createAndCopyFields(transferOrderItemView, TransferOrderItem.class);

                transferOrderItem.setSourceStorageLocationId(transferOrderItemView.getSourceStorageLocationId());
                transferOrderItem.setTargetStorageLocationId(transferOrderItemView.getTargetStorageLocationId());
                //如果请求单位改变则修改单位
                if (transferFinishItem.getTransferUnit()!=transferOrderItemView.getUnit())
                {
                    transferOrderItem.setUnit(transferFinishItem.getTransferUnit());
                    transferOrderItem.setUnitAmount(transferFinishItem.getTransferUnitAmount());
                }

            });
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

            Stream.of(transferOrderItems).forEach(transferOrderItem -> transferOrderItem.setState(TransferOrderItemService.STATE_ALL_FINISH));

            this.transferOrderItemService.update(accountBook, transferOrderItems);
        }
        else { //部分完成
            TransferFinishItem[] transferFinishItems = transferFinishArgs.getTransferFinishItems();
            Stream.of(transferFinishItems).forEach(transferFinishItem -> {
                //检查有没有移库单条目
                this.idChecker.check(TransferOrderItemService.class, accountBook, transferFinishArgs.getTransferOrderId(), "移库单条目");


                TransferOrderItemView transferOrderItemView = this.transferOrderItemService.find(accountBook, new Condition().addCondition("id", transferFinishItem.getTransferOrderItemId()))[0];
                TransferOrderItem transferOrderItem = ReflectHelper.createAndCopyFields(transferOrderItemView, TransferOrderItem.class);

                //把请求移动的数量加上已经移动的实际数量
                transferOrderItem.setRealAmount(transferFinishItem.getTransferAmount().add(transferOrderItemView.getRealAmount()));
                transferOrderItem.setUnit(transferFinishItem.getTransferUnit());
                transferOrderItem.setUnitAmount(transferFinishItem.getTransferUnitAmount());
                transferOrderItem.setSourceStorageLocationId(transferOrderItemView.getSourceStorageLocationId());
                transferOrderItem.setTargetStorageLocationId(transferOrderItemView.getTargetStorageLocationId());

                if(transferFinishItem.getPersonId() != -1){
                    transferOrderItem.setPersonId(transferFinishItem.getPersonId());
                }
                //部分完成移库操作
                transferOrderItem.setState(TransferOrderItemService.STATE_PARTIAL_FINNISH);

                this.transferOrderItemService.update(accountBook, new TransferOrderItem[]{transferOrderItem});
            });
        }
    }

    private void validateEntities(String accountBook,TransferOrder[] transferOrders) throws WMSServiceException{
        Stream.of(transferOrders).forEach((transferOrder -> {
            new Validator("状态").min(0).max(2).validate(transferOrder.getState());
            new Validator("创建时间").notnull().validate(transferOrder.getCreateTime());
            new Validator("打印次数").min(0).validate(transferOrder.getPrintTimes());

            idChecker.check(WarehouseService.class,accountBook,transferOrder.getWarehouseId(),"仓库");
                    //.check(PersonService.class,accountBook,transferOrder.getCreatePersonId(),"创建人员");
            if (transferOrder.getLastUpdatePersonId() != null && this.personService.find(accountBook,
                    new Condition().addCondition("id", transferOrder.getLastUpdatePersonId())).length == 0) {
                throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)", transferOrder.getLastUpdatePersonId()));
            }
        }));

    }
}
