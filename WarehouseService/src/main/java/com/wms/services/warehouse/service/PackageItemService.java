package com.wms.services.warehouse.service;

import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.PackageItem;
import com.wms.utilities.model.PackageItemView;

public interface PackageItemService {
    int[] add(String accountBook, PackageItem packageItems[]) throws WMSServiceException;
    void update(String accountBook, PackageItem packageItems[]) throws WMSServiceException;
    void remove(String accountBook, int ids[]) throws WMSServiceException;
    PackageItemView[] find(String accountBook, Condition cond) throws WMSServiceException;
}
