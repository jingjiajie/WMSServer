package com.wms.services.warehouse.controller;

import com.wms.services.warehouse.model.Material;
import org.springframework.http.ResponseEntity;

public interface MaterialController {
    ResponseEntity<int[]> add(String accountBook,Material[] materials);
    void remove(String accountBook,String strIDs);
    void update(String accountBook,Material materials[]);
    ResponseEntity<Material[]> find(String accountBook,String condStr);
}
