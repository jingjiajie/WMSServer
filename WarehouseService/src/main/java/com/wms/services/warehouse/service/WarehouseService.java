package com.wms.services.warehouse.service;

import com.wms.services.warehouse.datastructures.WarehouseEntryAndItems;
import com.wms.utilities.model.Warehouse;
import com.wms.utilities.model.WarehouseView;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;

import java.util.List;

public interface WarehouseService {
    int[] add(String accountBook, Warehouse warehouses[]) throws WMSServiceException;
    void update(String accountBook,Warehouse warehouses[]) throws WMSServiceException;
    void remove(String accountBook,int ids[]) throws WMSServiceException;
    WarehouseView[] find(String accountBook, Condition cond) throws WMSServiceException;
    Warehouse[] findTable(String accountBook, Condition cond) throws WMSServiceException;
    long findCount(String database,Condition cond) throws WMSServiceException;
}
