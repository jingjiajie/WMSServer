package com.wms.services.warehouse.controller;

import com.wms.services.warehouse.model.StorageLocation;
import org.springframework.http.ResponseEntity;

public interface StorageLocationController {
    ResponseEntity<int[]> add(String accountBook, StorageLocation[] storageLocations);
    void update(String accountBook,StorageLocation storageLocations[]);
    void remove(String accountBook,String strIDs);
    ResponseEntity<StorageLocation[]> find(String accountBook,String condStr);
}