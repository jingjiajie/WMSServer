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
            obj.setCreateTime(new Timestamp(System.currentTimeMillis()));
        });
        this.validateEntities(accountBook,objs);
        return transferOrderDAO.add(accountBook,objs);
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
                throw new WMSServiceException(String.format("送检单不存在，请重新查询(%d)",id));
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

            //如果设置了返回库位，则将每个条目返回到相应库位上。否则遵循各个条目原设置。
            if(transferFinishArgs.getTargetStorageLocationId() != -1){
                idChecker.check(StorageLocationService.class,accountBook,transferFinishArgs.getTargetStorageLocationId(),"目标库位");
                Stream.of(transferOrderItems).forEach(inspectionNoteItem -> {
                    inspectionNoteItem.setTargetStorageLocationId(transferFinishArgs.getTargetStorageLocationId());
                });
            }
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
                this.idChecker.check(TransferOrderItemService.class, accountBook, transferFinishArgs.getTransferOrderId(), "移库单条目");


                TransferOrderItemView transferOrderItemView = this.transferOrderItemService.find(accountBook, new Condition().addCondition("id", transferFinishItem.getTransferOrderItemId()))[0];
                TransferOrderItem transferOrderItem = ReflectHelper.createAndCopyFields(transferOrderItemView, TransferOrderItem.class);

                transferOrderItem.setRealAmount(transferFinishItem.getTransferAmount());
                transferOrderItem.setUnit(transferFinishItem.getTransferUnit());
                transferOrderItem.setUnitAmount(transferFinishItem.getTransferUnitAmount());


                if(transferFinishItem.getTargetStorageLocationId() != -1) {
                    transferOrderItem.setTargetStorageLocationId(transferFinishItem.getTargetStorageLocationId());
                }
                if(transferFinishItem.getPersonId() != -1){
                    transferOrderItem.setPersonId(transferFinishItem.getPersonId());
                }
                //部分完成移库操作
                if (transferFinishItem.isQualified()) {
                    transferOrderItem.setState(TransferOrderItemService.STATE_PARTIAL_FINNISH);
                } else {
                    transferOrderItem.setState(TransferOrderItemService.STATE_IN_TRANSFER);
                }
                this.transferOrderItemService.update(accountBook, new TransferOrderItem[]{transferOrderItem});
            });
        }
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
}
