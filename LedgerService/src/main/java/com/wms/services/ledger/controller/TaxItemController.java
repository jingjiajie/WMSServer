package com.wms.services.ledger.controller;

import com.wms.services.ledger.model.TaxItem;
import org.springframework.http.ResponseEntity;

public interface TaxItemController {
    ResponseEntity<int[]> add(String accountBook,TaxItem[] taxItems);
    void remove(String accountBook,String strIDs);
    void update(String accountBook,TaxItem taxItems[]);
    ResponseEntity<TaxItem[]> find(String accountBook,String condStr);
}
