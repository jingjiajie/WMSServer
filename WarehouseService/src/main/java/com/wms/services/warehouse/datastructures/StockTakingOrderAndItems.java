package com.wms.services.warehouse.datastructures;

import com.wms.utilities.model.StockTakingOrder;
import com.wms.utilities.model.StockTakingOrderItem;
import com.wms.utilities.model.StockTakingOrderItemView;
import com.wms.utilities.model.StockTakingOrderView;

import java.util.List;

public class StockTakingOrderAndItems {
    public StockTakingOrderView getStockTakingOrderView() {
        return stockTakingOrderView;
    }

    public List<StockTakingOrderItemView> getStockTakingOrderItems() {
        return stockTakingOrderItems;
    }

    public void setStockTakingOrderView(StockTakingOrderView stockTakingOrderView) {
        this.stockTakingOrderView = stockTakingOrderView;
    }

    public void setStockTakingOrderItems(List<StockTakingOrderItemView> stockTakingOrderItems) {
        this.stockTakingOrderItems = stockTakingOrderItems;
    }

    private StockTakingOrderView stockTakingOrderView;
    private List<StockTakingOrderItemView> stockTakingOrderItems;
}
