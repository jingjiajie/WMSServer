package com.wms.services.warehouse.service;
import com.wms.utilities.model.Supplier;
import com.wms.utilities.model.SupplierView;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;

public interface SupplierServices {
    int[] add(String accountBook, Supplier suppliers[]) throws WMSServiceException;
    void update(String accountBook,Supplier suppliers[]) throws WMSServiceException;
    void remove(String accountBook,int ids[]) throws WMSServiceException;
    SupplierView[] find(String accountBook, Condition cond) throws WMSServiceException;
    long findCount(String database,Condition cond) throws WMSServiceException;
}
