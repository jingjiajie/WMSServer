package com.wms.services.warehouse.service;
import com.wms.services.warehouse.model.Supplier;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;

public interface SupplierServices {
    int[] add(String accountBook, Supplier suppliers[]) throws WMSServiceException;
    void update(String accountBook,Supplier suppliers[]) throws WMSServiceException;
    void remove(String accountBook,int ids[]) throws WMSServiceException;
    Supplier[] find(String accountBook,Condition cond) throws WMSServiceException;

}
