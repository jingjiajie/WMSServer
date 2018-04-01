package com.wms.services.warehouse.controller;

import com.wms.services.warehouse.model.StorageArea;
import com.wms.services.warehouse.model.Supplier;
import org.springframework.http.ResponseEntity;

public interface StorgeAreaController {
    ResponseEntity<int[]> add(String accountBook, Supplier[] suppliers);
    void update(String accountBook,StorageArea storageAreas[]);
    void remove(String accountBook,String strIDs);
    ResponseEntity<StorageArea[]> find(String accountBook,String condStr);
}
