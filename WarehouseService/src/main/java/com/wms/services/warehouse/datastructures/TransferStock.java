package com.wms.services.warehouse.datastructures;

public class TransferStock {

    public int getSourceStockRecordId() {
        return sourceStockRecordId;
    }

    public int getNewStockLocationId() {
        return newStockLocationId;
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

    public void setSourceStockRecordId(int sourceStockRecordId) {
        this.sourceStockRecordId = sourceStockRecordId;
    }

    public void setNewStockLocationId(int newStockLocationId) {
        this.newStockLocationId = newStockLocationId;
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

    public int sourceStockRecordId;
  public int newStockLocationId;
  public int amount;
  public String unit;
    public int unitAmount;
    public String relatedOrderNo;
}
