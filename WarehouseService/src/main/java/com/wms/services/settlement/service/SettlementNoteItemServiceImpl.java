package com.wms.services.settlement.service;

import com.wms.services.settlement.dao.SettlementNoteItemDAO;
import com.wms.services.warehouse.service.SupplierServices;
import com.wms.services.warehouse.service.WarehouseService;
import com.wms.utilities.IDChecker;
import com.wms.utilities.ReflectHelper;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.SettlementNote;
import com.wms.utilities.model.SettlementNoteItem;
import com.wms.utilities.model.SettlementNoteItemView;
import com.wms.utilities.model.SettlementNoteView;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional
public class SettlementNoteItemServiceImpl
    implements SettlementNoteItemService{
    @Autowired
    SettlementNoteItemDAO settlementNoteItemDAO;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    IDChecker idChecker;
    @Autowired
    SettlementNoteService settlementNoteService;

    @Override
    public int[] add(String accountBook, SettlementNoteItem[] settlementNoteItems) throws WMSServiceException
    {
        this.validateEntities(accountBook,settlementNoteItems);
        for(SettlementNoteItem settlementNoteItem:settlementNoteItems){
            settlementNoteItem.setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        return settlementNoteItemDAO.add(accountBook,settlementNoteItems);
    }

    @Override
    public void update(String accountBook, SettlementNoteItem[] settlementNoteItems) throws WMSServiceException
    {
        this.validateEntities(accountBook,settlementNoteItems);
        settlementNoteItemDAO.update(accountBook, settlementNoteItems);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException{

        try {
            for (int id : ids) {
                if (settlementNoteItemDAO.find(accountBook, new Condition().addCondition("id", new Integer[]{id})).length == 0) {
                    throw new WMSServiceException(String.format("删除结算单条目不存在，请重新查询！(%d)", id));
                }
            }

            settlementNoteItemDAO.remove(accountBook, ids);
        }
        catch (Throwable ex){
            throw new WMSServiceException("删除结算单条目信息失败，如果结算单条目信息已经被引用，需要先删除引用的内容，才能删除该结算单条目");
        }
    }

    @Override
    public SettlementNoteItemView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.settlementNoteItemDAO.find(accountBook, cond);
    }

    private void validateEntities(String accountBook,SettlementNoteItem[] settlementNoteItems) throws WMSServiceException{
        Stream.of(settlementNoteItems).forEach((settlementNoteItem -> {
            this.idChecker.check(SupplierServices.class, accountBook, settlementNoteItem.getSupplierId(), "供货商ID");
            this.idChecker.check(SettlementNoteService.class, accountBook, settlementNoteItem.getSettlementNoteId(), "结算单ID");

            new Validator("状态").min(0).max(1).validate(settlementNoteItem.getState());
            new Validator("仓储费").notEmpty().validate(settlementNoteItem.getStorageCharge());
            new Validator("物流费").notEmpty().validate(settlementNoteItem.getLogisticFee());
            new Validator("实付金额").notEmpty().validate(settlementNoteItem.getActualPayment());

            BigDecimal sumFee=settlementNoteItem.getStorageCharge().add(settlementNoteItem.getLogisticFee());
            if (settlementNoteItem.getActualPayment().compareTo(sumFee)>0){
                throw new WMSServiceException("实付金额不能大于仓储费和物流费用之和!");
            }
        }));
    }

    @Override
    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.settlementNoteItemDAO.findCount(database,cond);
    }

    @Override
    public void confirm(String accountBook,List<Integer> ids) throws WMSServiceException{

        SettlementNoteItemView[] settlementNoteItemViews= this.find(accountBook,new Condition().addCondition("id",ids.toArray(), ConditionItem.Relation.IN));
        if (settlementNoteItemViews.length == 0) return;
        SettlementNoteItem[] settlementNoteItems = ReflectHelper.createAndCopyFields(settlementNoteItemViews,SettlementNoteItem.class);

        Stream.of(settlementNoteItems).forEach(settlementNoteItem -> {
            settlementNoteItem.setState(SettlementNoteItemService.Confirmed);
        });

        this.update(accountBook,settlementNoteItems);
    }

//    public void updateSettlementNote( String accountBook,int SettlementNoteId){
//        SettlementNoteView[] settlementNoteViews= settlementNoteService.find(accountBook,new Condition().addCondition("id",new Integer[]{SettlementNoteId}));
//        SettlementNote[] settlementNotes = ReflectHelper.createAndCopyFields(settlementNoteViews,SettlementNote.class);
//        if(settlementNotes.length==0){
//            throw new WMSServiceException("没有找到要更新的结算单！");
//        }
//        SettlementNote settlementNote=settlementNotes[0];
//        SettlementNoteItem[] allItems = this.settlementNoteItemDAO.findTable(accountBook,new Condition().addCondition("settlementNoteId",settlementNote.getId(), ConditionItem.Relation.IN));
//
//        SettlementNoteItem[] allFinishItems = this.settlementNoteItemDAO.findTable(accountBook,new Condition()
//                .addCondition("settlementNoteId",settlementNote.getId(), ConditionItem.Relation.IN)
//                .addCondition("state",SettlementNoteItemService.Confirmed, ConditionItem.Relation.IN));
//
//        SettlementNoteItem[] zeroItems = this.settlementNoteItemDAO.findTable(accountBook,new Condition()
//                .addCondition("settlementNoteId",settlementNote.getId(), ConditionItem.Relation.IN).
//                        addCondition("state",SettlementNoteItemService.To_be_confirmed, ConditionItem.Relation.IN));
//
//        if (allItems.length>0&&allFinishItems.length==allItems.length){
//            settlementNote.setState(SettlementNoteService.To_be_confirmed);
//        }else if (allItems.length==0||zeroItems.length==allItems.length){
//            settlementNote.setState(SettlementNoteItemService.To_be_confirmed);
//        }
//        else{
//            settlementNote.setState(SettlementNoteItemService.To_be_confirmed);
//        }
//        settlementNoteService.update(accountBook,new SettlementNote[]{settlementNote});
//    }
}
