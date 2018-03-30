package com.wms.services.ledger.service;

import com.wms.services.ledger.dao.AccountTitleDAO;
import com.wms.services.ledger.model.AccountTitle;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountTitleServiceImpl implements AccountTitleService {
    @Autowired
    AccountTitleDAO accountTitleDAO;

    @Transactional
    public  int[] add(String accountBook, AccountTitle[] accountTitles) throws WMSServiceException{
        try{
            return accountTitleDAO.add(accountBook,accountTitles);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook" + accountBook + "not found!");
        }
    }

    @Transactional
    public void update(String accountBook, AccountTitle[] accountTitles) throws WMSServiceException{
        try {
            accountTitleDAO.update(accountBook, accountTitles);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook" + accountBook + "not found!");
        }
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
        try {
            accountTitleDAO.remove(accountBook, ids);
        }catch (DatabaseNotFoundException ex) {
            throw new WMSServiceException("Accountbook" + accountBook + "not found!");
        }
    }

    @Transactional
    public AccountTitle[] find(String accountBook, Condition cond) throws WMSServiceException{
        try {
            return this.accountTitleDAO.find(accountBook, cond);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook" + accountBook + "not found!");
        }
    }
}
