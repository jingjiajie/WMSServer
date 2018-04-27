package com.wms.services.warehouse.service;

import com.wms.services.warehouse.model.WarehouseEntry;
import com.wms.services.warehouse.model.WarehouseEntryItem;
import com.wms.services.warehouse.model.WarehouseEntryItemView;
import com.wms.services.warehouse.model.WarehouseEntryView;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;

public interface WarehouseEntryItemService {
    int[] add(String accountBook, WarehouseEntryItem warehouseEntryItems[]) throws WMSServiceException;
    void update(String accountBook, WarehouseEntryItem warehouseEntryItems[]) throws WMSServiceException;
    void remove(String accountBook, int ids[]) throws WMSServiceException;
    WarehouseEntryItemView[] find(String accountBook, Condition cond) throws WMSServiceException;
}
