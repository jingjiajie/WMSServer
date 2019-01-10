package com.wms.services.ledger.datestructures;

import java.math.BigDecimal;
import java.sql.Timestamp;

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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    BigDecimal balance;

    public int getCurAccountTitleId() {
        return curAccountTitleId;
    }

    public void setCurAccountTitleId(int curAccountTitleId) {
        this.curAccountTitleId = curAccountTitleId;
    }

    int curAccountTitleId;

    public String getCurAccountTitleName() {
        return curAccountTitleName;
    }

    public void setCurAccountTitleName(String curAccountTitleName) {
        this.curAccountTitleName = curAccountTitleName;
    }

    String curAccountTitleName;

    public Timestamp getStartTime() {
        return startTime;
    }

    Timestamp startTime;

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    Timestamp endTime;
}
