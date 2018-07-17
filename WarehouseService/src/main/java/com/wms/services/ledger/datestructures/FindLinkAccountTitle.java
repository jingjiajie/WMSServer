package com.wms.services.ledger.datestructures;

import com.wms.utilities.model.AccountTitleView;

import java.util.List;

public class FindLinkAccountTitle {


    public String getCurAccountTitleNo() {
        return curAccountTitleNo;
    }

    public List<AccountTitleView> getAccountTitleViews() {
        return accountTitleViews;
    }

    public void setCurAccountTitleNo(String curAccountTitleNo) {
        this.curAccountTitleNo = curAccountTitleNo;
    }

    public void setAccountTitleViews(List<AccountTitleView> accountTitleViews) {
        this.accountTitleViews = accountTitleViews;
    }

    private List<AccountTitleView> accountTitleViews;
    private String curAccountTitleNo;
}
