package com.wms.services.warehouse.datastructures;

import com.wms.utilities.model.StockRecordView;

public class StockRecordGroup {
    public StockRecordView getStockRecordView() {
        return stockRecordView;
    }

    public String getGroup() {
        return group;
    }

    public void setStockRecordView(StockRecordView stockRecordView) {
        this.stockRecordView = stockRecordView;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    private StockRecordView stockRecordView;
    private String group;
}
