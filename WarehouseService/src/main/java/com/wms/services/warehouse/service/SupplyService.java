package com.wms.services.warehouse.service;
import com.wms.services.warehouse.model.Supply;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;

public interface SupplyService {
    int[] add(String accountBook, Supply supplies[]) throws WMSServiceException;
    void update(String accountBook,Supply supplies[]) throws WMSServiceException;
    void remove(String accountBook,int ids[]) throws WMSServiceException;
    Supply[] find(String accountBook,Condition cond) throws WMSServiceException;
}
