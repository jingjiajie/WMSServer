package com.wms.services.warehouse.controller;

import com.wms.utilities.model.StorageArea;
import org.springframework.http.ResponseEntity;

public interface StorageAreaController {
    ResponseEntity<int[]> add(String accountBook,StorageArea[] storageAreas);
    void remove(String accountBook,String strIDs);
    void update(String accountBook,StorageArea storageAreas[]);
    ResponseEntity<StorageArea[]> find(String accountBook, String condStr);
}
