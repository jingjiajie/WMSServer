package com.wms.services.ledger.datestructures;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class TransferAccount {
    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public void setAccountPeriodId(Integer accountPeriodId) {
        this.accountPeriodId = accountPeriodId;
    }

    public void setOutaccountTitleId(int outaccountTitleId) {
        this.outaccountTitleId = outaccountTitleId;
    }

    public void setInaccountTitleId(int inaccountTitleId) {
        this.inaccountTitleId = inaccountTitleId;
    }

    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }

    public void setVoucherInfo(String voucherInfo) {
        this.voucherInfo = voucherInfo;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    private int warehouseId;
    private Integer personId;
    private Integer accountPeriodId;

    private int outaccountTitleId;
    private int inaccountTitleId;
    private BigDecimal changeAmount;

    private String voucherInfo;
    private String summary;

    public int getWarehouseId() {
        return warehouseId;
    }

    public Integer getPersonId() {
        return personId;
    }

    public Integer getAccountPeriodId() {
        return accountPeriodId;
    }

    public int getOutaccountTitleId() {
        return outaccountTitleId;
    }

    public int getInaccountTitleId() {
        return inaccountTitleId;
    }

    public BigDecimal getChangeAmount() {
        return changeAmount;
    }

    public String getVoucherInfo() {
        return voucherInfo;
    }

    public String getSummary() {
        return summary;
    }

    public String getComment() {
        return comment;
    }

    private String comment;
}
