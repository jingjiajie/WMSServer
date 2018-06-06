package com.wms.services.warehouse.service;

import com.wms.services.ledger.service.PersonService;
import com.wms.services.warehouse.dao.InspectionNoteDAO;
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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class InspectionNoteServiceImpl
    implements InspectionNoteService{
    @Autowired
    InspectionNoteDAO inspectionNoteDAO;
    @Autowired
    InspectionNoteItemService inspectionNoteItemService;
    @Autowired
    IDChecker idChecker;
    @Autowired
    OrderNoGenerator orderNoGenerator;
    @Autowired
    WarehouseEntryService warehouseEntryService;
    @Autowired
    WarehouseEntryItemService warehouseEntryItemService;
    @Autowired
    StockRecordService stockRecordService;

    private static final String PREFIX = "J";

    @Override
    public int[] add(String accountBook, InspectionNote[] objs) throws WMSServiceException {
        Stream.of(objs).forEach(obj->{
            if(obj.getNo() == null || obj.getNo().isEmpty()) {
                obj.setNo(this.orderNoGenerator.generateNextNo(accountBook, PREFIX));
            }
            obj.setCreateTime(new Timestamp(System.currentTimeMillis()));
        });
        this.validateEntities(accountBook,objs);
        return inspectionNoteDAO.add(accountBook,objs);
    }

    @Override
    public void update(String accountBook, InspectionNote[] objs) throws WMSServiceException {
        Stream.of(objs).forEach((obj)->{
            //new Validator("最后更新人员").notnull().validate(obj.getLastUpdatePersonId());
            obj.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
        });
        this.validateEntities(accountBook,objs);
        this.inspectionNoteDAO.update(accountBook,objs);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        for(int id : ids){
            if(this.inspectionNoteDAO.find(accountBook,new Condition().addCondition("id",id)).length == 0){
                throw new WMSServiceException(String.format("送检单不存在，请重新查询(%d)",id));
            }
        }
        this.inspectionNoteDAO.remove(accountBook,ids);
    }

    @Override
    public InspectionNoteView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.inspectionNoteDAO.find(accountBook,cond);
    }

    @Override
    public void inspectFinish(String accountBook, InspectFinishArgs inspectFinishArgs) throws WMSServiceException {
        if (inspectFinishArgs.isAllFinish()) { //整单完成
            //this.idChecker.check(InspectionNoteService.class,accountBook,inspectFinishArgs.getInspectionNoteId(),"送检单");
            InspectionNote inspectionNote = this.inspectionNoteDAO.get(accountBook, inspectFinishArgs.getInspectionNoteId());
            if (inspectionNote.getState() == ALL_INSPECTED) {
                throw new WMSServiceException("送检单 " + inspectionNote.getNo() + " 已送检完成，请不要重复操作！");
            }
            inspectionNote.setState(ALL_INSPECTED);
            inspectionNote.setLastUpdatePersonId(inspectFinishArgs.getPersonId());
            this.update(accountBook, new InspectionNote[]{inspectionNote});
            InspectionNoteItemView[] inspectionNoteItemViews = this.inspectionNoteItemService.find(accountBook,
                    new Condition().addCondition("inspectionNoteId", inspectFinishArgs.getInspectionNoteId())
                            .addCondition("state", InspectionNoteItemService.NOT_INSPECTED));
            if (inspectionNoteItemViews.length == 0) {
                return;
            }
            InspectionNoteItem[] inspectionNoteItems = ReflectHelper.createAndCopyFields(inspectionNoteItemViews, InspectionNoteItem.class);

            //如果设置了人员，将每个条目的人员设置为相应人员。否则遵循各个条目原设置
            if (inspectFinishArgs.getPersonId() != -1) {
                idChecker.check(PersonService.class, accountBook, inspectFinishArgs.getPersonId(), "作业人员");
                Stream.of(inspectionNoteItems).forEach(inspectionNoteItem -> inspectionNoteItem.setPersonId(inspectFinishArgs.getPersonId()));
            }
            //将每一条的返回数量设置为满额，单位设置成送检相同单位
            Stream.of(inspectionNoteItems).forEach(inspectionNoteItem -> {
                inspectionNoteItem.setReturnAmount(inspectionNoteItem.getAmount());
                inspectionNoteItem.setReturnUnit(inspectionNoteItem.getUnit());
                inspectionNoteItem.setReturnUnitAmount(inspectionNoteItem.getUnitAmount());
            });
            //如果是合格，则将每一项状态更新为合格，否则为不合格，并调用入库单的收货功能。
            if (inspectFinishArgs.isQualified()) {
                Stream.of(inspectionNoteItems).forEach(inspectionNoteItem -> inspectionNoteItem.setState(InspectionNoteItemService.QUALIFIED));
                this.warehouseEntryItemService.receive(accountBook, Stream.of(inspectionNoteItems).map((item) -> item.getWarehouseEntryItemId()).collect(Collectors.toList()),null);
            } else {
                Stream.of(inspectionNoteItems).forEach(inspectionNoteItem -> inspectionNoteItem.setState(InspectionNoteItemService.UNQUALIFIED));
                this.warehouseEntryItemService.reject(accountBook, Stream.of(inspectionNoteItems).map((item) -> item.getWarehouseEntryItemId()).collect(Collectors.toList()),null);
            }
            this.inspectionNoteItemService.update(accountBook, inspectionNoteItems);
        } else { //部分完成
            Map<Integer, BigDecimal> warehouseEntryItemAndReturnAmount = new HashMap<>();
            List<Integer> warehouseEntryIDsToReceive = new ArrayList<>();
            List<Integer> warehouseEntryIDsToReject = new ArrayList<>();
            List<InspectionNoteItem> inspectionNoteItemsToUpdate = new ArrayList<>();
            InspectFinishItem[] inspectFinishItems = inspectFinishArgs.getInspectFinishItems();
            Stream.of(inspectFinishItems).forEach(inspectFinishItem -> {
//                this.idChecker.check(InspectionNoteItemService.class, accountBook, inspectFinishItem.getInspectionNoteItemId(), "送检单条目");
                InspectionNoteItem inspectionNoteItem = this.inspectionNoteItemService.get(accountBook, inspectFinishItem.getInspectionNoteItemId());
                inspectionNoteItem.setReturnAmount(inspectFinishItem.getReturnAmount());
                inspectionNoteItem.setReturnUnit(inspectFinishItem.getReturnUnit());
                inspectionNoteItem.setReturnUnitAmount(inspectFinishItem.getReturnAmount());
                WarehouseEntryItemView warehouseEntryItemView = this.warehouseEntryItemService.find(accountBook, new Condition().addCondition("id", inspectionNoteItem.getWarehouseEntryItemId()))[0];
                warehouseEntryItemAndReturnAmount.put(inspectionNoteItem.getWarehouseEntryItemId(), inspectionNoteItem.getReturnAmount());
                //如果返回数量小于送检数量，则将差值从入库单条目的库存里扣除，再收货。
                BigDecimal unreturnedAmount = inspectionNoteItem.getAmount().subtract(inspectionNoteItem.getReturnAmount());
                if (unreturnedAmount.compareTo(BigDecimal.ZERO) != 0) {
                    TransferStock transferStock = new TransferStock();
                    transferStock.setAmount(unreturnedAmount.negate());
                    transferStock.setUnit(warehouseEntryItemView.getUnit());
                    transferStock.setUnitAmount(warehouseEntryItemView.getUnitAmount());
                    transferStock.setSupplyId(warehouseEntryItemView.getSupplyId());
                    transferStock.setSourceStorageLocationId(warehouseEntryItemView.getStorageLocationId());
                    transferStock.setRelatedOrderNo(warehouseEntryItemView.getWarehouseEntryNo());
                    this.stockRecordService.addAmount(accountBook, transferStock);
                }
                if (inspectFinishItem.getPersonId() != null) {
                    inspectionNoteItem.setPersonId(inspectFinishItem.getPersonId());
                }
                if (inspectFinishItem.isQualified()) {
                    inspectionNoteItem.setState(InspectionNoteItemService.QUALIFIED);
                    warehouseEntryIDsToReceive.add(inspectionNoteItem.getWarehouseEntryItemId());
                } else {
                    inspectionNoteItem.setState(InspectionNoteItemService.UNQUALIFIED);
                    warehouseEntryIDsToReject.add(inspectionNoteItem.getWarehouseEntryItemId());
                }
                inspectionNoteItemsToUpdate.add(inspectionNoteItem);
            });
            if (inspectionNoteItemsToUpdate.size() == 0) {
                return;
            }
            this.inspectionNoteItemService.update(accountBook, ReflectHelper.listToArray(inspectionNoteItemsToUpdate, InspectionNoteItem.class));
            if (warehouseEntryIDsToReceive.size() > 0) {
                this.warehouseEntryItemService.receive(accountBook, warehouseEntryIDsToReceive,warehouseEntryItemAndReturnAmount);
            }
            if (warehouseEntryIDsToReject.size() > 0) {
                this.warehouseEntryItemService.reject(accountBook, warehouseEntryIDsToReject,warehouseEntryItemAndReturnAmount);
            }
        }
    }

    private void validateEntities(String accpountBook,InspectionNote[] inspectionNotes) throws WMSServiceException{
        Stream.of(inspectionNotes).forEach((inspectionNote -> {
            new Validator("状态").min(0).max(2).validate(inspectionNote.getState());
            new Validator("创建时间").notnull().validate(inspectionNote.getCreateTime());

            idChecker.check(WarehouseEntryService.class,accpountBook,inspectionNote.getWarehouseEntryId(),"关联入库单")
                    .check(WarehouseService.class,accpountBook,inspectionNote.getWarehouseId(),"仓库")
                    .check(PersonService.class,accpountBook,inspectionNote.getCreatePersonId(),"创建人员");
            if(inspectionNote.getLastUpdatePersonId() != null){
                idChecker.check(PersonService.class,accpountBook,inspectionNote.getLastUpdatePersonId(),"最后更新人员");
            }
        }));
    }


    @Override
    public long findCount(String accountBook, Condition cond) throws WMSServiceException{
        return this.inspectionNoteDAO.findCount(accountBook,cond);
    }

    @Override
    public void updateState(String accountBook, List<Integer> ids) {
        List<InspectionNote> inspectionNotesToUpdate = new ArrayList<>();
        for (int id : ids) {
            this.idChecker.check(InspectionNoteService.class, accountBook, id, "送检单");
            InspectionNote inspectionNote = ReflectHelper.createAndCopyFields(this.find(accountBook, new Condition().addCondition("id", id))[0],InspectionNote.class);
            inspectionNotesToUpdate.add(inspectionNote);
            InspectionNoteItemView[] inspectionNoteItemViews = this.inspectionNoteItemService.find(accountBook, new Condition().addCondition("inspectionNoteId", id));
            long total = inspectionNoteItemViews.length;
            long notInspectedCount = Stream.of(inspectionNoteItemViews).filter((item)->item.getState()==InspectionNoteItemService.NOT_INSPECTED).count();
            long qualifiedCount = Stream.of(inspectionNoteItemViews).filter((item)->item.getState()==InspectionNoteItemService.QUALIFIED).count();
            long unqualifiedCount = Stream.of(inspectionNoteItemViews).filter((item)->item.getState()==InspectionNoteItemService.UNQUALIFIED).count();
            if(notInspectedCount == total) {
                inspectionNote.setState(NOT_INSPECTED);
            }else if(qualifiedCount+unqualifiedCount == total){
                inspectionNote.setState(ALL_INSPECTED);
            }else {
                inspectionNote.setState(PART_INSPECTED);
            }
        }
        this.update(accountBook, ReflectHelper.listToArray(inspectionNotesToUpdate,InspectionNote.class));
    }

    @Override
    public List<InspectionNoteAndItems> getPreviewData(String accountBook, List<Integer> inspectionNoteIDs,boolean qualifiedOnly) throws WMSServiceException{
        InspectionNoteView[] inspectionNoteViews = this.inspectionNoteDAO.find(accountBook,new Condition().addCondition("id",inspectionNoteIDs.toArray(), ConditionItem.Relation.IN));
        Condition condItem = new Condition().addCondition("inspectionNoteId",inspectionNoteIDs.toArray(), ConditionItem.Relation.IN);
        if(qualifiedOnly){
            condItem.addCondition("state",InspectionNoteItemService.QUALIFIED);
        }
        InspectionNoteItemView[] itemViews = this.inspectionNoteItemService.find(accountBook,condItem);
        List<InspectionNoteAndItems> result = new ArrayList<>();
        for(InspectionNoteView inspectionNoteView : inspectionNoteViews){
            InspectionNoteAndItems inspectionNoteAndItems = new InspectionNoteAndItems();
            inspectionNoteAndItems.setInspectionNote(inspectionNoteView);
            inspectionNoteAndItems.setInspectionNoteItems(new ArrayList<>());
            result.add(inspectionNoteAndItems);
            for(InspectionNoteItemView itemView : itemViews){
                if(itemView.getInspectionNoteId() == inspectionNoteView.getId()){
                    inspectionNoteAndItems.getInspectionNoteItems().add(itemView);
                }
            }
        }
        return result;
    }
}
