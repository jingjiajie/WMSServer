package com.wms.services.warehouse.service;

import com.wms.services.warehouse.model.WarehouseEntry;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;

public interface WarehouseEntryService {
    int[] add(String accountBook, WarehouseEntry warehouseEntries[]) throws WMSServiceException;
    void update(String accountBook,WarehouseEntry warehouseEntries[]) throws WMSServiceException;
    void remove(String accountBook,int ids[]) throws WMSServiceException;
    WarehouseEntry[] find(String accountBook, Condition cond) throws WMSServiceException;
}
