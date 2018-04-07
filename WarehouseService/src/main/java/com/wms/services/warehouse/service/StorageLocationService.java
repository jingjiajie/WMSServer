package com.wms.services.warehouse.service;

import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.services.warehouse.model.StorageLocation;
public interface StorageLocationService {
    int[] add(String accountBook, StorageLocation storageLocations[]) throws WMSServiceException;
    void update(String accountBook,StorageLocation storageLocations[]) throws WMSServiceException;
    void remove(String accountBook,int ids[]) throws WMSServiceException;
    StorageLocation[] find(String accountBook,Condition cond) throws WMSServiceException;
}
