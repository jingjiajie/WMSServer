package com.wms.services.warehouse.datastructures;

import java.math.BigDecimal;

public class ReturnAmount {
    public int getSupplyId() {
        return supplyId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setSupplyId(int supplyId) {
        this.supplyId = supplyId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    private int supplyId;
    private BigDecimal amount;
}
