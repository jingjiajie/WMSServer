package com.wms.services.ledger.service;

import com.wms.utilities.model.TaxItem;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;

import java.util.Map;

public interface TaxItemService {
    int[] add(String accountBook, TaxItem taxItems[]) throws WMSServiceException;
    void update(String accountBook,TaxItem taxItems[]) throws WMSServiceException;
    void remove(String accountBook,int ids[]) throws WMSServiceException;
    TaxItem[] find(String accountBook,Condition cond) throws WMSServiceException;
}