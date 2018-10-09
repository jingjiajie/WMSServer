package com.wms.services.settlement.service;

import com.wms.services.ledger.service.AccountRecordService;
import com.wms.services.ledger.service.AccountTitleService;
import com.wms.services.settlement.dao.SettlementNoteDAO;
import com.wms.services.settlement.datastructures.LedgerSynchronous;
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

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


@Service
@Transactional
public class SettlementNoteServiceImpl implements SettlementNoteService {
    @Autowired
    SettlementNoteDAO settlementNoteDAO;
    @Autowired
    AccountTitleService accountTitleService;
    @Autowired
    SettlementNoteItemService settlementNoteItemService;
    @Autowired
    AccountRecordService accountRecordService;
    @Autowired
    IDChecker idChecker;
    @Autowired
    OrderNoGenerator orderNoGenerator;
    @Autowired
    SummaryNoteItemService summaryNoteItemService;
    @Autowired
    SummaryDetailsService summaryDetailsService;
    @Autowired
    PriceService priceService;

    private static final String NO_PREFIX = "S";

    @Override
    public int[] add(String accountBook, SettlementNote[] settlementNotes) throws WMSServiceException
    {

        //生成/检测单号
        Stream.of(settlementNotes).forEach((settlementNote) -> {
            //如果单号留空则自动生成
            if (settlementNote.getNo() == null) {
                settlementNote.setNo(this.orderNoGenerator.generateNextNo(accountBook, SettlementNoteServiceImpl.NO_PREFIX,settlementNote.getWarehouseId()));
            }
        });
        this.validateEntities(accountBook,settlementNotes);

        int[] ids=settlementNoteDAO.add(accountBook,settlementNotes);

        List<SettlementNoteItem> settlementNoteItemList=new ArrayList();

        for(int j=0;j<ids.length;j++){
            int summaryNoteId=settlementNotes[j].getSummaryNoteId();
            int settlementNoteId=ids[j];
            SummaryNoteItemView[] summaryNoteItemViews=this.summaryNoteItemService.find(accountBook,new Condition().addCondition("summaryNoteId",summaryNoteId));

            Stream.of(summaryNoteItemViews).forEach((summaryNoteItemView) -> {
                SettlementNoteItem settlementNoteItem=new SettlementNoteItem();
                settlementNoteItem.setState(0);
                settlementNoteItem.setSupplierId(summaryNoteItemView.getSupplierId());
                settlementNoteItem.setSettlementNoteId(settlementNoteId);

                BigDecimal thAreaPrice=BigDecimal.ZERO;
                BigDecimal thLogisticFee=BigDecimal.ZERO;

                SummaryDetailsView[] summaryDetailsViews=this.summaryDetailsService.find(accountBook,new Condition().addCondition("summaryNoteItemId",summaryNoteItemView.getId()));
                for (int i=0;i<summaryDetailsViews.length;i++){
                    PriceView[] priceViews=this.priceService.find(accountBook,new Condition().addCondition("supplyId",summaryDetailsViews[i].getSupplyId()));
                    if (priceViews.length==0){
                        throw new WMSServiceException(String.format("供货商：（%s），物料：（%s），价格不存在！", summaryDetailsViews[i].getSupplierName(),summaryDetailsViews[i].getMaterialName()));
                    }
                    PriceView priceView=priceViews[0];
                    BigDecimal areaPrice=priceView.getAreaUnitPrice().multiply(summaryDetailsViews[i].getArea()).multiply(summaryNoteItemView.getDays());
                    BigDecimal logisticFee=BigDecimal.ZERO;
                    if (summaryDetailsViews[i].getDeliveryAmount().compareTo(priceView.getLogisticsThreshold1())<0){
                        logisticFee=priceView.getLogisticsUnitPrice1().multiply(summaryDetailsViews[i].getDeliveryAmount());
                    }else if (priceView.getLogisticsThreshold2()==null){
                        logisticFee=priceView.getLogisticsUnitPrice2().multiply(summaryDetailsViews[i].getDeliveryAmount());
                    }else if (summaryDetailsViews[i].getDeliveryAmount().compareTo(priceView.getLogisticsThreshold2())<0){
                        logisticFee=priceView.getLogisticsUnitPrice2().multiply(summaryDetailsViews[i].getDeliveryAmount());
                    }else if (priceView.getLogisticsThreshold3()==null){
                        logisticFee=priceView.getLogisticsUnitPrice3().multiply(summaryDetailsViews[i].getDeliveryAmount());
                    }else if (summaryDetailsViews[i].getDeliveryAmount().compareTo(priceView.getLogisticsThreshold3())<0){
                        logisticFee=priceView.getLogisticsUnitPrice3().multiply(summaryDetailsViews[i].getDeliveryAmount());
                    }
                    thAreaPrice=thAreaPrice.add(areaPrice);
                    thLogisticFee=thLogisticFee.add(logisticFee);
                }
                settlementNoteItem.setLogisticFee(thLogisticFee);
                settlementNoteItem.setStorageCharge(thAreaPrice);
                settlementNoteItem.setActualPayment(BigDecimal.ZERO);
                settlementNoteItemList.add(settlementNoteItem);
            });
        }
        SettlementNoteItem[] settlementNoteItems=new SettlementNoteItem[settlementNoteItemList.size()];
        settlementNoteItemList.toArray(settlementNoteItems);

        this.settlementNoteItemService.add(accountBook,settlementNoteItems);
        return ids;
    }

    @Override
    public void update(String accountBook, SettlementNote[] settlementNotes) throws WMSServiceException
    {
        this.validateEntities(accountBook,settlementNotes);
        settlementNoteDAO.update(accountBook, settlementNotes);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException{

        try {
            for (int id : ids) {
                if (settlementNoteDAO.find(accountBook, new Condition().addCondition("id", new Integer[]{id})).length == 0) {
                    throw new WMSServiceException(String.format("删除结算单不存在，请重新查询！(%d)", id));
                }
            }

            settlementNoteDAO.remove(accountBook, ids);
        }
        catch (Throwable ex){
            throw new WMSServiceException("删除结算单信息失败，如果结算单信息已经被引用，需要先删除引用的内容，才能删除该汇总单");
        }
    }

    @Override
    public SettlementNoteView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.settlementNoteDAO.find(accountBook, cond);
    }

    private void validateEntities(String accountBook,SettlementNote[] settlementNotes) throws WMSServiceException{
        Stream.of(settlementNotes).forEach((settlementNote -> {
            new Validator("状态").min(0).max(2).validate(settlementNote.getState());
            new Validator("单号").notEmpty().validate(settlementNote.getNo());

            this.idChecker.check(AccountTitleService.class, accountBook, settlementNote.getAccountTitleIncomeId(), "收入科目ID");
            this.idChecker.check(AccountTitleService.class, accountBook, settlementNote.getAccountTitleReceivableId(), "应收款科目ID");
            this.idChecker.check(AccountTitleService.class, accountBook, settlementNote.getAccountTitlePropertyId(), "资产科目ID");
            this.idChecker.check(SummaryNoteService.class, accountBook, settlementNote.getSummaryNoteId(), "汇总单ID");

        }));
    }

    @Override
    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.settlementNoteDAO.findCount(database,cond);
    }

    @Override
    public void synchronousReceivables(String accountBook,LedgerSynchronous ledgerSynchronous) throws WMSServiceException{

        SettlementNoteView[] settlementNoteViews =this.find(accountBook,new Condition().addCondition("id",ledgerSynchronous.getSettlementNoteIds().toArray(),ConditionItem.Relation.IN));
        SettlementNote[] settlementNotes = ReflectHelper.createAndCopyFields(settlementNoteViews,SettlementNote.class);
        List<AccountRecord> accountRecordList=new ArrayList();

        Stream.of(settlementNoteViews).forEach(settlementNoteView -> {

            SettlementNoteItemView[] settlementNoteItemViews= this.settlementNoteItemService.find(accountBook,new Condition().addCondition("settlementNoteId",settlementNoteView.getId()));
            if (settlementNoteItemViews.length == 0) return;


            Stream.of(settlementNoteItemViews).forEach(settlementNoteItemView -> {
                if (settlementNoteItemView.getState()!=SettlementNoteItemService.Confirmed){
                    throw new WMSServiceException("结算单同步到总账应收款失败，结算单条目需要供货商全部确认，才能同步到总账应收款！");
                }
            });

            Stream.of(settlementNoteItemViews).forEach(settlementNoteItemView -> {

                AccountRecord accountRecord=new AccountRecord();
                accountRecord.setAccountTitleId(settlementNoteView.getAccountTitleIncomeId());
                accountRecord.setAccountPeriodId(ledgerSynchronous.getAccountPeriodId());
                accountRecord.setPersonId(ledgerSynchronous.getPersonId());
                accountRecord.setComment("结算单应收同步");
                accountRecord.setWarehouseId(settlementNoteView.getWarehouseId());
                accountRecord.setCreditAmount(settlementNoteItemView.getStorageCharge().add(settlementNoteItemView.getLogisticFee()));
                accountRecord.setDebitAmount(BigDecimal.ZERO);
                accountRecord.setBalance(BigDecimal.ZERO);
                accountRecord.setSummary(settlementNoteItemView.getSupplierName());

                AccountRecord accountRecord1=new AccountRecord();
                accountRecord1.setAccountTitleId(settlementNoteView.getAccountTitleReceivableId());
                accountRecord1.setAccountPeriodId(ledgerSynchronous.getAccountPeriodId());
                accountRecord1.setPersonId(ledgerSynchronous.getPersonId());
                accountRecord1.setComment("结算单应收同步");
                accountRecord1.setWarehouseId(settlementNoteView.getWarehouseId());
                accountRecord1.setDebitAmount(settlementNoteItemView.getStorageCharge().add(settlementNoteItemView.getLogisticFee()));
                accountRecord1.setCreditAmount(BigDecimal.ZERO);
                accountRecord1.setBalance(BigDecimal.ZERO);
                accountRecord1.setSummary(settlementNoteItemView.getSupplierName());

                accountRecordList.add(accountRecord);
                accountRecordList.add(accountRecord1);

            });

        });

        AccountRecord[] accountRecords = (AccountRecord[]) Array.newInstance(AccountRecord.class,accountRecordList.size());
        accountRecordList.toArray(accountRecords);

        this.accountRecordService.add(accountBook,accountRecords);
        Stream.of(settlementNotes).forEach(settlementNote -> {
            settlementNote.setState(SettlementNoteService.Synchronous_receivables);
        });
        this.update(accountBook,settlementNotes);
    }

    @Override
    public void synchronousReceipt(String accountBook,LedgerSynchronous ledgerSynchronous) throws WMSServiceException{

        SettlementNoteView[] settlementNoteViews =this.find(accountBook,new Condition().addCondition("id",ledgerSynchronous.getSettlementNoteIds().toArray(),ConditionItem.Relation.IN));
        SettlementNote[] settlementNotes = ReflectHelper.createAndCopyFields(settlementNoteViews,SettlementNote.class);
        List<AccountRecord> accountRecordList=new ArrayList();

        Stream.of(settlementNoteViews).forEach(settlementNoteView -> {

            SettlementNoteItemView[] settlementNoteItemViews= this.settlementNoteItemService.find(accountBook,new Condition().addCondition("settlementNoteId",settlementNoteView.getId()));
            if (settlementNoteItemViews.length == 0) return;


            Stream.of(settlementNoteItemViews).forEach(settlementNoteItemView -> {
                BigDecimal sumFee=settlementNoteItemView.getStorageCharge().add(settlementNoteItemView.getLogisticFee());
                if (settlementNoteItemView.getActualPayment().compareTo(sumFee)!=0){
                    throw new WMSServiceException("结算单同步到总账实收款失败，结算单条目实收款未全部实付！");
                }
            });

            Stream.of(settlementNoteItemViews).forEach(settlementNoteItemView -> {

                AccountRecord accountRecord=new AccountRecord();
                accountRecord.setAccountTitleId(settlementNoteView.getAccountTitleReceivableId());
                accountRecord.setAccountPeriodId(ledgerSynchronous.getAccountPeriodId());
                accountRecord.setPersonId(ledgerSynchronous.getPersonId());
                accountRecord.setComment("结算单实收款同步");
                accountRecord.setWarehouseId(settlementNoteView.getWarehouseId());
                accountRecord.setCreditAmount(settlementNoteItemView.getActualPayment());
                accountRecord.setDebitAmount(BigDecimal.ZERO);
                accountRecord.setBalance(BigDecimal.ZERO);
                accountRecord.setSummary(settlementNoteItemView.getSupplierName());

                AccountRecord accountRecord1=new AccountRecord();
                accountRecord1.setAccountTitleId(settlementNoteView.getAccountTitlePropertyId());
                accountRecord1.setAccountPeriodId(ledgerSynchronous.getAccountPeriodId());
                accountRecord1.setPersonId(ledgerSynchronous.getPersonId());
                accountRecord1.setComment("结算单实收款同步");
                accountRecord1.setWarehouseId(settlementNoteView.getWarehouseId());
                accountRecord1.setDebitAmount(settlementNoteItemView.getActualPayment());
                accountRecord1.setCreditAmount(BigDecimal.ZERO);
                accountRecord1.setBalance(BigDecimal.ZERO);
                accountRecord1.setSummary(settlementNoteItemView.getSupplierName());

                accountRecordList.add(accountRecord);
                accountRecordList.add(accountRecord1);

            });

        });

        AccountRecord[] accountRecords = (AccountRecord[]) Array.newInstance(AccountRecord.class,accountRecordList.size());
        accountRecordList.toArray(accountRecords);

        this.accountRecordService.add(accountBook,accountRecords);
        Stream.of(settlementNotes).forEach(settlementNote -> {
            settlementNote.setState(SettlementNoteService.Synchronous_receipt);
        });
        this.update(accountBook,settlementNotes);

    }
}
