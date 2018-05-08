package com.wms.services.warehouse.datastructures;

import java.math.BigDecimal;

public class TransferStock {

    public int getSupplyId() {
        return supplyId;
    }

    public int getNewStorageLocationId() {
        return newStorageLocationId;
    }

    public String getUnit() {
        return unit;
    }


    public String getRelatedOrderNo() {
        return relatedOrderNo;
    }

    public void setSupplyId(int supplyId) {
        this.supplyId = supplyId;
    }

    public void setNewStorageLocationId(int newStorageLocationId) {
        this.newStorageLocationId = newStorageLocationId;
    }



    public void setUnit(String unit) {
        this.unit = unit;
    }


    public void setRelatedOrderNo(String relatedOrderNo) {
        this.relatedOrderNo = relatedOrderNo;
    }

    public int supplyId;

    public int getSourceStorageLocationId() {
        return sourceStorageLocationId;
    }

    public void setSourceStorageLocationId(int sourceStorageLocationId) {
        this.sourceStorageLocationId = sourceStorageLocationId;
    }

    public int sourceStorageLocationId;
  public int newStorageLocationId;

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getUnitAmount() {
        return unitAmount;
    }

    public BigDecimal amount;

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setUnitAmount(BigDecimal unitAmount) {
        this.unitAmount = unitAmount;
    }

    public String unit;
    public BigDecimal unitAmount;
    public String relatedOrderNo;
}
