package com.wms.services.warehouse.service;

import com.wms.services.ledger.service.PersonService;
import com.wms.services.warehouse.dao.WarehouseEntryDAO;
import com.wms.services.warehouse.datastructures.InspectArgs;
import com.wms.services.warehouse.datastructures.InspectItem;
import com.wms.services.warehouse.datastructures.WarehouseEntryAndItems;
import com.wms.utilities.IDChecker;
import com.wms.utilities.OrderNoGenerator;
import com.wms.utilities.ReflectHelper;
import com.wms.utilities.model.*;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class WarehouseEntryServiceImpl implements WarehouseEntryService {
    @Autowired
    WarehouseEntryDAO warehouseEntryDAO;
    @Autowired
    OrderNoGenerator orderNoGenerator;
    @Autowired
    PersonService personService;
    @Autowired
    SupplierServices supplierService;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    WarehouseEntryItemService warehouseEntryItemService;
    @Autowired
    IDChecker idChecker;
    @Autowired
    InspectionNoteService inspectionNoteService;
    @Autowired
    InspectionNoteItemService inspectionNoteItemService;

    private static final String NO_PREFIX = "R";

    @Override
    public int[] add(String accountBook, WarehouseEntry[] warehouseEntries) throws WMSServiceException {
        //数据验证
        Stream.of(warehouseEntries).forEach(
                (warehouseEntry) -> {
                    new Validator("状态").min(0).max(5).validate(warehouseEntry.getState());
                    new Validator("创建用户").notnull().validate(warehouseEntry.getCreatePersonId());
                }
        );

        //外键检测
        Stream.of(warehouseEntries).forEach(
                (warehouseEntry) -> {
                    if (this.warehouseService.find(accountBook,
                            new Condition().addCondition("id", warehouseEntry.getWarehouseId())).length == 0) {
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)", warehouseEntry.getWarehouseId()));
                    } else if (this.supplierService.find(accountBook,
                            new Condition().addCondition("id", warehouseEntry.getSupplierId())).length == 0) {
                        throw new WMSServiceException(String.format("供应商不存在，请重新提交！(%d)", warehouseEntry.getSupplierId()));
                    } else if (this.personService.find(accountBook,
                            new Condition().addCondition("id", warehouseEntry.getCreatePersonId())).length == 0) {
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)", warehouseEntry.getCreatePersonId()));
                    }
                    if (warehouseEntry.getLastUpdatePersonId() != null && this.personService.find(accountBook,
                            new Condition().addCondition("id", warehouseEntry.getLastUpdatePersonId())).length == 0) {
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)", warehouseEntry.getLastUpdatePersonId()));
                    }
                }
        );

        //生成创建时间
        Stream.of(warehouseEntries).forEach((w) -> w.setCreateTime(new java.sql.Timestamp(System.currentTimeMillis())));

        //生成/检测单号
        Stream.of(warehouseEntries).forEach((warehouseEntry) -> {
            //如果单号留空则自动生成
            if (warehouseEntry.getNo() == null) {
                warehouseEntry.setNo(this.orderNoGenerator.generateNextNo(accountBook, WarehouseEntryServiceImpl.NO_PREFIX));
            } else { //否则检查单号是否重复
                Condition cond = new Condition();
                cond.addCondition("no", new String[]{warehouseEntry.getNo()});
                if (warehouseEntryDAO.find(accountBook, cond).length > 0) {
                    throw new WMSServiceException("入库单单号重复：" + warehouseEntry.getNo());
                }
            }
        });

        return warehouseEntryDAO.add(accountBook, warehouseEntries);
    }

    @Override
    public void update(String accountBook, WarehouseEntry[] warehouseEntries) throws WMSServiceException {
        //数据验证
        Stream.of(warehouseEntries).forEach((warehouseEntry) -> {
            new Validator("状态").min(0).max(5).validate(warehouseEntry.getState());
        });

        //编号查重
        for (int i = 0; i < warehouseEntries.length; i++) {
            Condition cond = new Condition();
            cond.addCondition("no", new String[]{warehouseEntries[i].getNo()});
            cond.addCondition("id", new Integer[]{warehouseEntries[i].getId()}, ConditionItem.Relation.NOT_EQUAL);
            if (warehouseEntryDAO.find(accountBook, cond).length > 0) {
                throw new WMSServiceException("入库单单号重复：" + warehouseEntries[i].getNo());
            }
        }

        Stream.of(warehouseEntries).forEach((warehouseEntry -> {
            warehouseEntry.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
        }));

        warehouseEntryDAO.update(accountBook, warehouseEntries);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        for (int id : ids) {
            if (warehouseEntryDAO.find(accountBook, new Condition().addCondition("id", id)).length == 0) {
                throw new WMSServiceException(String.format("删除入库单不存在，请重新查询！(%d)", id));
            }
        }

        try {
            warehouseEntryDAO.remove(accountBook, ids);
        } catch (Throwable ex) {
            throw new WMSServiceException("删除入库单失败，如果入库单已经被引用，需要先删除引用该入库单的内容，才能删除该入库单");
        }
    }

    @Override
    public WarehouseEntryView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return warehouseEntryDAO.find(accountBook, cond);
    }

    @Override
    public List<Integer> inspect(String accountBook, InspectArgs inspectArgs) {
        List<Integer> inspectionNoteIDs = new ArrayList<>();
        List<InspectionNoteItem> inspectionNoteItemsToAdd = new ArrayList<>();
        List<WarehouseEntry> warehouseEntriesToUpdate = new ArrayList<>();
        InspectItem[] inspectItems = inspectArgs.getInspectItems();
        Stream.of(inspectItems).forEach((inspectItem) -> {
            int warehouseEntryID = inspectItem.getInspectionNote().getWarehouseEntryId();
            WarehouseEntry warehouseEntry = this.warehouseEntryDAO.get(accountBook, warehouseEntryID);
            if (warehouseEntry.getState() != WAIT_FOR_PUT_IN_STORAGE) {
                throw new WMSServiceException("入库单 " + warehouseEntry.getNo() + " 已送检/入库，请不要重复操作！");
            }
            warehouseEntry.setState(WarehouseEntryService.BEING_INSPECTED);
            warehouseEntriesToUpdate.add(warehouseEntry);
            //创建新的送检单
            InspectionNote inspectionNote = inspectItem.getInspectionNote();
            new Validator("送检单信息").notnull().validate(inspectionNote);
            //将每一条入库单条目生成送检单条目
            int newInspectionNoteID = this.inspectionNoteService.add(accountBook, new InspectionNote[]{inspectionNote})[0];
            Stream.of(inspectItem.getInspectionNoteItems()).forEach(inspectionNoteItem -> {
                inspectionNoteItem.setInspectionNoteId(newInspectionNoteID);
                inspectionNoteItemsToAdd.add(inspectionNoteItem);
            });
            inspectionNoteIDs.add(newInspectionNoteID);
        });
        this.inspectionNoteItemService.add(accountBook, ReflectHelper.listToArray(inspectionNoteItemsToAdd,InspectionNoteItem.class));
        this.update(accountBook, ReflectHelper.listToArray(warehouseEntriesToUpdate,WarehouseEntry.class));
        return inspectionNoteIDs;
    }

    @Override
    public long findCount(String accountBook, Condition cond) throws WMSServiceException {
        return this.warehouseEntryDAO.findCount(accountBook, cond);
    }

    @Override
    public void updateState(String accountBook, List<Integer> ids) {
        List<WarehouseEntry> warehouseEntriesToUpdate = new ArrayList<>();
        for (int id : ids) {
            //this.idChecker.check(WarehouseEntryService.class, accountBook, id, "入库单");
            WarehouseEntry warehouseEntry = this.warehouseEntryDAO.get(accountBook, id);
            warehouseEntriesToUpdate.add(warehouseEntry);
            WarehouseEntryItemView[] warehouseEntryItemViews = this.warehouseEntryItemService.find(accountBook, new Condition().addCondition("warehouseEntryId", id));
            long total = warehouseEntryItemViews.length;
            long waitForPutInCount = Stream.of(warehouseEntryItemViews).filter((item) -> item.getState() == WarehouseEntryItemService.WAIT_FOR_PUT_IN_STORAGE).count();
            long beingInspectedCount = Stream.of(warehouseEntryItemViews).filter((item) -> item.getState() == WarehouseEntryItemService.BEING_INSPECTED).count();
            long qualifiedCount = Stream.of(warehouseEntryItemViews).filter((item) -> item.getState() == WarehouseEntryItemService.QUALIFIED).count();
            long unqualifiedCount = Stream.of(warehouseEntryItemViews).filter((item) -> item.getState() == WarehouseEntryItemService.UNQUALIFIED).count();
            if (waitForPutInCount == total) {
                warehouseEntry.setState(WAIT_FOR_PUT_IN_STORAGE);
            } else if (qualifiedCount + unqualifiedCount == total) {
                warehouseEntry.setState(ALL_PUT_IN_STORAGE);
            } else if (qualifiedCount > 0 || unqualifiedCount > 0) {
                warehouseEntry.setState(PART_PUT_IN_STORAGE);
            } else if (beingInspectedCount > 0) {
                warehouseEntry.setState(BEING_INSPECTED);
            }
        }
        this.update(accountBook, ReflectHelper.listToArray(warehouseEntriesToUpdate, WarehouseEntry.class));
    }

    @Override
    public WarehouseEntry get(String accountBook,int id){
        return this.warehouseEntryDAO.get(accountBook,id);
    }


    @Override
    public List<WarehouseEntryAndItems> getPreviewData(String accountBook, List<Integer> warehouseEntryIDs) throws WMSServiceException{
        WarehouseEntryView[] warehouseEntryViews = this.warehouseEntryDAO.find(accountBook,new Condition().addCondition("id",warehouseEntryIDs.toArray(), ConditionItem.Relation.IN));
        WarehouseEntryItemView[] itemViews = this.warehouseEntryItemService.find(accountBook,new Condition().addCondition("warehouseEntryId",warehouseEntryIDs.toArray(), ConditionItem.Relation.IN));
        List<WarehouseEntryAndItems> result = new ArrayList<>();
        for(WarehouseEntryView warehouseEntryView : warehouseEntryViews){
            WarehouseEntryAndItems warehouseEntryAndItems = new WarehouseEntryAndItems();
            warehouseEntryAndItems.setWarehouseEntry(warehouseEntryView);
            warehouseEntryAndItems.setWarehouseEntryItems(new ArrayList<>());
            result.add(warehouseEntryAndItems);
            for(WarehouseEntryItemView itemView : itemViews){
                if(itemView.getWarehouseEntryId() == warehouseEntryView.getId()){
                    warehouseEntryAndItems.getWarehouseEntryItems().add(itemView);
                }
            }
        }
        return result;
    }

    @Override
    public void receive(String accountBook,List<Integer> ids) throws WMSServiceException{
        this.putIn(accountBook,ids,true);
    }

    @Override
    public void reject(String accountBook,List<Integer> ids) throws WMSServiceException{
        this.putIn(accountBook,ids,false);
    }

    private void putIn(String accountBook,List<Integer> ids,boolean ifQualified) {
        if (ids.size() == 0) {
            throw new WMSServiceException("请选择至少一个入库单！");
        }
        WarehouseEntry[] warehouseEntries = this.warehouseEntryDAO.findTable(accountBook, new Condition().addCondition("id", ids.toArray(), ConditionItem.Relation.IN));
        Stream.of(warehouseEntries).forEach(warehouseEntry -> {
            if (warehouseEntry.getState() != WAIT_FOR_PUT_IN_STORAGE) {
                throw new WMSServiceException("入库单：" + warehouseEntry.getNo() + " 已经送检/入库，请勿重复操作！");
            }
            warehouseEntry.setState(ALL_PUT_IN_STORAGE);
        });
        //更新入库单
        this.update(accountBook, warehouseEntries);
        WarehouseEntryItemView[] warehouseEntryItemViews = this.warehouseEntryItemService.find(accountBook, new Condition().addCondition("warehouseEntryId", ids.toArray(), ConditionItem.Relation.IN));
        if (warehouseEntryItemViews.length == 0) return;
        List<Integer> itemIDs = Stream.of(warehouseEntryItemViews).map(item -> item.getId()).collect(Collectors.toList());
        //更新入库单条目
        if (ifQualified) {
            this.warehouseEntryItemService.receive(accountBook, itemIDs);
        } else {
            this.warehouseEntryItemService.reject(accountBook, itemIDs);
        }
    }
}
