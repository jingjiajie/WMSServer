package com.wms.services.ledger.datestructures;

public class TreeViewData {


    public String getAccountTitleName() {
        return accountTitleName;
    }

    public int getAccountTitleId() {
        return accountTitleId;
    }

    public int getParentAccountTitleId() {
        return parentAccountTitleId;
    }

    public void setAccountTitleName(String accountTitleName) {
        this.accountTitleName = accountTitleName;
    }

    public void setAccountTitleId(int accountTitleId) {
        this.accountTitleId = accountTitleId;
    }

    public void setParentAccountTitleId(int parentAccountTitleId) {
        this.parentAccountTitleId = parentAccountTitleId;
    }

    public String getAccountTitleNo() {
        return accountTitleNo;
    }

    public void setAccountTitleNo(String accountTitleNo) {
        this.accountTitleNo = accountTitleNo;
    }

    String accountTitleNo;
    String accountTitleName;
    int accountTitleId;
    int parentAccountTitleId;
}
