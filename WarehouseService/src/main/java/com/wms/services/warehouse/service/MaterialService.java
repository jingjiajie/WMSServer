package com.wms.services.warehouse.service;

import com.wms.services.warehouse.model.MaterialView;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.services.warehouse.model.Material;

public interface MaterialService {
    int[] add(String accountBook, Material materials[]) throws WMSServiceException;
    void update(String accountBook,Material materials[]) throws WMSServiceException;
    void remove(String accountBook,int ids[]) throws WMSServiceException;
    MaterialView[] find(String accountBook, Condition cond) throws WMSServiceException;
}
