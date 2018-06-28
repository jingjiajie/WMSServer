package com.wms.services.warehouse.datastructures;

import org.omg.CORBA.INTERNAL;

import java.math.BigDecimal;
import java.sql.Timestamp;

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

    private int supplyId;

    public int getSourceStorageLocationId() {
        return sourceStorageLocationId;
    }

    public void setSourceStorageLocationId(int sourceStorageLocationId) {
        this.sourceStorageLocationId = sourceStorageLocationId;
    }

    private int sourceStorageLocationId;

    private int newStorageLocationId;

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

    private String unit;

    private BigDecimal unitAmount;

    public String relatedOrderNo;

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    private String batchNo;

    public Timestamp getInventoryDate() {
        return inventoryDate;
    }

    public void setInventoryDate(Timestamp inventoryDate) {
        this.inventoryDate = inventoryDate;
    }

    private Timestamp inventoryDate;

    public BigDecimal getModifyAvailableAmount() {
        return modifyAvailableAmount;
    }

    public void setModifyAvailableAmount(BigDecimal modifyAvailableAmount) {
        this.modifyAvailableAmount = modifyAvailableAmount;
    }

    private BigDecimal modifyAvailableAmount;

    public String getNewUnit() {
        return newUnit;
    }

    public BigDecimal getNewUnitAmount() {
        return newUnitAmount;
    }

    public void setNewUnit(String newUnit) {
        this.newUnit = newUnit;
    }

    public void setNewUnitAmount(BigDecimal newUnitAmount) {
        this.newUnitAmount = newUnitAmount;
    }

    private String newUnit;

    private BigDecimal newUnitAmount;

    public int getState() {
        return State;
    }

    public void setState(int State) {
        this.State = State;
    }

    private  int State=-1;

    public int getNewState() {
        return newState;
    }

    public void setNewState(int newState) {
        this.newState = newState;
    }

    private int newState=-1;

    private final int STATE_DEFAULT_DEPENDENT=-1;

    public static final int WAITING_FOR_INSPECTION =0;

    public static final int UNQUALIFIED=1;

    public static final int QUALIFIED=2;
}
