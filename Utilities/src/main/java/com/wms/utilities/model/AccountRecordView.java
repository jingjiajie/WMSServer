package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
public class AccountRecordView {
    private int ownAccountTitleId;
    private Integer otherAccountTitleId;
    private int id;
    private int warehouseId;
    private String ownAccountTitleDependence;
    private String otherAccountTitleDependence;
    private Integer accountPeriodId;
    private String voucherInfo;
    private String summary;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private BigDecimal ownBalance;
    private BigDecimal otherBalance;
    private String comment;
    private Integer personId;
    private Timestamp recordingTime;
    private Timestamp serviceTime;
    private String ownAccountName;
    private String ownAccountNo;
    private String otherAccountName;
    private String otherAccountNo;
    private String accountPeriodName;
    private String warehouseName;
    private String personName;

    @Basic
    @Id
    @Column(name = "OwnAccountTitleID")
    public int getOwnAccountTitleId() {
        return ownAccountTitleId;
    }

    public void setOwnAccountTitleId(int ownAccountTitleId) {
        this.ownAccountTitleId = ownAccountTitleId;
    }

    @Basic
    @Column(name = "OtherAccountTitleID")
    public Integer getOtherAccountTitleId() {
        return otherAccountTitleId;
    }

    public void setOtherAccountTitleId(Integer otherAccountTitleId) {
        this.otherAccountTitleId = otherAccountTitleId;
    }

    @Basic
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
    @Column(name = "OwnAccountTitleDependence")
    public String getOwnAccountTitleDependence() {
        return ownAccountTitleDependence;
    }

    public void setOwnAccountTitleDependence(String ownAccountTitleDependence) {
        this.ownAccountTitleDependence = ownAccountTitleDependence;
    }

    @Basic
    @Column(name = "OtherAccountTitleDependence")
    public String getOtherAccountTitleDependence() {
        return otherAccountTitleDependence;
    }

    public void setOtherAccountTitleDependence(String otherAccountTitleDependence) {
        this.otherAccountTitleDependence = otherAccountTitleDependence;
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
    @Column(name = "OwnBalance")
    public BigDecimal getOwnBalance() {
        return ownBalance;
    }

    public void setOwnBalance(BigDecimal ownBalance) {
        this.ownBalance = ownBalance;
    }

    @Basic
    @Column(name = "OtherBalance")
    public BigDecimal getOtherBalance() {
        return otherBalance;
    }

    public void setOtherBalance(BigDecimal otherBalance) {
        this.otherBalance = otherBalance;
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
    @Column(name = "RecordingTime")
    public Timestamp getRecordingTime() {
        return recordingTime;
    }

    public void setRecordingTime(Timestamp recordingTime) {
        this.recordingTime = recordingTime;
    }

    @Basic
    @Column(name = "ServiceTime")
    public Timestamp getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(Timestamp serviceTime) {
        this.serviceTime = serviceTime;
    }

    @Basic
    @Column(name = "OwnAccountName")
    public String getOwnAccountName() {
        return ownAccountName;
    }

    public void setOwnAccountName(String ownAccountName) {
        this.ownAccountName = ownAccountName;
    }

    @Basic
    @Column(name = "OwnAccountNo")
    public String getOwnAccountNo() {
        return ownAccountNo;
    }

    public void setOwnAccountNo(String ownAccountNo) {
        this.ownAccountNo = ownAccountNo;
    }

    @Basic
    @Column(name = "OtherAccountName")
    public String getOtherAccountName() {
        return otherAccountName;
    }

    public void setOtherAccountName(String otherAccountName) {
        this.otherAccountName = otherAccountName;
    }

    @Basic
    @Column(name = "OtherAccountNo")
    public String getOtherAccountNo() {
        return otherAccountNo;
    }

    public void setOtherAccountNo(String otherAccountNo) {
        this.otherAccountNo = otherAccountNo;
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
    @Column(name = "WarehouseName")
    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    @Basic
    @Column(name = "PersonName")
    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountRecordView that = (AccountRecordView) o;

        if (ownAccountTitleId != that.ownAccountTitleId) return false;
        if (id != that.id) return false;
        if (warehouseId != that.warehouseId) return false;
        if (otherAccountTitleId != null ? !otherAccountTitleId.equals(that.otherAccountTitleId) : that.otherAccountTitleId != null)
            return false;
        if (ownAccountTitleDependence != null ? !ownAccountTitleDependence.equals(that.ownAccountTitleDependence) : that.ownAccountTitleDependence != null)
            return false;
        if (otherAccountTitleDependence != null ? !otherAccountTitleDependence.equals(that.otherAccountTitleDependence) : that.otherAccountTitleDependence != null)
            return false;
        if (accountPeriodId != null ? !accountPeriodId.equals(that.accountPeriodId) : that.accountPeriodId != null)
            return false;
        if (voucherInfo != null ? !voucherInfo.equals(that.voucherInfo) : that.voucherInfo != null) return false;
        if (summary != null ? !summary.equals(that.summary) : that.summary != null) return false;
        if (debitAmount != null ? !debitAmount.equals(that.debitAmount) : that.debitAmount != null) return false;
        if (creditAmount != null ? !creditAmount.equals(that.creditAmount) : that.creditAmount != null) return false;
        if (ownBalance != null ? !ownBalance.equals(that.ownBalance) : that.ownBalance != null) return false;
        if (otherBalance != null ? !otherBalance.equals(that.otherBalance) : that.otherBalance != null) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        if (personId != null ? !personId.equals(that.personId) : that.personId != null) return false;
        if (recordingTime != null ? !recordingTime.equals(that.recordingTime) : that.recordingTime != null)
            return false;
        if (serviceTime != null ? !serviceTime.equals(that.serviceTime) : that.serviceTime != null) return false;
        if (ownAccountName != null ? !ownAccountName.equals(that.ownAccountName) : that.ownAccountName != null)
            return false;
        if (ownAccountNo != null ? !ownAccountNo.equals(that.ownAccountNo) : that.ownAccountNo != null) return false;
        if (otherAccountName != null ? !otherAccountName.equals(that.otherAccountName) : that.otherAccountName != null)
            return false;
        if (otherAccountNo != null ? !otherAccountNo.equals(that.otherAccountNo) : that.otherAccountNo != null)
            return false;
        if (accountPeriodName != null ? !accountPeriodName.equals(that.accountPeriodName) : that.accountPeriodName != null)
            return false;
        if (warehouseName != null ? !warehouseName.equals(that.warehouseName) : that.warehouseName != null)
            return false;
        if (personName != null ? !personName.equals(that.personName) : that.personName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ownAccountTitleId;
        result = 31 * result + (otherAccountTitleId != null ? otherAccountTitleId.hashCode() : 0);
        result = 31 * result + id;
        result = 31 * result + warehouseId;
        result = 31 * result + (ownAccountTitleDependence != null ? ownAccountTitleDependence.hashCode() : 0);
        result = 31 * result + (otherAccountTitleDependence != null ? otherAccountTitleDependence.hashCode() : 0);
        result = 31 * result + (accountPeriodId != null ? accountPeriodId.hashCode() : 0);
        result = 31 * result + (voucherInfo != null ? voucherInfo.hashCode() : 0);
        result = 31 * result + (summary != null ? summary.hashCode() : 0);
        result = 31 * result + (debitAmount != null ? debitAmount.hashCode() : 0);
        result = 31 * result + (creditAmount != null ? creditAmount.hashCode() : 0);
        result = 31 * result + (ownBalance != null ? ownBalance.hashCode() : 0);
        result = 31 * result + (otherBalance != null ? otherBalance.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (personId != null ? personId.hashCode() : 0);
        result = 31 * result + (recordingTime != null ? recordingTime.hashCode() : 0);
        result = 31 * result + (serviceTime != null ? serviceTime.hashCode() : 0);
        result = 31 * result + (ownAccountName != null ? ownAccountName.hashCode() : 0);
        result = 31 * result + (ownAccountNo != null ? ownAccountNo.hashCode() : 0);
        result = 31 * result + (otherAccountName != null ? otherAccountName.hashCode() : 0);
        result = 31 * result + (otherAccountNo != null ? otherAccountNo.hashCode() : 0);
        result = 31 * result + (accountPeriodName != null ? accountPeriodName.hashCode() : 0);
        result = 31 * result + (warehouseName != null ? warehouseName.hashCode() : 0);
        result = 31 * result + (personName != null ? personName.hashCode() : 0);
        return result;
    }
}
