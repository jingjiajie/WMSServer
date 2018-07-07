package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
public class AccountRecordView {
    private int id;
    private int warehouseId;
    private int accountTitleId;
    private Integer accountPeriodId;
    private String voucherInfo;
    private String summary;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private BigDecimal balance;
    private String comment;
    private Integer personId;
    private Timestamp time;
    private String personName;
    private String warehouseName;
    private String accountPeriodName;
    private String accountTitleName;
    private String accountTitleNo;

    @Basic
    @Id
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "WarehouseID")
    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    @Basic
    @Column(name = "AccountTitleID")
    public int getAccountTitleId() {
        return accountTitleId;
    }

    public void setAccountTitleId(int accountTitleId) {
        this.accountTitleId = accountTitleId;
    }

    @Basic
    @Column(name = "AccountPeriodID")
    public Integer getAccountPeriodId() {
        return accountPeriodId;
    }

    public void setAccountPeriodId(Integer accountPeriodId) {
        this.accountPeriodId = accountPeriodId;
    }

    @Basic
    @Column(name = "VoucherInfo")
    public String getVoucherInfo() {
        return voucherInfo;
    }

    public void setVoucherInfo(String voucherInfo) {
        this.voucherInfo = voucherInfo;
    }

    @Basic
    @Column(name = "Summary")
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Basic
    @Column(name = "DebitAmount")
    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    @Basic
    @Column(name = "CreditAmount")
    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    @Basic
    @Column(name = "Balance")
    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Basic
    @Column(name = "Comment")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Basic
    @Column(name = "PersonID")
    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    @Basic
    @Column(name = "Time")
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Basic
    @Column(name = "PersonName")
    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    @Basic
    @Column(name = "WarehouseName")
    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    @Basic
    @Column(name = "AccountPeriodName")
    public String getAccountPeriodName() {
        return accountPeriodName;
    }

    public void setAccountPeriodName(String accountPeriodName) {
        this.accountPeriodName = accountPeriodName;
    }

    @Basic
    @Column(name = "AccountTitleName")
    public String getAccountTitleName() {
        return accountTitleName;
    }

    public void setAccountTitleName(String accountTitleName) {
        this.accountTitleName = accountTitleName;
    }

    @Basic
    @Column(name = "AccountTitleNo")
    public String getAccountTitleNo() {
        return accountTitleNo;
    }

    public void setAccountTitleNo(String accountTitleNo) {
        this.accountTitleNo = accountTitleNo;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        AccountRecordView that = (AccountRecordView) object;

        if (id != that.id) return false;
        if (warehouseId != that.warehouseId) return false;
        if (accountTitleId != that.accountTitleId) return false;
        if (accountPeriodId != null ? !accountPeriodId.equals(that.accountPeriodId) : that.accountPeriodId != null)
            return false;
        if (voucherInfo != null ? !voucherInfo.equals(that.voucherInfo) : that.voucherInfo != null) return false;
        if (summary != null ? !summary.equals(that.summary) : that.summary != null) return false;
        if (debitAmount != null ? !debitAmount.equals(that.debitAmount) : that.debitAmount != null) return false;
        if (creditAmount != null ? !creditAmount.equals(that.creditAmount) : that.creditAmount != null) return false;
        if (balance != null ? !balance.equals(that.balance) : that.balance != null) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        if (personId != null ? !personId.equals(that.personId) : that.personId != null) return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (personName != null ? !personName.equals(that.personName) : that.personName != null) return false;
        if (warehouseName != null ? !warehouseName.equals(that.warehouseName) : that.warehouseName != null)
            return false;
        if (accountPeriodName != null ? !accountPeriodName.equals(that.accountPeriodName) : that.accountPeriodName != null)
            return false;
        if (accountTitleName != null ? !accountTitleName.equals(that.accountTitleName) : that.accountTitleName != null)
            return false;
        if (accountTitleNo != null ? !accountTitleNo.equals(that.accountTitleNo) : that.accountTitleNo != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + warehouseId;
        result = 31 * result + accountTitleId;
        result = 31 * result + (accountPeriodId != null ? accountPeriodId.hashCode() : 0);
        result = 31 * result + (voucherInfo != null ? voucherInfo.hashCode() : 0);
        result = 31 * result + (summary != null ? summary.hashCode() : 0);
        result = 31 * result + (debitAmount != null ? debitAmount.hashCode() : 0);
        result = 31 * result + (creditAmount != null ? creditAmount.hashCode() : 0);
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (personId != null ? personId.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (personName != null ? personName.hashCode() : 0);
        result = 31 * result + (warehouseName != null ? warehouseName.hashCode() : 0);
        result = 31 * result + (accountPeriodName != null ? accountPeriodName.hashCode() : 0);
        result = 31 * result + (accountTitleName != null ? accountTitleName.hashCode() : 0);
        result = 31 * result + (accountTitleNo != null ? accountTitleNo.hashCode() : 0);
        return result;
    }
}
