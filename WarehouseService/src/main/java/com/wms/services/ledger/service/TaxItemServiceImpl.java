package com.wms.services.ledger.service;


import com.wms.services.ledger.dao.TaxDAO;
import com.wms.services.ledger.dao.TaxItemDAO;
import com.wms.utilities.IDChecker;
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
        try{
            return taxItemDAO.add(accountBook,taxItems);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }

    @Transactional
    public void update(String accountBook, TaxItem[] taxItems) throws WMSServiceException{
        this.validateEntities(accountBook,taxItems);
        try {
            taxItemDAO.update(accountBook, taxItems);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
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
            new Validator("税务名称").min(0).max(1).validate(taxItem.getType());
            this.idChecker.check(TaxService.class, accountBook, taxItem.getTaxId(), "税务");
        }));

        for (int i = 0;i < taxItems.length;i ++) {


            BigDecimal taxitemStartamount = taxItems[i].getStartAmount();
            String strtaxitemStartamount = taxitemStartamount.toString();
            String strleftStartamount = strtaxitemStartamount.split(".")[0];
            String strrightStartamount = strtaxitemStartamount.split(".")[1];
            if (strleftStartamount.length() > 18) {
                throw new WMSServiceException("整数部分不能超过18位!");
            }
            if (strrightStartamount.length() > 3) {
                throw new WMSServiceException("小数部分不能超过3位!");
            }

            BigDecimal taxitemEndamount = taxItems[i].getEndAmount();
            String strtaxitemEndamount = taxitemEndamount.toString();
            String strleftEndamount = strtaxitemEndamount.split(".")[0];
            String strrightEndamount = strtaxitemEndamount.split(".")[1];
            if (strleftEndamount.length() > 18) {
                throw new WMSServiceException("整数部分不能超过18位!");
            }
            if (strrightEndamount.length() > 3) {
                throw new WMSServiceException("小数部分不能超过3位!");
            }

            BigDecimal taxitemTaxamount = taxItems[i].getTaxAmount();
            String strtaxitemTaxamount = taxitemTaxamount.toString();
            String strleftTaxamount = strtaxitemTaxamount.split(".")[0];
            String strrightTaxamount = strtaxitemTaxamount.split(".")[1];
            if (strleftTaxamount.length() > 18) {
                throw new WMSServiceException("整数部分不能超过18位!");
            }
            if (strrightTaxamount.length() > 3) {
                throw new WMSServiceException("小数部分不能超过3位!");
            }

            BigDecimal taxitemTaxrate = taxItems[i].getTaxRate();
            String strtaxitemTaxrate = taxitemTaxrate.toString();
            String strleftTaxrate = strtaxitemTaxrate.split(".")[0];
            String strrightTaxrate = strtaxitemTaxrate.split(".")[1];
            if (strleftTaxrate.length() > 18) {
                throw new WMSServiceException("整数部分不能超过18位!");
            }
            if (strrightTaxrate.length() > 3) {
                throw new WMSServiceException("小数部分不能超过3位!");
            }

        }

    }
}
