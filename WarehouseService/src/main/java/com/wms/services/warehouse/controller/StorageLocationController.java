package com.wms.services.warehouse.controller;

import com.wms.utilities.model.StorageLocationView;
import org.springframework.http.ResponseEntity;
import com.wms.utilities.model.StorageLocation;
import com.wms.utilities.model.StorageLocationView;
public interface StorageLocationController {
    ResponseEntity<int[]> add(String accountBook, StorageLocation[] storageLocations);
    void update(String accountBook,StorageLocation[] storageLocations);
    void remove(String accountBook,String strIDs);
    ResponseEntity<StorageLocationView[]> find(String accountBook, String condStr);
    long findCount(String accountBook,String condStr);
}
