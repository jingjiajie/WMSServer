package com.wms.services.warehouse.controller;


import com.wms.services.warehouse.model.WarehouseEntry;
import com.wms.services.warehouse.model.WarehouseEntryItem;
import com.wms.services.warehouse.model.WarehouseEntryItemView;
import com.wms.services.warehouse.model.WarehouseEntryView;
import com.wms.utilities.datastructures.Condition;
import org.springframework.http.ResponseEntity;

public interface WarehouseEntryItemController {

        ResponseEntity<int[]> add(String accountBook, WarehouseEntryItem[] items);

        void update(String accountBook, WarehouseEntryItem[] items);

        void remove(String accountBook, String strIDs);

        ResponseEntity<WarehouseEntryItemView[]> find(String accountBook, String strCond);

}