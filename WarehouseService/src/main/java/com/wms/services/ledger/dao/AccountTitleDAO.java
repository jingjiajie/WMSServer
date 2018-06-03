package com.wms.services.ledger.dao;

import com.wms.utilities.model.AccountTitle;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.datastructures.Condition;

import java.util.Map;

public interface AccountTitleDAO {
    int[] add(String database, AccountTitle accountTitles[]) throws WMSDAOException;
    void update(String database,AccountTitle accountTitles[]) throws WMSDAOException;
    void remove(String database, int[] ids) throws WMSDAOException;
    AccountTitle[] find(String database,Condition cond) throws WMSDAOException;
}