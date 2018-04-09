package com.wms.services.warehouse.controller;


import com.wms.services.warehouse.model.Warehouse;
import org.springframework.http.ResponseEntity;
public interface WarehouseController {

        ResponseEntity<int[]> add(String accountBook, Warehouse[] warehouses);

        void update(String accountBook, Warehouse[] warehouses);

        void remove(String accountBook, String strIDs);

        ResponseEntity<Warehouse[]> find(String accountBook, String condStr);

}