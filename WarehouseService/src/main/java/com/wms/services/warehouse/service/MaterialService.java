package com.wms.services.warehouse.service;

import com.wms.services.warehouse.model.Material;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import java.util.Map;

public interface MaterialService {
    int[] add(String accountBook, Material materials[]) throws WMSServiceException;
    void update(String accountBook,Material materials[]) throws WMSServiceException;
    void remove(String accountBook,int ids[]) throws WMSServiceException;
    Material[] find(String accountBook,Condition cond) throws WMSServiceException;
}
