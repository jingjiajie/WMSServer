package com.wms.services.warehouse.controller;


import com.wms.services.warehouse.model.Warehouse;
import com.wms.services.warehouse.model.WarehouseEntry;
import org.springframework.http.ResponseEntity;

public interface WarehouseEntryController {

        ResponseEntity<int[]> add(String accountBook, WarehouseEntry[] warehouseEntries);

        void update(String accountBook, WarehouseEntry[] warehouseEntries);

        void remove(String accountBook, String strIDs);

        ResponseEntity<WarehouseEntry[]> find(String accountBook, String condStr);

}