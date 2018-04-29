package com.wms.services.warehouse.controller;

import com.wms.utilities.model.Supply;
import com.wms.utilities.model.SupplyView;
import org.springframework.http.ResponseEntity;

public interface SupplyController {
    ResponseEntity<int[]> add(String accountBook, Supply[] supplys);
    void update(String accountBook,Supply supplys[]);
    void remove(String accountBook,String strIDs);
    ResponseEntity<SupplyView[]> find(String accountBook, String condStr);
}
