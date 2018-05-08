package com.wms.services.warehouse.datastructures;

import com.wms.utilities.datastructures.Condition;

import java.sql.Timestamp;

public class StockRecordFind {

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    private Timestamp timestamp;

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    Condition condition;

}
