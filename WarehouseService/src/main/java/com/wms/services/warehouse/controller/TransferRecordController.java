package com.wms.services.warehouse.controller;

import com.wms.utilities.model.TransferRecordView;
import org.springframework.http.ResponseEntity;
import com.wms.utilities.model.TransferRecord;

public interface TransferRecordController {
    ResponseEntity<int[]> add(String accountBook, TransferRecord[] warehouses);
    ResponseEntity<TransferRecordView[]> find(String accountBook, String condStr);
}
