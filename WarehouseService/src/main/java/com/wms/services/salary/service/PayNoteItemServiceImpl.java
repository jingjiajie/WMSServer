package com.wms.services.salary.service;

import com.wms.services.ledger.datestructures.TaxCalculation;
import com.wms.services.ledger.service.AccountTitleService;
import com.wms.services.ledger.service.PersonService;
import com.wms.services.ledger.service.TaxService;
import com.wms.services.salary.dao.PayNoteItemDAO;
import com.wms.services.salary.datestructures.*;
import com.wms.services.warehouse.service.WarehouseService;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
@Service
public class PayNoteItemServiceImpl implements PayNoteItemService {
    @Autowired
    PayNoteItemDAO payNoteItemDAO;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    PersonService personService;
    @Autowired
    PayNoteService payNoteService;
    @Autowired
    TaxService taxService;
    @Autowired
    AccountTitleService accountTitleService;
    @Autowired
    PersonSalaryService personSalaryService;
    @Autowired
    SalaryItemService salaryItemService;


    public int[] add(String accountBook, PayNoteItem[] payNoteItems) throws WMSServiceException {
        for (int i = 0; i < payNoteItems.length; i++) {
            Validator validator1 = new Validator("税前应付");
            validator1.notnull().notEmpty().min(0).validate(payNoteItems[i].getPreTaxAmount());
            payNoteItems[i].setState(PayNoteItemState.WAITING_FOR_CALCULATE_PAY);
            payNoteItems[i].setAfterTaxAmount(BigDecimal.ZERO);
            payNoteItems[i].setTaxAmount(BigDecimal.ZERO);
            payNoteItems[i].setPaidAmount(BigDecimal.ZERO);
        }
        //外键检测
        Stream.of(payNoteItems).forEach(
                (payNoteItem) -> {
                    if (this.payNoteService.find(accountBook,
                            new Condition().addCondition("id", new Integer[]{payNoteItem.getPayNoteId()})).length == 0) {
                        throw new WMSServiceException(String.format("薪资发放单不存在，请重新提交！(%d)", payNoteItem.getPayNoteId()));
                    }

                    if (this.personService.find(accountBook,
                            new Condition().addCondition("id", new Integer[]{payNoteItem.getPersonId()})).length == 0) {
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)", payNoteItem.getPersonId()));
                    }
                }
        );
        //将人员薪资的总数找出来填到税前应付
        //payNoteItems=this.getPersonAmount(accountBook,payNoteItems);
        return payNoteItemDAO.add(accountBook, payNoteItems);
    }

    @Transactional
    public void update(String accountBook, PayNoteItem[] payNoteItems) throws WMSServiceException {

        for (int i = 0; i < payNoteItems.length; i++) {
            Validator validator1 = new Validator("税前应付");
            validator1.notnull().notEmpty().min(0).validate(payNoteItems[i].getPreTaxAmount());
            Validator validator = new Validator("税费");
            validator.notnull().notEmpty().min(0).validate(payNoteItems[i].getTaxAmount());
            Validator validator2 = new Validator("税后应付");
            validator2.notnull().notEmpty().min(0).validate(payNoteItems[i].getAfterTaxAmount());
            Validator validator3 = new Validator("实付金额");
            validator3.notnull().notEmpty().min(0).validate(payNoteItems[i].getPaidAmount());
        }
        //对于已计算税费的条目应该保持税后应付=税前-税费
        for (int i = 0; i < payNoteItems.length; i++) {
            if (payNoteItems[i].getState() != PayNoteItemState.WAITING_FOR_CALCULATE_PAY) {
                if (payNoteItems[i].getPreTaxAmount().subtract(payNoteItems[i].getTaxAmount()).compareTo(payNoteItems[i].getAfterTaxAmount()) != 0) {
                    throw new WMSServiceException("条目应满足税后应付=税前-税费！");
                }
            }
        }
        //外键检测
        Stream.of(payNoteItems).forEach(
                (payNoteItem) -> {
                    if (this.payNoteService.find(accountBook,
                            new Condition().addCondition("id", new Integer[]{payNoteItem.getPayNoteId()})).length == 0) {
                        throw new WMSServiceException(String.format("薪资发放单不存在，请重新提交！(%d)", payNoteItem.getPayNoteId()));
                    }

                    if (this.personService.find(accountBook,
                            new Condition().addCondition("id", new Integer[]{payNoteItem.getPersonId()})).length == 0) {
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)", payNoteItem.getPersonId()));
                    }
                }
        );
        //this.getPersonAmount(accountBook,payNoteItems);
        payNoteItemDAO.update(accountBook, payNoteItems);
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        for (int id : ids) {
            if (payNoteItemDAO.find(accountBook, new Condition().addCondition("id", id)).length == 0) {
                throw new WMSServiceException(String.format("删除项目不存在，请重新查询！(%d)", id));
            }
        }
        try {
            payNoteItemDAO.remove(accountBook, ids);
        } catch (Exception e) {
            throw new WMSServiceException("删除薪金发放单条目失败，如果薪金发放单条目已经被引用，需要先删除引用的内容，才能删除薪金发放单条目！");
        }
    }

    public PayNoteItemView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.payNoteItemDAO.find(accountBook, cond);
    }

    public PayNoteItem[] findTable(String accountBook, Condition cond) throws WMSServiceException {
        return this.payNoteItemDAO.findTable(accountBook, cond);
    }

    public long findCount(String database, Condition cond) throws WMSServiceException {
        return this.payNoteItemDAO.findCount(database, cond);
    }

    //按条目或者整单计算税费 如果不提供条目id则默认按整单操作
    public void calculateTax(String accountBook, CalculateTax calculateTax) {
        int payNoteId = calculateTax.getPayNoteId();
        BigDecimal[] taxAmount = null;
        java.util.List<Integer> payNoteItemId = calculateTax.getPayNoteItemId();
        PayNoteItem[] payNoteItems = null;
        PayNoteItemView[] payNoteItemViews = null;
        //PayNoteView[] payNoteViews=payNoteService.find(accountBook,new Condition().addCondition("id",payNoteId));
        int taxId = calculateTax.getTaxId();
        if (payNoteItemId != null) {
            payNoteItemViews = payNoteItemDAO.find(accountBook, new Condition().addCondition("id", payNoteItemId.toArray(), ConditionItem.Relation.IN));
            if (payNoteItemViews.length != payNoteItemId.size()) {
                throw new WMSServiceException("查询薪资发放单条目出错,某些条目已经不存在！");
            }
        } else {
            payNoteItemViews = payNoteItemDAO.find(accountBook, new Condition().addCondition("payNoteId", payNoteId));
        }
        if (payNoteItemViews.length == 0) {
            throw new WMSServiceException("薪资发放单中无条目！");
        }
        List<Integer> state = new ArrayList<>();
        state.add(PayNoteItemState.WAITING_FOR_CALCULATE_PAY);
        state.add(PayNoteItemState.PAYED);
        payNoteItems = this.getStateItem(payNoteItemViews, state);
        //计算税费
        TaxCalculation taxCalculation = new TaxCalculation();
        for (int i = 0; i < payNoteItems.length; i++) {
            taxCalculation.setMoneyAmount(payNoteItems[i].getPreTaxAmount());
            taxCalculation.setTaxId(taxId);
            BigDecimal tax = taxService.taxCalculation(accountBook, taxCalculation);
            payNoteItems[i].setTaxAmount(tax);
            payNoteItems[i].setAfterTaxAmount(payNoteItems[i].getPreTaxAmount().subtract(tax));
            payNoteItems[i].setState(PayNoteItemState.PAYED);
        }
        payNoteItemDAO.update(accountBook, payNoteItems);
    }

    public boolean judgeAllFinish(String accountBook, int state, int payNoteId) {
        PayNoteItem[] payNoteItems = payNoteItemDAO.findTable(accountBook, new Condition().addCondition("payNoteId", payNoteId));
        if (payNoteItems.length == 0) {
            throw new WMSServiceException("判断：查询薪资发放单条目出错！");
        }
        for (int i = 0; i < payNoteItems.length; i++) {
            if (payNoteItems[i].getState() != state) {
                return false;
            }
        }
        return true;
    }


    //将符合状态的条目提取出来
    private PayNoteItem[] getStateItem(PayNoteItemView[] payNoteItemViews, List<Integer> state) {
        List<PayNoteItem> payNoteItemList = new ArrayList<>();
        for (int i = 0; i < payNoteItemViews.length; i++) {
            if (state.contains(payNoteItemViews[i].getState())) {
                PayNoteItem payNoteItem = ReflectHelper.createAndCopyFields(payNoteItemViews[i], PayNoteItem.class);
                payNoteItemList.add(payNoteItem);
            }
        }
        PayNoteItem[] payNoteItems = new PayNoteItem[payNoteItemList.size()];
        payNoteItemList.toArray(payNoteItems);
        return payNoteItems;
    }


    public void addAllItem(String accountBook, AddAllItem AddAllItem) {
        int payNoteId = AddAllItem.getPayNoteId();
        PayNote[] payNotes = payNoteService.findTable(accountBook, new Condition().addCondition("id", payNoteId));
        PayNoteItem[] payNoteItemsTable = payNoteItemDAO.findTable(accountBook, new Condition().addCondition("payNoteId", payNoteId));
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < payNoteItemsTable.length; i++) {
            ids.add(payNoteItemsTable[i].getPersonId());
        }
        if (payNotes.length != 1) {
            throw new WMSServiceException("查询薪金发放单出错！");
        }
        if (payNotes[0].getState() != PayNoteState.WAITING_FOR_CONFIRM) {
            throw new WMSServiceException("本单不在待确认状态，无法添加条目！");
        }
        int periodId = payNotes[0].getSalaryPeriodId();
        int typeId = payNotes[0].getSalaryTypeId();
        int warehouseId = payNotes[0].getWarehouseId();
        List<PayNoteItem> payNoteItemList = new ArrayList<>();
        SalaryItem[] salaryItems = this.salaryItemService.findTable(accountBook, new Condition().addCondition("salaryTypeId", typeId));
        List<Integer> itemIds = new ArrayList<>();
        for (int i = 0; i < salaryItems.length; i++) {
            itemIds.add(salaryItems[i].getId());
        }
        PersonSalaryView[] personSalaryViews = personSalaryService.find(accountBook, new Condition().addCondition("salaryPeriodId", periodId).addCondition("warehouseId", warehouseId).addCondition("salaryItemId", itemIds.toArray(), ConditionItem.Relation.IN));
        //把人员薪资按人员分组
        Map<Integer, List<PersonSalaryView>> groupByPersonIdMap =
                Stream.of(personSalaryViews).collect(Collectors.groupingBy(PersonSalaryView::getPersonId));
        for (Map.Entry<Integer, List<PersonSalaryView>> entry : groupByPersonIdMap.entrySet()) {
            PersonSalaryView[] personSalaryViewsEachGroup = new PersonSalaryView[entry.getValue().size()];
            entry.getValue().toArray(personSalaryViewsEachGroup);
            BigDecimal preTaxAmount = new BigDecimal(0);
            for (int i = 0; i < personSalaryViewsEachGroup.length; i++) {
                if (personSalaryViewsEachGroup[i].getGiveOut() == SalaryItemTypeState.GIVE_OUT_ON)
                    preTaxAmount = preTaxAmount.add(personSalaryViewsEachGroup[i].getAmount());
            }
            if (ids.contains(entry.getKey())) {
                continue;
            }
            PayNoteItem payNoteItem = new PayNoteItem();
            payNoteItem.setPersonId(entry.getKey());
            payNoteItem.setPreTaxAmount(preTaxAmount);
            payNoteItem.setAfterTaxAmount(BigDecimal.ZERO);
            payNoteItem.setTaxAmount(BigDecimal.ZERO);
            payNoteItem.setPaidAmount(BigDecimal.ZERO);
            payNoteItem.setPayNoteId(payNoteId);
            payNoteItem.setState(PayNoteItemState.WAITING_FOR_CALCULATE_PAY);
            payNoteItem.setComment("自动生成薪资单条目");
            payNoteItemList.add(payNoteItem);
        }
        PayNoteItem[] payNoteItems = new PayNoteItem[payNoteItemList.size()];
        payNoteItemList.toArray(payNoteItems);
        payNoteItemDAO.add(accountBook, payNoteItems);
    }
}
