package com.wms.services.warehouse.datastructures;

import com.wms.utilities.datastructures.Condition;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class StockRecordFind {

    public Timestamp getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Timestamp timeEnd) {
        this.timeEnd = timeEnd;
    }

    private Timestamp timeEnd =new Timestamp(System.currentTimeMillis());

    public Integer getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(Integer supplyId) {
        this.supplyId = supplyId;
    }

    private Integer supplyId;

    public Timestamp getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Timestamp timeStart) {
        this.timeStart = timeStart;
    }

    private Timestamp timeStart;

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    private Integer warehouseId;


    private String unit="";


    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    Condition condition;

    public Integer getStorageLocationId() {
        return storageLocationId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setStorageLocationId(Integer storageLocationId) {
        this.storageLocationId = storageLocationId;
    }

    private Integer storageLocationId;

    public BigDecimal getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(BigDecimal unitAomun) {
        this.unitAmount = unitAomun;
    }

    private BigDecimal unitAmount;

    public String getReturnMode() {
        return returnMode;
    }

    public void setReturnMode(String returnMode) {
        this.returnMode = returnMode;
    }

    private String returnMode="newest";

    public Timestamp getInventaryDate() {
        return inventaryDate;
    }

    public void setInventaryDate(Timestamp inventaryDate) {
        this.inventaryDate = inventaryDate;
    }

    private Timestamp inventaryDate;

}
