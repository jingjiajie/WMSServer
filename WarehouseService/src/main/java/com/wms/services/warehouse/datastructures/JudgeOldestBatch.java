package com.wms.services.warehouse.datastructures;

import java.math.BigDecimal;

public class JudgeOldestBatch {
    public int getSupplyId() {
        return supplyId;
    }

    public String getUnit() {
        return unit;
    }

    public BigDecimal getUnitAmount() {
        return unitAmount;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public int getState() {
        return state;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setSupplyId(int supplyId) {
        this.supplyId = supplyId;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setUnitAmount(BigDecimal unitAmount) {
        this.unitAmount = unitAmount;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    int supplyId;
    String unit;
    BigDecimal unitAmount;
    String batchNo;
    int state=-1;
    int warehouseId;
}
