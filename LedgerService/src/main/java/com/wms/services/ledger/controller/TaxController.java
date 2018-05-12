package com.wms.services.ledger.controller;

import com.wms.utilities.model.Tax;
import org.springframework.http.ResponseEntity;

public interface TaxController {
    ResponseEntity<int[]> add(String accountBook,Tax[] taxes);
    void remove(String accountBook,String strIDs);
    void update(String accountBook,Tax taxes[]);
    ResponseEntity<Tax[]> find(String accountBook,String condStr);
}
