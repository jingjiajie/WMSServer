package com.wms.services.ledger.service;

import com.wms.services.ledger.dao.TaxDAO;
import com.wms.services.ledger.model.Tax;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class TaxServiceImpl implements TaxService {
    @Autowired
    TaxDAO taxDAO;

    @Transactional
    public int[] add(String accountBook, Tax[] taxes) throws WMSServiceException{
        try{
            return taxDAO.add(accountBook,taxes);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }

    @Transactional
    public void update(String accountBook, Tax[] taxes) throws WMSServiceException{
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
    public Tax[] find(String accountBook, Condition cond) throws WMSServiceException{
        try {
            return this.taxDAO.find(accountBook, cond);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }
}
