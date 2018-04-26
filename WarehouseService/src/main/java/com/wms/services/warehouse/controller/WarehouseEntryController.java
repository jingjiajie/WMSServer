package com.wms.services.warehouse.controller;


import com.wms.services.warehouse.model.Warehouse;
import com.wms.services.warehouse.model.WarehouseEntry;
import com.wms.services.warehouse.model.WarehouseEntryView;
import org.springframework.http.ResponseEntity;

public interface WarehouseEntryController {

        ResponseEntity<int[]> add(String accountBook, WarehouseEntryView[] warehouseEntryViews);

        void update(String accountBook, WarehouseEntryView[] warehouseEntryViews);

        void remove(String accountBook, String strIDs);

        ResponseEntity<WarehouseEntryView[]> find(String accountBook, String condStr);

}