package com.wms.services.warehouse.controller;
import com.wms.services.warehouse.datastructures.SupplierAmount;
import com.wms.utilities.model.Supplier;
import com.wms.utilities.model.SupplierView;
import org.springframework.http.ResponseEntity;

public interface SupplierController {
    ResponseEntity<int[]> add(String accountBook, Supplier[] suppliers);
    void update(String accountBook,Supplier[] suppliers);
    void updateHistorySave(String accountBook,Supplier[] suppliers);
    void remove(String accountBook,String strIDs);
    ResponseEntity<SupplierView[]> find(String accountBook, String condStr);
    ResponseEntity<SupplierView[]> findNew(String accountBook, String condStr);
    ResponseEntity<SupplierView[]> findHistory(String accountBook, String condStr);
    long findCount(String accountBook,String condStr);
    long findCountNew(String accountBook,String condStr);
    long findCountHistory(String accountBook,String condStr);
    SupplierAmount[] supplierRemind(String accountBook, int supplierId);
}
