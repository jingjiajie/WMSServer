package com.wms.services.warehouse.controller;


import com.wms.utilities.controller.BaseController;
import com.wms.utilities.model.WarehouseEntry;
import com.wms.utilities.model.WarehouseEntryItem;
import com.wms.utilities.model.WarehouseEntryItemView;
import com.wms.utilities.model.WarehouseEntryView;
import com.wms.utilities.datastructures.Condition;
import org.springframework.http.ResponseEntity;

public interface WarehouseEntryItemController
        extends BaseController<WarehouseEntryItem,WarehouseEntryItemView>{

}