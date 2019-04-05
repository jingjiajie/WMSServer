package com.wms.services.warehouse.datastructures;

import java.math.BigDecimal;

public class JudgeAmount {
    public int getI() {
        return i;
    }

    public BigDecimal getLastIRemainAmount() {
        return lastIRemainAmount;
    }

    public void setI(int i) {
        this.i = i;
    }

    public void setLastIRemainAmount(BigDecimal lastIRemainAmount) {
        this.lastIRemainAmount = lastIRemainAmount;
    }

    int i=-1;
    BigDecimal lastIRemainAmount=new BigDecimal(-1);
}
