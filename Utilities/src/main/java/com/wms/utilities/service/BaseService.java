package com.wms.utilities.service;

import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;

public interface BaseService<TTable,TView> {
    int[] add(String accountBook, TTable objs[]) throws WMSServiceException;
    void update(String accountBook,TTable objs[]) throws WMSServiceException;
    void remove(String accountBook,int ids[]) throws WMSServiceException;
    TView[] find(String accountBook, Condition cond) throws WMSServiceException;
}
