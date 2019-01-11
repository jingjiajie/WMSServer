package com.wms.services.ledger.datestructures;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class SummaryAccountRecord {
    public void setAccountTitleNo(String accountTitleNo) {
        this.accountTitleNo = accountTitleNo;
    }

    public void setAccountTitleName(String accountTitleName) {
        this.accountTitleName = accountTitleName;
    }

    public void setAccountTitleDependent(String accountTitleDependent) {
        this.accountTitleDependent = accountTitleDependent;
    }



    String accountTitleNo;
    String accountTitleName;
    String accountTitleDependent;

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public BigDecimal getBalance() {
        return Balance;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public void setBalance(BigDecimal balance) {
        Balance = balance;
    }

    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private BigDecimal Balance;

    public String getAccountTitleNo() {
        return accountTitleNo;
    }

    public String getAccountTitleName() {
        return accountTitleName;
    }

    public String getAccountTitleDependent() {
        return accountTitleDependent;
    }




    public Timestamp getSummaryStartTime() {
        return summaryStartTime;
    }

    public Timestamp getSummaryEndTime() {
        return summaryEndTime;
    }

    Timestamp summaryStartTime;

    public void setSummaryStartTime(Timestamp summaryStartTime) {
        this.summaryStartTime = summaryStartTime;
    }

    public void setSummaryEndTime(Timestamp summaryEndTime) {
        this.summaryEndTime = summaryEndTime;
    }

    Timestamp summaryEndTime;

}
