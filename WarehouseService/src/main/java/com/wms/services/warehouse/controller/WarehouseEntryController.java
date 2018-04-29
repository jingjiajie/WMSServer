package com.wms.services.warehouse.controller;


import com.wms.utilities.model.Warehouse;
import com.wms.utilities.model.WarehouseEntry;
import com.wms.utilities.model.WarehouseEntryView;
import org.springframework.http.ResponseEntity;

public interface WarehouseEntryController {

        ResponseEntity<int[]> add(String accountBook, WarehouseEntry[] warehouseEntries);

        void update(String accountBook, WarehouseEntry[] warehouseEntries);

        void remove(String accountBook, String strIDs);

        ResponseEntity<WarehouseEntryView[]> find(String accountBook, String condStr);

}