package com.wms.services.warehouse.controller;

import com.wms.services.warehouse.model.MaterialView;
import org.springframework.http.ResponseEntity;
import com.wms.services.warehouse.model.Material;

public interface MaterialController {
    ResponseEntity<int[]> add(String accountBook,Material[] materials);
    void remove(String accountBook,String strIDs);
    void update(String accountBook,Material materials[]);
    ResponseEntity<MaterialView[]> find(String accountBook, String condStr);
}
