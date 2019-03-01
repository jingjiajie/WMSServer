package com.wms.services.warehouse.datastructures;

import java.math.BigDecimal;

public class ReturnAmount {
    public int getSupplyId() {
        return supplyId;
    }


    public void setSupplyId(int supplyId) {
        this.supplyId = supplyId;
    }


    private int supplyId;

    public void setAmountQualified(BigDecimal amountQualified) {
        this.amountQualified = amountQualified;
    }

    public void setAmountUnqualified(BigDecimal amountUnqualified) {
        this.amountUnqualified = amountUnqualified;
    }

    private BigDecimal amountQualified;

    public BigDecimal getAmountQualified() {
        return amountQualified;
    }

    public BigDecimal getAmountUnqualified() {
        return amountUnqualified;
    }

    private BigDecimal amountUnqualified;
}
