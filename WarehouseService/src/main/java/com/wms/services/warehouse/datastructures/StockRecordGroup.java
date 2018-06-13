package com.wms.services.warehouse.datastructures;

import com.wms.utilities.model.StockRecord;
import com.wms.utilities.model.StockRecordView;
import com.wms.utilities.model.StockRecordViewAndSum;
import org.omg.CORBA.OBJ_ADAPTER;

public class StockRecordGroup {
    public StockRecordViewAndSum[] getStockRecord() {
        return stockRecordViewAndSums;
    }

    public Object getGroup() {
        return group;
    }

    public void setStockRecords(StockRecordViewAndSum[] stockRecords) {
        this.stockRecordViewAndSums = stockRecords;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }

    private StockRecordViewAndSum[] stockRecordViewAndSums;
    private Integer group;
}

