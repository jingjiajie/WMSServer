package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.InspectionNoteItemDAO;
import com.wms.services.warehouse.datastructures.TransferStock;
import com.wms.utilities.IDChecker;
import com.wms.utilities.ReflectHelper;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional
public class InspectionNoteItemServiceImpl
        implements InspectionNoteItemService {

    @Autowired
    InspectionNoteItemDAO inspectionNoteItemDAO;
    @Autowired
    WarehouseEntryItemService warehouseEntryItemService;
    @Autowired
    WarehouseEntryService warehouseEntryService;
    @Autowired
    InspectionNoteService inspectionNoteService;
    @Autowired
    StockRecordService stockRecordService;
    @Autowired
    IDChecker idChecker;

    @Override
    public int[] add(String accountBook, InspectionNoteItem[] objs) throws WMSServiceException {
        List<WarehouseEntryItem> warehouseEntryItemsToUpdate = new ArrayList<>();
        //根据每条送检单条目，更新入库单条目的送检数量
        Stream.of(objs).forEach(inspectionNoteItem -> {
            int warehouseEntryItemID = inspectionNoteItem.getWarehouseEntryItemId(); //入库单条目ID
            BigDecimal inspectAmount = inspectionNoteItem.getAmount(); //入库单条目要送检的数量
            //检查送检的入库单条目是否存在
            //this.idChecker.check(WarehouseEntryItemService.class, accountBook, warehouseEntryItemID, "送检入库单条目");
            //存在就把相应的入库单条目取出来。
            //由于数据库事务的原子性，可以保证上一条语句执行确定入库单条目存在和这一条语句之间，不会有任何进行任何其他的对该数据表进行的操作
            WarehouseEntryItem warehouseEntryItem = this.warehouseEntryItemService.get(accountBook,warehouseEntryItemID);
            new Validator("送检数量")
                    .min(0)
                    .max(warehouseEntryItem.getRealAmount().subtract(warehouseEntryItem.getInspectionAmount()))
                    .validate(inspectAmount);
            //更新入库单条目的送检数量
            warehouseEntryItem.setInspectionAmount(warehouseEntryItem.getInspectionAmount().add(inspectAmount));
            warehouseEntryItem.setState(WarehouseEntryItemService.BEING_INSPECTED);
            warehouseEntryItemsToUpdate.add(warehouseEntryItem);
        });
        this.validateEntities(accountBook, objs);
        warehouseEntryItemService.update(accountBook, ReflectHelper.listToArray(warehouseEntryItemsToUpdate,WarehouseEntryItem.class),true);
        return this.inspectionNoteItemDAO.add(accountBook, objs);
    }

    @Override
    public void update(String accountBook, InspectionNoteItem[] objs) throws WMSServiceException {
        this.validateEntities(accountBook, objs);
        List<Integer> inspectionNotesToUpdateState = new ArrayList<>();
        Stream.of(objs).forEach((inspectionNoteItem -> {
            InspectionNoteItem oriItem = this.inspectionNoteItemDAO.get(accountBook, inspectionNoteItem.getId());
            if (oriItem == null) {
                throw new WMSServiceException(String.format("送检单条目不存在，修改失败(%d)", inspectionNoteItem.getId()));
            }
            if(oriItem.getWarehouseEntryItemId() != inspectionNoteItem.getWarehouseEntryItemId()){
                throw new WMSServiceException("不能修改送检单条目关联的收货单条目！");
            }
            BigDecimal oriAmount = oriItem.getAmount(); //原送检数量
            BigDecimal deltaAmount = inspectionNoteItem.getAmount().subtract(oriAmount); //变化送检数量
            if (deltaAmount.compareTo(BigDecimal.ZERO) != 0) {
                throw new WMSServiceException("不允许修改计划送检数量！");
            }else if(!oriItem.getUnit().equals(inspectionNoteItem.getUnit())){
                throw new WMSServiceException("不允许修改送检单位！");
            }else if(oriItem.getUnitAmount().compareTo(inspectionNoteItem.getUnitAmount())!=0){
                throw new WMSServiceException("不允许修改送检单位数量！");
            }
            if(oriItem.getState() != inspectionNoteItem.getState() && !inspectionNotesToUpdateState.contains(inspectionNoteItem.getInspectionNoteId())){
                inspectionNotesToUpdateState.add(inspectionNoteItem.getInspectionNoteId());
            }
        }));
        this.inspectionNoteItemDAO.update(accountBook, objs);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        throw new WMSServiceException("送检单条目不能删除");
//        //没同步库存
//        for (int id : ids) {
//            idChecker.check(this.getClass(), accountBook, id, "删除的送检单条目");
//        }
//        this.inspectionNoteItemDAO.remove(accountBook, ids);
    }

    @Override
    public InspectionNoteItemView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.inspectionNoteItemDAO.find(accountBook, cond);
    }

    private void validateEntities(String accountBook, InspectionNoteItem[] inspectionNoteItems) {
        Stream.of(inspectionNoteItems).forEach((inspectionNoteItem -> {
            //数据验证
            new Validator("状态").min(0).max(2).validate(inspectionNoteItem.getState());
            new Validator("送检单位").notnull().notEmpty().validate(inspectionNoteItem.getUnit());
            new Validator("送检数量").min(0).validate(inspectionNoteItem.getAmount());
            new Validator("送检单位数量").min(0).validate(inspectionNoteItem.getUnitAmount());
            if (inspectionNoteItem.getReturnAmount() != null) {
                new Validator("送检返回数量").min(0).max(inspectionNoteItem.getAmount()).validate(inspectionNoteItem.getAmount());
            }
            if (inspectionNoteItem.getReturnUnitAmount() != null) {
                new Validator("送检返回单位数量").min(0).validate(inspectionNoteItem.getReturnUnitAmount());
            }
            //验证外键
            this.idChecker.check(InspectionNoteService.class, accountBook, inspectionNoteItem.getInspectionNoteId(), "关联送检单");
            this.idChecker.check(WarehouseEntryItemService.class, accountBook, inspectionNoteItem.getWarehouseEntryItemId(), "关联入库单条目");
            if (inspectionNoteItem.getPersonId() != null) {
                this.idChecker.check(PersonService.class, accountBook, inspectionNoteItem.getPersonId(), "作业人员");
            }
        }));
    }


    @Override
    public long findCount(String accountBook, Condition cond) throws WMSServiceException{
        return this.inspectionNoteItemDAO.findCount(accountBook,cond);
    }
}
