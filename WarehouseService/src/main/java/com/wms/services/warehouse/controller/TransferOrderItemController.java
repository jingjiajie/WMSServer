package com.wms.services.warehouse.controller;


import org.springframework.http.ResponseEntity;

public interface TransferOrderItemController {
    ResponseEntity<int[]> add(String accountBook, TransferOrderItem[] items);
    void remove(String accountBook,String strIDs);
    void update(String accountBook,TransferOrderItem transferOrderItems[]);
    TransferOrderItemView[] find(String accountBook, String condStr);
    long findCount(String accountBook,String condStr);
}
