package com.wms.services.warehouse.service;

import com.wms.services.warehouse.model.StorageArea;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;

public interface StoragrAreaService {
   int[] add(String accountBook,StorageArea[] storageAreas) throws WMSServiceException;
   void update(String accountBook, StorageArea[] storageAreas) throws WMSServiceException;
    void remove(String accountBook, int[] ids) throws WMSServiceException;
    StorageArea[] find(String accountBook, Condition cond) throws WMSServiceException;

}
