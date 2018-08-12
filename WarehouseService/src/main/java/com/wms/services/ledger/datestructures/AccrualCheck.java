package com.wms.services.ledger.datestructures;

import java.math.BigDecimal;

public class AccrualCheck {
    int warehouseId;
    int personId;
    int curAccountPeriodId;

    public int getCurAccountPeriodId() {
        return curAccountPeriodId;
    }

    public void setCurAccountPeriodId(int curAccountPeriodId) {
        this.curAccountPeriodId = curAccountPeriodId;
    }



    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    BigDecimal creditAmount;

    public int getWarehouseId() {
        return warehouseId;
    }

    public int getPersonId() {
        return personId;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    BigDecimal debitAmount;

}
