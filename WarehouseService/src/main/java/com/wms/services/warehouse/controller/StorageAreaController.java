package com.wms.services.warehouse.controller;

import com.wms.services.warehouse.model.StorageArea;
import org.springframework.http.ResponseEntity;
import com.wms.services.warehouse.model.StorageAreaView;
public interface StorageAreaController {
    ResponseEntity<int[]> add(String accountBook,StorageArea[] storageAreas);
    void remove(String accountBook,String strIDs);
    void update(String accountBook,StorageArea storageAreas[]);
    ResponseEntity<StorageAreaView[]> find(String accountBook, String condStr);
}
