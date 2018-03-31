package com.wms.services.warehouse.controller;
import com.wms.services.warehouse.model.Supplier;
import org.springframework.http.ResponseEntity;
public interface SupplierController {
    ResponseEntity<int[]> add(String accountBook, Supplier[] suppliers);
    void update(String accountBook,Supplier suppliers[]);
    void remove(String accountBook,String strIDs);
    ResponseEntity<Supplier[]> find(String accountBook,String condStr);
}
