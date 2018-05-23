package com.wms.services.warehouse.service;

import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.StorageLocation;
import com.wms.utilities.model.StorageLocationView;
import com.wms.utilities.model.StorageLocationView;
public interface StorageLocationService {
    int[] add(String accountBook, StorageLocation storageLocations[]) throws WMSServiceException;
    void update(String accountBook,StorageLocation storageLocations[]) throws WMSServiceException;
    void remove(String accountBook,int ids[]) throws WMSServiceException;
    StorageLocationView[] find(String accountBook, Condition cond) throws WMSServiceException;
    long findCount(String database,Condition cond) throws WMSServiceException;
}
