package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.sql.Timestamp;

@Entity
public class PayNoteView {
    private int id;
    private int warehouseId;
    private int salaryPeriodId;
    private String no;
    private Integer accountTitlePayableId;
    private Integer accountTitlePaidId;
    private String description;
    private int createPersonId;
    private Timestamp createTime;
    private String createPersonName;
    private String accountTitlePayableName;
    private String accountTitlePayableNo;
    private String accountTitlePaidName;
    private String accountTitlePaidNo;
    private String warehouseName;

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
    @Column(name = "SalaryPeriodID")
    public int getSalaryPeriodId() {
        return salaryPeriodId;
    }

    public void setSalaryPeriodId(int salaryPeriodId) {
        this.salaryPeriodId = salaryPeriodId;
    }

    @Basic
    @Column(name = "No")
    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    @Basic
    @Column(name = "AccountTitlePayableID")
    public Integer getAccountTitlePayableId() {
        return accountTitlePayableId;
    }

    public void setAccountTitlePayableId(Integer accountTitlePayableId) {
        this.accountTitlePayableId = accountTitlePayableId;
    }

    @Basic
    @Column(name = "AccountTitlePaidID")
    public Integer getAccountTitlePaidId() {
        return accountTitlePaidId;
    }

    public void setAccountTitlePaidId(Integer accountTitlePaidId) {
        this.accountTitlePaidId = accountTitlePaidId;
    }

    @Basic
    @Column(name = "Description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "CreatePersonID")
    public int getCreatePersonId() {
        return createPersonId;
    }

    public void setCreatePersonId(int createPersonId) {
        this.createPersonId = createPersonId;
    }

    @Basic
    @Column(name = "CreateTime")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "CreatePersonName")
    public String getCreatePersonName() {
        return createPersonName;
    }

    public void setCreatePersonName(String createPersonName) {
        this.createPersonName = createPersonName;
    }

    @Basic
    @Column(name = "AccountTitlePayableName")
    public String getAccountTitlePayableName() {
        return accountTitlePayableName;
    }

    public void setAccountTitlePayableName(String accountTitlePayableName) {
        this.accountTitlePayableName = accountTitlePayableName;
    }

    @Basic
    @Column(name = "AccountTitlePayableNo")
    public String getAccountTitlePayableNo() {
        return accountTitlePayableNo;
    }

    public void setAccountTitlePayableNo(String accountTitlePayableNo) {
        this.accountTitlePayableNo = accountTitlePayableNo;
    }

    @Basic
    @Column(name = "AccountTitlePaidName")
    public String getAccountTitlePaidName() {
        return accountTitlePaidName;
    }

    public void setAccountTitlePaidName(String accountTitlePaidName) {
        this.accountTitlePaidName = accountTitlePaidName;
    }

    @Basic
    @Column(name = "AccountTitlePaidNo")
    public String getAccountTitlePaidNo() {
        return accountTitlePaidNo;
    }

    public void setAccountTitlePaidNo(String accountTitlePaidNo) {
        this.accountTitlePaidNo = accountTitlePaidNo;
    }

    @Basic
    @Column(name = "WarehouseName")
    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PayNoteView that = (PayNoteView) o;

        if (id != that.id) return false;
        if (warehouseId != that.warehouseId) return false;
        if (salaryPeriodId != that.salaryPeriodId) return false;
        if (createPersonId != that.createPersonId) return false;
        if (no != null ? !no.equals(that.no) : that.no != null) return false;
        if (accountTitlePayableId != null ? !accountTitlePayableId.equals(that.accountTitlePayableId) : that.accountTitlePayableId != null)
            return false;
        if (accountTitlePaidId != null ? !accountTitlePaidId.equals(that.accountTitlePaidId) : that.accountTitlePaidId != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (createPersonName != null ? !createPersonName.equals(that.createPersonName) : that.createPersonName != null)
            return false;
        if (accountTitlePayableName != null ? !accountTitlePayableName.equals(that.accountTitlePayableName) : that.accountTitlePayableName != null)
            return false;
        if (accountTitlePayableNo != null ? !accountTitlePayableNo.equals(that.accountTitlePayableNo) : that.accountTitlePayableNo != null)
            return false;
        if (accountTitlePaidName != null ? !accountTitlePaidName.equals(that.accountTitlePaidName) : that.accountTitlePaidName != null)
            return false;
        if (accountTitlePaidNo != null ? !accountTitlePaidNo.equals(that.accountTitlePaidNo) : that.accountTitlePaidNo != null)
            return false;
        if (warehouseName != null ? !warehouseName.equals(that.warehouseName) : that.warehouseName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + warehouseId;
        result = 31 * result + salaryPeriodId;
        result = 31 * result + (no != null ? no.hashCode() : 0);
        result = 31 * result + (accountTitlePayableId != null ? accountTitlePayableId.hashCode() : 0);
        result = 31 * result + (accountTitlePaidId != null ? accountTitlePaidId.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + createPersonId;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (createPersonName != null ? createPersonName.hashCode() : 0);
        result = 31 * result + (accountTitlePayableName != null ? accountTitlePayableName.hashCode() : 0);
        result = 31 * result + (accountTitlePayableNo != null ? accountTitlePayableNo.hashCode() : 0);
        result = 31 * result + (accountTitlePaidName != null ? accountTitlePaidName.hashCode() : 0);
        result = 31 * result + (accountTitlePaidNo != null ? accountTitlePaidNo.hashCode() : 0);
        result = 31 * result + (warehouseName != null ? warehouseName.hashCode() : 0);
        return result;
    }
}
