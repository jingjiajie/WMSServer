package com.wms.services.warehouse.service;

import com.wms.services.warehouse.model.Warehouse;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;

public interface WarehouseService {
    int[] add(String accountBook, Warehouse warehouses[]) throws WMSServiceException;
    void update(String accountBook,Warehouse warehouses[]) throws WMSServiceException;
    void remove(String accountBook,int ids[]) throws WMSServiceException;
    Warehouse[] find(String accountBook, Condition cond) throws WMSServiceException;
}
