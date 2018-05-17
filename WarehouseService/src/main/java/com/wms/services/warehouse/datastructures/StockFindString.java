package com.wms.services.warehouse.datastructures;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class StockFindString {
    String unit;
    BigDecimal unitAmount;

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setUnitAmount(BigDecimal unitAmount) {
        this.unitAmount = unitAmount;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    Timestamp time;

    public String getUnit() {
        return unit;
    }

    public BigDecimal getUnitAmount() {
        return unitAmount;
    }

    public Timestamp getTime() {
        return time;
    }

    public String getBatchNo() {
        return batchNo;
    }

    String batchNo;



}
