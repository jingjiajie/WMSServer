package com.wms.services.warehouse.controller;

import com.wms.utilities.model.TransferRecord;
import com.wms.utilities.model.TransferRecordView;
import org.springframework.http.ResponseEntity;

public interface TransferRecordController {
    ResponseEntity<int[]> add(String accountBook, TransferRecord[] warehouses);
    ResponseEntity<TransferRecordView[]> find(String accountBook, String condStr);
    long findCount(String accountBook,String condStr);
}
