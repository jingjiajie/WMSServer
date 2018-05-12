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
    InspectionNoteService inspectionNoteService;
    @Autowired
    StockRecordService stockRecordService;
    @Autowired
    IDChecker idChecker;

    @Override
    public int[] add(String accountBook, InspectionNoteItem[] objs) throws WMSServiceException {
        //根据每条送检单条目，更新入库单条目的送检数量
        Stream.of(objs).forEach(inspectionNoteItem -> {
            int warehouseEntryItemID = inspectionNoteItem.getWarehouseEntryItemId(); //入库单条目ID
            BigDecimal inspectAmount = inspectionNoteItem.getAmount(); //入库单条目要送检的数量
            //检查送检的入库单条目是否存在
            this.idChecker.check(WarehouseEntryItemService.class,accountBook,warehouseEntryItemID,"送检入库单条目");
            //存在就把相应的入库单条目取出来。
            //由于数据库事务的原子性，可以保证上一条语句执行确定入库单条目存在和这一条语句之间，不会有任何进行任何其他的对该数据表进行的操作
            WarehouseEntryItemView warehouseEntryItemView = this.warehouseEntryItemService.find(accountBook,
                    new Condition().addCondition("id",warehouseEntryItemID))[0];
            new Validator("送检数量")
                    .min(0)
                    .max(warehouseEntryItemView.getRealAmount().subtract(warehouseEntryItemView.getInspectionAmount()))
                    .validate(inspectAmount);
            //更新入库单条目的送检数量
            WarehouseEntryItem warehouseEntryItem = ReflectHelper.createAndCopyFields(warehouseEntryItemView,WarehouseEntryItem.class);
            warehouseEntryItem.setInspectionAmount(warehouseEntryItem.getInspectionAmount().add(inspectAmount));
            warehouseEntryItemService.update(accountBook,new WarehouseEntryItem[]{warehouseEntryItem});

            //获取送检单
            int inspectionNoteID = inspectionNoteItem.getInspectionNoteId();
            InspectionNoteView[] foundInspectionNotes = this.inspectionNoteService.find(accountBook,new Condition().addCondition("id",inspectionNoteID));
            if(foundInspectionNotes.length == 0){
                throw new WMSServiceException(String.format("送检单不存在，请重新提交！(%d)",inspectionNoteID));
            }
            InspectionNoteView inspectionNoteView = foundInspectionNotes[0];
            //更新库存
            TransferStock transferStock = new TransferStock();
            //TODO TransferStock该字段不应该是整型 transferStock.setAmount(inspectionNoteItem.getAmount());
            transferStock.setNewStorageLocationId(inspectionNoteItem.getInspectionStorageLocationId());
            transferStock.setRelatedOrderNo(inspectionNoteView.getNo());
            transferStock.setSourceStorageLocationId(warehouseEntryItem.getStorageLocationId());
            transferStock.setSupplyId(warehouseEntryItem.getSupplyId());
            transferStock.setUnit(inspectionNoteItem.getUnit());
            //TODO TransferStock该字段不应该是整型 transferStock.setUnitAmount(inspectionNoteItem.getUnitAmount());
           //TODO 改了函数名 this.stockRecordService.transformStock(accountBook, transferStock);
        });

        this.validateEntities(accountBook, objs);
        return this.inspectionNoteItemDAO.add(accountBook, objs);
    }

    @Override
    public void update(String accountBook, InspectionNoteItem[] objs) throws WMSServiceException {
        this.validateEntities(accountBook, objs);
        Stream.of(objs).forEach((inspectionNoteItem -> {
            //如果送检数量变化，反映到关联入库单条目和库存上
            //TODO 库存API还没做
            InspectionNoteItemView[] oriItemViews = this.inspectionNoteItemDAO.find(accountBook, new Condition().addCondition("id", inspectionNoteItem.getId()));
            if (oriItemViews.length == 0) {
                throw new WMSServiceException(String.format("送检单条目不存在，修改失败(%d)", inspectionNoteItem.getId()));
            }
            //如果返回数量变化，反映到库存中
            //TODO 库存API还没做

            InspectionNoteItemView oriItemView = oriItemViews[0];//原对象
            BigDecimal oriAmount = oriItemView.getAmount(); //原送检数量
            BigDecimal deltaAmount = inspectionNoteItem.getAmount().subtract(oriAmount); //变化送检数量
            //加回入库单条目中
            WarehouseEntryItemView warehouseEntryItemView = this.warehouseEntryItemService.find(accountBook, new Condition().addCondition("id", inspectionNoteItem.getWarehouseEntryItemId()))[0];
            WarehouseEntryItem warehouseEntryItem = ReflectHelper.createAndCopyFields(warehouseEntryItemView, WarehouseEntryItem.class);
            warehouseEntryItem.setInspectionAmount(warehouseEntryItemView.getInspectionAmount().add(deltaAmount));
            warehouseEntryItemService.update(accountBook, new WarehouseEntryItem[]{warehouseEntryItem});
        }));
        this.inspectionNoteItemDAO.update(accountBook, objs);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        //TODO 没同步库存
        for (int id : ids) {
            idChecker.check(this.getClass(), accountBook, id, "删除的送检单条目");
        }
        this.inspectionNoteItemDAO.remove(accountBook, ids);
    }

    @Override
    public InspectionNoteItemView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.inspectionNoteItemDAO.find(accountBook, cond);
    }

    private void validateEntities(String accountBook, InspectionNoteItem[] inspectionNoteItems) {
        Stream.of(inspectionNoteItems).forEach((inspectionNoteItem -> {
            //数据验证
            new Validator("状态").min(0).max(2).validate(inspectionNoteItem.getState());
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
            this.idChecker.check(StorageLocationService.class, accountBook, inspectionNoteItem.getInspectionStorageLocationId(), "送检库位");
            if (inspectionNoteItem.getReturnStorageLocationId() != null) {
                this.idChecker.check(StorageLocationService.class, accountBook, inspectionNoteItem.getReturnStorageLocationId(), "返回库位");
            }
            if (inspectionNoteItem.getPersonId() != null) {
                this.idChecker.check(PersonService.class, accountBook, inspectionNoteItem.getPersonId(), "作业人员");
            }
        }));
    }
}
