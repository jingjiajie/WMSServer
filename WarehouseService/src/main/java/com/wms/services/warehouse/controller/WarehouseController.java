package com.wms.services.warehouse.controller;


import com.wms.utilities.model.Warehouse;
import com.wms.utilities.model.WarehouseView;
import org.springframework.http.ResponseEntity;
public interface WarehouseController {

        ResponseEntity<int[]> add(String accountBook, Warehouse[] warehouses);

        void update(String accountBook, Warehouse[] warehouses);

        void remove(String accountBook, String strIDs);

        ResponseEntity<WarehouseView[]> find(String accountBook, String condStr);

}