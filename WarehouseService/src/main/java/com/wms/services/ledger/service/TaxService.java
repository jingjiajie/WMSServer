package com.wms.services.ledger.service;

import com.wms.utilities.model.Tax;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;

import java.util.Map;

public interface TaxService {
    int[] add(String accountBook, Tax taxes[]) throws WMSServiceException;
    void update(String accountBook,Tax taxes[]) throws WMSServiceException;
    void remove(String accountBook,int ids[]) throws WMSServiceException;
    Tax[] find(String accountBook,Condition cond) throws WMSServiceException;
}
