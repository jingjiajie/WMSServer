package com.wms.services.warehouse.datastructures;

import com.wms.utilities.model.StockRecordViewAndSum;

public class StockRecordViewAndSumGroupBySupplyId {
    public StockRecordViewAndSum[] getStockRecord() {
        return stockRecordViewAndSums;
    }

    public Object getSupplyId() {
        return supplyId;
    }

    public void setStockRecords(StockRecordViewAndSum[] stockRecords) {
        this.stockRecordViewAndSums = stockRecords;
    }

    public void setSupplyId(Integer supplyId) {
        this.supplyId = supplyId;
    }

    private StockRecordViewAndSum[] stockRecordViewAndSums;
    private Integer supplyId;
}

