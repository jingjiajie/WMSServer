package com.wms.services.warehouse.controller;

import org.springframework.http.ResponseEntity;
import com.wms.services.warehouse.model.StorageLocation;
import com.wms.services.warehouse.model.StorageLocationView;
public interface StorageLocationController {
    ResponseEntity<int[]> add(String accountBook, StorageLocation[] storageLocations);
    void update(String accountBook,StorageLocation[] storageLocations);
    void remove(String accountBook,String strIDs);
    ResponseEntity<StorageLocationView[]> find(String accountBook,String condStr);
}
