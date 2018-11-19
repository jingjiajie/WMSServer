package com.wms.services.warehouse.service;

import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.StorageLocation;
import com.wms.utilities.model.StorageLocationView;
import com.wms.utilities.service.BaseService;

public interface StorageLocationService
    extends BaseService<StorageLocation,StorageLocationView>{
    Object[] findLess(String accountBook, Condition cond) throws WMSServiceException;
}
