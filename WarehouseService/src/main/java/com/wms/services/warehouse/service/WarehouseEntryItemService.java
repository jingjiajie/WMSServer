package com.wms.services.warehouse.service;

import com.wms.utilities.model.WarehouseEntry;
import com.wms.utilities.model.WarehouseEntryItem;
import com.wms.utilities.model.WarehouseEntryItemView;
import com.wms.utilities.model.WarehouseEntryView;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.service.BaseService;

public interface WarehouseEntryItemService
        extends BaseService<WarehouseEntryItem, WarehouseEntryItemView> {
    void update(String accountBook, WarehouseEntryItem[] warehouseEntryItems, boolean allowUpdateInspectionAmount) throws WMSServiceException;
    void reject(String accountBook, int[] ids) throws WMSServiceException;
    void receive(String accountBook, int[] ids) throws WMSServiceException;

    int WAIT_FOR_PUT_IN_STORAGE = 0;//待入库
    int BEING_INSPECTED = 1;//送检中
    int QUALIFIED = 2; //正品入库
    int UNQUALIFIED = 3; //不良品入库
}
