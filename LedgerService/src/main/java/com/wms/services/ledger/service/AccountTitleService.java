package com.wms.services.ledger.service;

import com.wms.services.ledger.model.AccountTitle;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;

public interface AccountTitleService {
    int[] add(String accountBook, AccountTitle accountTitles[]) throws WMSServiceException;
    void update(String accountBook,AccountTitle accountTitles[]) throws WMSServiceException;
    void remove(String accountBook, int[] ids) throws WMSServiceException;
    AccountTitle[] find(String accountBook,Condition cond) throws WMSServiceException;
}
