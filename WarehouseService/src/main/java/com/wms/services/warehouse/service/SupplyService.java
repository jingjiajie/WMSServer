package com.wms.services.warehouse.service;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.Supply;
import com.wms.utilities.model.SupplyView;

public interface SupplyService {
    int[] add(String accountBook, Supply supplies[]) throws WMSServiceException;
    void update(String accountBook,Supply supplies[]) throws WMSServiceException;
    void remove(String accountBook,int ids[]) throws WMSServiceException;
    SupplyView[] find(String accountBook, Condition cond) throws WMSServiceException;
    long findCount(String database,Condition cond) throws WMSServiceException;
}
