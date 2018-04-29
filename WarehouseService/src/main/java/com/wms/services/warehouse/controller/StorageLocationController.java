package com.wms.services.warehouse.controller;

import org.springframework.http.ResponseEntity;
import com.wms.utilities.model.StorageLocation;
public interface StorageLocationController {
    ResponseEntity<int[]> add(String accountBook, StorageLocation[] storageLocations);
    void update(String accountBook,StorageLocation[] storageLocations);
    void remove(String accountBook,String strIDs);
    ResponseEntity<StorageLocation[]> find(String accountBook,String condStr);
}
