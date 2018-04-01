package com.wms.services.ledger.service;


import com.wms.services.ledger.dao.TaxItemDAO;
import com.wms.services.ledger.model.TaxItem;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class TaxItemServiceImpl implements TaxItemService {
    @Autowired
    TaxItemDAO taxItemDAO;

    @Transactional
    public int[] add(String accountBook, TaxItem[] taxItems) throws WMSServiceException{
        try{
            return taxItemDAO.add(accountBook,taxItems);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }

    @Transactional
    public void update(String accountBook, TaxItem[] taxItems) throws WMSServiceException{
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
    public TaxItem[] find(String accountBook, Condition cond) throws WMSServiceException{
        try {
            return this.taxItemDAO.find(accountBook, cond);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }
}
