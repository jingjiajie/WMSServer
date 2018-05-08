package com.wms.services.warehouse.datastructures;

import java.math.BigDecimal;

public class TransferFinishItem {
    private int transferOrderItemId = -1;
    private boolean qualified = true;//是否完成移库
    private BigDecimal transferAmount;
    private String transferUnit;
    private BigDecimal transferUnitAmount;

    public void setTransferOrderItemId(int transferOrderItemId) {
        this.transferOrderItemId = transferOrderItemId;
    }

    public void setQualified(boolean qualified) {
        this.qualified = qualified;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public void setTransferUnit(String transferUnit) {
        this.transferUnit = transferUnit;
    }

    public void setTransferUnitAmount(BigDecimal transferUnitAmount) {
        this.transferUnitAmount = transferUnitAmount;
    }

    public void setTargetStorageLocationId(int targetStorageLocationId) {
        this.targetStorageLocationId = targetStorageLocationId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    private int targetStorageLocationId = -1;//（整单完成）目标库区ID

    public int getTransferOrderItemId() {
        return transferOrderItemId;
    }

    public boolean isQualified() {
        return qualified;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public String getTransferUnit() {
        return transferUnit;
    }

    public BigDecimal getTransferUnitAmount() {
        return transferUnitAmount;
    }

    public int getTargetStorageLocationId() {
        return targetStorageLocationId;
    }

    public int getPersonId() {
        return personId;
    }

    private int personId = -1;
}
