package com.wms.services.warehouse.controller;

import org.springframework.http.ResponseEntity;
import com.wms.services.warehouse.model.StorageArea;
public interface StorageAreaController {
    ResponseEntity<int[]> add(String accountBook, StorageArea[] storageAreas);
    void update(String accountBook,StorageArea[] storageAreas);
    void remove(String accountBook,String strIDs);
    ResponseEntity<StorageArea[]> find(String accountBook,String condStr);
}
