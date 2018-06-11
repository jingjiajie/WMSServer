package com.wms.services.warehouse.datastructures;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class StockRecordFindByTime {
    public Timestamp getEndTime() {
        return endTime;
    }

    public Integer getStorageLocationId() {
        return storageLocationId;
    }

    public Integer getSupplyId() {
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

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public void setStorageLocationId(Integer storageLocationId) {
        this.storageLocationId = storageLocationId;
    }

    public void setSupplyId(Integer supplyId) {
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

    private Timestamp endTime=new Timestamp(System.currentTimeMillis());
    private Integer storageLocationId;
    private Integer supplyId;
    private String unit="";
    private BigDecimal unitAmount;
    private String batchNo="";
}
