package com.wms.services.ledger.service;


import com.wms.services.ledger.dao.TaxDAO;
import com.wms.services.ledger.dao.TaxItemDAO;
import com.wms.utilities.IDChecker;
import com.wms.utilities.ReflectHelper;
import com.wms.utilities.model.Tax;
import com.wms.utilities.model.TaxItem;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.TaxItemView;
import com.wms.utilities.model.TaxView;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;


@Service
public class TaxItemServiceImpl implements TaxItemService {
    @Autowired
    TaxItemDAO taxItemDAO;
    @Autowired
    IDChecker idChecker;
    @Autowired
    TaxDAO taxDAO;


    @Transactional
    public int[] add(String accountBook, TaxItem[] taxItems) throws WMSServiceException{
        this.validateEntities(accountBook,taxItems);


        int[] ids= taxItemDAO.add(accountBook,taxItems);
        TaxItem[] allTaxItems=this.taxItemDAO.findTable(accountBook,new Condition().addCondition("taxId",taxItems[0].getTaxId()));
        this.validateEntities1(accountBook,allTaxItems);
        return ids;
    }

    @Transactional
    public void update(String accountBook, TaxItem[] taxItems) throws WMSServiceException{
        this.validateEntities(accountBook,taxItems);
        try {
            taxItemDAO.update(accountBook, taxItems);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
        TaxItem[] allTaxItems=this.taxItemDAO.findTable(accountBook,new Condition().addCondition("taxId",taxItems[0].getTaxId()));
        this.validateEntities1(accountBook,allTaxItems);
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
        try {
            taxItemDAO.remove(accountBook, ids);
        } catch (DatabaseNotFoundException ex) {
            throw new WMSServiceException("Accountbook " + accountBook + " not found!");
        }
    }
    @Transactional
    public TaxItemView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.taxItemDAO.find(accountBook, cond);
    }

    @Transactional
    public long findCount(String accountBook,Condition cond) throws WMSServiceException{
        return this.taxItemDAO.findCount(accountBook,cond);
    }

    private void validateEntities(String accountBook,TaxItem[] taxItems) throws WMSServiceException{
        Stream.of(taxItems).forEach((taxItem -> {
            new Validator("税务类型").min(0).max(1).validate(taxItem.getType());
            new Validator("起始金额").notEmpty().min(0).max(taxItem.getEndAmount()).validate(taxItem.getStartAmount());
            new Validator("截止金额").notEmpty().greaterThan(0).validate(taxItem.getEndAmount());
            if (taxItem.getType()==TaxItemService.Type_QUOTA){
                new Validator("定额金额").min(0).max(taxItem.getEndAmount().subtract(taxItem.getStartAmount())).validate(taxItem.getTaxAmount());
            }
            if (taxItem.getType()==TaxItemService.Type_PROPORTION){
                new Validator("金额比例").min(0).max(1).validate(taxItem.getTaxRate());
            }

            this.idChecker.check(TaxService.class, accountBook, taxItem.getTaxId(), "税务");
        }));

//        for (int i = 0;i < taxItems.length;i ++) {
//
//
//            BigDecimal taxitemStartamount = taxItems[i].getStartAmount();
//            String strtaxitemStartamount = taxitemStartamount.toString();
//            String strleftStartamount = strtaxitemStartamount.split(".")[0];
//            String strrightStartamount = strtaxitemStartamount.split(".")[1];
//            if (strleftStartamount.length() > 18) {
//                throw new WMSServiceException("整数部分不能超过18位!");
//            }
//            if (strrightStartamount.length() > 3) {
//                throw new WMSServiceException("小数部分不能超过3位!");
//            }
//
//            BigDecimal taxitemEndamount = taxItems[i].getEndAmount();
//            String strtaxitemEndamount = taxitemEndamount.toString();
//            String strleftEndamount = strtaxitemEndamount.split(".")[0];
//            String strrightEndamount = strtaxitemEndamount.split(".")[1];
//            if (strleftEndamount.length() > 18) {
//                throw new WMSServiceException("整数部分不能超过18位!");
//            }
//            if (strrightEndamount.length() > 3) {
//                throw new WMSServiceException("小数部分不能超过3位!");
//            }
//
//            BigDecimal taxitemTaxamount = taxItems[i].getTaxAmount();
//            String strtaxitemTaxamount = taxitemTaxamount.toString();
//            String strleftTaxamount = strtaxitemTaxamount.split(".")[0];
//            String strrightTaxamount = strtaxitemTaxamount.split(".")[1];
//            if (strleftTaxamount.length() > 18) {
//                throw new WMSServiceException("整数部分不能超过18位!");
//            }
//            if (strrightTaxamount.length() > 3) {
//                throw new WMSServiceException("小数部分不能超过3位!");
//            }
//
//            BigDecimal taxitemTaxrate = taxItems[i].getTaxRate();
//            String strtaxitemTaxrate = taxitemTaxrate.toString();
//            String strleftTaxrate = strtaxitemTaxrate.split(".")[0];
//            String strrightTaxrate = strtaxitemTaxrate.split(".")[1];
//            if (strleftTaxrate.length() > 18) {
//                throw new WMSServiceException("整数部分不能超过18位!");
//            }
//            if (strrightTaxrate.length() > 3) {
//                throw new WMSServiceException("小数部分不能超过3位!");
//            }
//
//        }

    }
    private void validateEntities1(String accountBook,TaxItem[] taxItems) throws WMSServiceException {
        List<TaxItem> taxItemList= Arrays.asList(taxItems);
        taxItemList.stream().sorted(Comparator.comparing(TaxItem::getStartAmount)).reduce((last, cur) -> {
            if (last.getEndAmount().compareTo(cur.getStartAmount())>0){
                throw new WMSServiceException("税务金额计算区间不能重叠！");
            }
            return cur;
        });
    }

}
