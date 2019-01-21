package com.wms.services.warehouse.datastructures;

import java.math.BigDecimal;

public class SupplierAmount {
    public String getSupplierName() {
        return SupplierName;
    }

    public String getMaterialName() {
        return MaterialName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setSupplierName(String supplierName) {
        SupplierName = supplierName;
    }

    public void setMaterialName(String materialName) {
        MaterialName = materialName;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    private String SupplierName;
    private String MaterialName;
    private BigDecimal amount;

    public void setSupplyId(int supplyId) {
        this.supplyId = supplyId;
    }

    public int getSupplyId() {
        return supplyId;
    }

    private int supplyId;

    public BigDecimal getAmountNeed() {
        return amountNeed;
    }

    public void setAmountNeed(BigDecimal amountNeed) {
        this.amountNeed = amountNeed;
    }

    private BigDecimal amountNeed;
}
