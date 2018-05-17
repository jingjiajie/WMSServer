package com.wms.services.warehouse.service;

import com.wms.utilities.model.WarehouseEntry;
import com.wms.utilities.model.WarehouseEntryItem;
import com.wms.utilities.model.WarehouseEntryItemView;
import com.wms.utilities.model.WarehouseEntryView;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.service.BaseService;

public interface WarehouseEntryItemService
        extends BaseService<WarehouseEntryItem,WarehouseEntryItemView> {
}
