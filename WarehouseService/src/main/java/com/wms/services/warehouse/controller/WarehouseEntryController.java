package com.wms.services.warehouse.controller;


import com.wms.services.warehouse.datastructures.InspectArgs;
import com.wms.utilities.controller.BaseController;
import com.wms.utilities.model.Warehouse;
import com.wms.utilities.model.WarehouseEntry;
import com.wms.utilities.model.WarehouseEntryView;
import org.springframework.http.ResponseEntity;

public interface WarehouseEntryController
        extends BaseController<WarehouseEntry,WarehouseEntryView>{
    void inspect(String accountBook, InspectArgs inspectArgs);
}