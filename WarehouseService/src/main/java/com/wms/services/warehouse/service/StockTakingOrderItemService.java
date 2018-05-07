package com.wms.services.warehouse.service;

import com.wms.services.warehouse.datastructures.StockTakingOrderItemAdd;
import com.wms.utilities.model.StockTakingOrderItem;
import com.wms.utilities.model.StockTakingOrderItemView;
import com.wms.utilities.service.BaseService;

public interface StockTakingOrderItemService
        extends BaseService<StockTakingOrderItem, StockTakingOrderItemView> {
    public void addStockTakingOrderItemSingle(String accountBook, StockTakingOrderItemAdd stockTakingOrderItemAdd);
}