package com.wms.services.warehouse.datastructures;

public class TransferStock {

    public int getSupplyId() {
        return supplyId;
    }

    public int getNewStorageLocationId() {
        return newStorageLocationId;
    }

    public int getAmount() {
        return amount;
    }

    public String getUnit() {
        return unit;
    }

    public int getUnitAmount() {
        return unitAmount;
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

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setUnitAmount(int unitAmount) {
        this.unitAmount = unitAmount;
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
  public int amount;
  public String unit;
    public int unitAmount;
    public String relatedOrderNo;
}
