package com.wms.services.warehouse.controller;

import com.wms.services.warehouse.datastructures.StockTakingItemDelete;
import com.wms.utilities.model.StockTakingOrder;
import com.wms.utilities.model.StockTakingOrderView;
import org.springframework.http.ResponseEntity;

public interface StockTakingOrderController {
    ResponseEntity<int[]> add(String accountBook, StockTakingOrder[] stockTakingOrders);
    void remove(String accountBook,String strIDs);
    void update(String accountBook,StockTakingOrder stockTakingOrders[]);
    ResponseEntity<StockTakingOrderView[]> find(String accountBook, String condStr);
    long findCount(String accountBook,String condStr);
}
