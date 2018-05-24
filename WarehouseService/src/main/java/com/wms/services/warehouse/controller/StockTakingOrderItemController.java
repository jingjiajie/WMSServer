package com.wms.services.warehouse.controller;

import com.wms.services.warehouse.datastructures.StockTakingOrderItemAdd;
import org.springframework.http.ResponseEntity;
import com.wms.utilities.model.StockTakingOrderItemView;
import com.wms.utilities.model.StockTakingOrderItem;
import com.wms.utilities.model.StockTakingOrderItemView;
public interface StockTakingOrderItemController {
    ResponseEntity<int[]> add(String accountBook, StockTakingOrderItem[] stockTakingOrderItems);
    void remove(String accountBook,String strIDs);
    void update(String accountBook,StockTakingOrderItem stockTakingOrderItems[]);
    ResponseEntity<StockTakingOrderItemView[]> find(String accountBook, String condStr);
    void addStockTakingOrderItemAll(String accountBook, StockTakingOrderItemAdd stockTakingOrderItemAdd);
    void addStockTakingOrderItemSingle(String accountBook,StockTakingOrderItemAdd stockTakingOrderItemAdd);
    void setRealAmount(String accountBook,StockTakingOrderItem stockTakingOrderItem);
    long findCount(String accountBook,String condStr);
}
