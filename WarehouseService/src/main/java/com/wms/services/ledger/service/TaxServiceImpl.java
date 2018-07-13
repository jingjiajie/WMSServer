package com.wms.services.ledger.service;

import com.wms.services.ledger.dao.TaxDAO;
import com.wms.utilities.model.Tax;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.TaxItemView;
import com.wms.utilities.model.TaxView;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wms.services.ledger.datestructures.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.stream.Stream;

@Service
public class TaxServiceImpl implements TaxService {
    @Autowired
    TaxDAO taxDAO;
    @Autowired
    TaxItemService taxItemService;

    @Transactional
    public int[] add(String accountBook, Tax[] taxes) throws WMSServiceException{
        this.validateEntities(accountBook,taxes);
        try{
            return taxDAO.add(accountBook,taxes);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }

    @Transactional
    public void update(String accountBook, Tax[] taxes) throws WMSServiceException{
        this.validateEntities(accountBook,taxes);
        try {
            taxDAO.update(accountBook, taxes);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
        try {
            taxDAO.remove(accountBook, ids);
        } catch (DatabaseNotFoundException ex) {
            throw new WMSServiceException("Accountbook " + accountBook + " not found!");
        }
    }

    @Transactional
    public TaxView[] find(String accountBook, Condition cond) throws WMSServiceException{
        try {
            return this.taxDAO.find(accountBook, cond);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }

    @Transactional
    public long findCount(String accountBook,Condition cond) throws WMSServiceException{
        return this.taxDAO.findCount(accountBook,cond);
    }

    private void validateEntities(String accountBook,Tax[] taxes) throws WMSServiceException{
        Stream.of(taxes).forEach((tax -> {
            new Validator("税务名称").notEmpty().validate(tax.getName());
            new Validator("税务代码").notEmpty().validate(tax.getNo());
        }));

    }

    @Transactional
    public BigDecimal taxCalculation(String accountBook, TaxCalculation taxCalculation) throws WMSServiceException{
        BigDecimal taxAmount=new BigDecimal(0);
        BigDecimal curAmount=taxCalculation.getMoneyAmount();
        int taxId=taxCalculation.getTaxId();
        TaxItemView[] taxItemViews=taxItemService.find(accountBook,new Condition().addCondition("taxId",new  Integer[]{taxId}));
        for(int i=0;i<taxItemViews.length;i++){
            if (curAmount.compareTo(taxItemViews[i].getStartAmount())>0&&curAmount.compareTo(taxItemViews[i].getEndAmount())<=0){
                if (taxItemViews[i].getType()==TaxItemService.Type_PROPORTION) {
                    taxAmount = taxAmount.add((curAmount.subtract(taxItemViews[i].getStartAmount())).multiply(taxItemViews[i].getTaxRate()));
                }
                if (taxItemViews[i].getType()==TaxItemService.Type_QUOTA) {
                    taxAmount = taxAmount.add(taxItemViews[i].getTaxAmount());
                }
            }
            if (curAmount.compareTo(taxItemViews[i].getStartAmount())>0&&curAmount.compareTo(taxItemViews[i].getEndAmount())>0){
                if (taxItemViews[i].getType()==TaxItemService.Type_PROPORTION) {
                    taxAmount = taxAmount.add((taxItemViews[i].getEndAmount().subtract(taxItemViews[i].getStartAmount())).multiply(taxItemViews[i].getTaxRate()));
                }
                if (taxItemViews[i].getType()==TaxItemService.Type_QUOTA) {
                    taxAmount = taxAmount.add(taxItemViews[i].getTaxAmount());
                }
            }

        }
        return taxAmount;
    }
}
