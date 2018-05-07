package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.InspectionNoteDAO;
import com.wms.services.warehouse.dao.TransferOrderDAO;
import com.wms.services.warehouse.datastructures.InspectFinishArgs;
import com.wms.services.warehouse.datastructures.InspectFinishItem;
import com.wms.utilities.IDChecker;
import com.wms.utilities.OrderNoGenerator;
import com.wms.utilities.ReflectHelper;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.TransferOrder;
import com.wms.utilities.model.InspectionNoteItem;
import com.wms.utilities.model.InspectionNoteItemView;
import com.wms.utilities.model.TransferOrderView;
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
    InspectionNoteItemService inspectionNoteItemService;
    @Autowired
    IDChecker idChecker;
    @Autowired
    OrderNoGenerator orderNoGenerator;
    @Autowired
    PersonService personService;

    private static final String PREFIX = "J";

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
    public void transferFinish(String accountBook, InspectFinishArgs inspectFinishArgs) throws WMSServiceException{
        if(inspectFinishArgs.isAllFinish()){ //整单完成
            InspectionNoteItemView[] inspectionNoteItemViews = this.inspectionNoteItemService.find(accountBook,new Condition().addCondition("inspectionNoteId",inspectFinishArgs.getInspectionNoteId()));
            InspectionNoteItem[] inspectionNoteItems = ReflectHelper.createAndCopyFields(inspectionNoteItemViews,InspectionNoteItem.class);
            //如果设置了返回库位，则将每个条目返回到相应库位上。否则遵循各个条目原设置。
            if(inspectFinishArgs.getReturnStorageLocationId() != -1){
                idChecker.check(StorageLocationService.class,accountBook,inspectFinishArgs.getReturnStorageLocationId(),"返回库位");
                Stream.of(inspectionNoteItems).forEach(inspectionNoteItem -> {
                    inspectionNoteItem.setReturnStorageLocationId(inspectFinishArgs.getReturnStorageLocationId());
                });
            }
            //如果设置了人员，将每个条目的人员设置为相应人员。否则遵循各个条目原设置
            if(inspectFinishArgs.getPersonId() != -1){
                idChecker.check(PersonService.class,accountBook,inspectFinishArgs.getPersonId(),"作业人员");
                Stream.of(inspectionNoteItems).forEach(inspectionNoteItem -> inspectionNoteItem.setPersonId(inspectFinishArgs.getPersonId()));
            }
            //将每一条的返回数量设置为满额，单位设置成送检相同单位
            Stream.of(inspectionNoteItems).forEach(inspectionNoteItem -> {
                inspectionNoteItem.setReturnAmount(inspectionNoteItem.getAmount());
                inspectionNoteItem.setReturnUnit(inspectionNoteItem.getUnit());
                inspectionNoteItem.setReturnUnitAmount(inspectionNoteItem.getUnitAmount());
            });
            //如果是合格，则将每一项状态更新为合格，否则为不合格
            if(inspectFinishArgs.isQualified()){
                Stream.of(inspectionNoteItems).forEach(inspectionNoteItem -> inspectionNoteItem.setState(InspectionNoteItemService.STATE_QUALIFIED));
            }else{
                Stream.of(inspectionNoteItems).forEach(inspectionNoteItem -> inspectionNoteItem.setState(InspectionNoteItemService.STATE_UNQUALIFIED));
            }
            this.inspectionNoteItemService.update(accountBook, inspectionNoteItems);
        }else { //部分完成
            InspectFinishItem[] inspectFinishItems = inspectFinishArgs.getInspectFinishItems();
            Stream.of(inspectFinishItems).forEach(inspectFinishItem -> {
                this.idChecker.check(InspectionNoteItemService.class, accountBook, inspectFinishItem.getInspectionNoteItemId(), "送检单条目");
                InspectionNoteItemView inspectionNoteItemView = this.inspectionNoteItemService.find(accountBook, new Condition().addCondition("id", inspectFinishItem.getInspectionNoteItemId()))[0];
                InspectionNoteItem inspectionNoteItem = ReflectHelper.createAndCopyFields(inspectionNoteItemView, InspectionNoteItem.class);
                inspectionNoteItem.setReturnAmount(inspectFinishItem.getReturnAmount());
                inspectionNoteItem.setReturnUnit(inspectFinishItem.getReturnUnit());
                inspectionNoteItem.setReturnUnitAmount(inspectFinishItem.getReturnAmount());
                if(inspectFinishItem.getReturnStorageLocationId() != -1) {
                    inspectionNoteItem.setReturnStorageLocationId(inspectFinishItem.getReturnStorageLocationId());
                }
                if(inspectFinishItem.getPersonId() != -1){
                    inspectionNoteItem.setPersonId(inspectFinishItem.getPersonId());
                }
                if (inspectFinishItem.isQualified()) {
                    inspectionNoteItem.setState(InspectionNoteItemService.STATE_QUALIFIED);
                } else {
                    inspectionNoteItem.setState(InspectionNoteItemService.STATE_UNQUALIFIED);
                }
                this.inspectionNoteItemService.update(accountBook, new InspectionNoteItem[]{inspectionNoteItem});
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
