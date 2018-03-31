package com.wms.services.warehouse.service;
import com.wms.services.warehouse.model.Supplier;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
public interface SupplierServices {
    int[] add(String accountBook, Supplier persons[]) throws WMSServiceException;

}
