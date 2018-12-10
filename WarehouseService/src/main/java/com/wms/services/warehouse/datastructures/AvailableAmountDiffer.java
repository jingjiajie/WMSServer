package com.wms.services.warehouse.datastructures;

import java.math.BigDecimal;

public class AvailableAmountDiffer {
    public int getI() {
        return i;
    }

    public BigDecimal getAmountDiffer() {
        return amountDiffer;
    }

    public void setI(int i) {
        this.i = i;
    }

    public void setAmountDiffer(BigDecimal amountDiffer) {
        this.amountDiffer = amountDiffer;
    }

    int i=-1;
    BigDecimal amountDiffer=new BigDecimal(-1);
}
