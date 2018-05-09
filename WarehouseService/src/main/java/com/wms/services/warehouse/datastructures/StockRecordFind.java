package com.wms.services.warehouse.datastructures;

import com.wms.utilities.datastructures.Condition;
import javafx.beans.binding.IntegerExpression;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class StockRecordFind {

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    private Timestamp timestamp=new Timestamp(System.currentTimeMillis());

    public Integer getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(Integer supplyId) {
        this.supplyId = supplyId;
    }

    private Integer supplyId;


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

    public BigDecimal getUnitAomunt() {
        return unitAomunt;
    }

    public void setUnitAomunt(BigDecimal unitAomun) {
        this.unitAomunt = unitAomun;
    }

    private BigDecimal unitAomunt;

}
