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

    public void setDebitAmount(List<BigDecimal> debitAmount) {
        this.debitAmount = debitAmount;
    }

    public void setCreditAmount(List<BigDecimal> creditAmount) {
        this.creditAmount = creditAmount;
    }

    public void setBalance(List<BigDecimal> balance) {
        Balance = balance;
    }

    String accountTitleNo;
    String accountTitleName;
    String accountTitleDependent;
    private List<BigDecimal> debitAmount;
    private List<BigDecimal> creditAmount;
    private List<BigDecimal> Balance;

    public String getAccountTitleNo() {
        return accountTitleNo;
    }

    public String getAccountTitleName() {
        return accountTitleName;
    }

    public String getAccountTitleDependent() {
        return accountTitleDependent;
    }

    public List<BigDecimal> getDebitAmount() {
        return debitAmount;
    }

    public List<BigDecimal> getCreditAmount() {
        return creditAmount;
    }

    public List<BigDecimal> getBalance() {
        return Balance;
    }


    public void setSummaryTime(Timestamp summaryTime) {
        this.summaryTime = summaryTime;
    }

    public Timestamp getSummaryTime() {
        return summaryTime;
    }

    Timestamp summaryTime;

}
