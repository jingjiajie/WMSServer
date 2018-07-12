package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class PayNoteView {
    private int id;
    private int warehouseId;
    private int salaryPeriodId;
    private String no;
    private Integer accountTitlePayableId;
    private Integer accountTitlePropertyId;
    private String description;
    private int createPersonId;
    private Timestamp createTime;
    private Integer accountTitleExpenseId;
    private String createPersonName;
    private String warehouseName;
    private String accountTitleExpenseName;
    private String accountTitleExpenseNo;
    private String accountTitlePayableName;
    private String accountTitlePayableNo;
    private String accountTitlePropertyName;
    private String accountTitlePropertyNo;
    private String salaryPeriodName;
    private int state;
    private Integer taxId;
    private String taxName;
    private String taxNo;

    @Basic
    @Column(name = "ID")
    @Id
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
    @Column(name = "AccountTitlePropertyID")
    public Integer getAccountTitlePropertyId() {
        return accountTitlePropertyId;
    }

    public void setAccountTitlePropertyId(Integer accountTitlePropertyId) {
        this.accountTitlePropertyId = accountTitlePropertyId;
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
    @Column(name = "AccountTitleExpenseID")
    public Integer getAccountTitleExpenseId() {
        return accountTitleExpenseId;
    }

    public void setAccountTitleExpenseId(Integer accountTitleExpenseId) {
        this.accountTitleExpenseId = accountTitleExpenseId;
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
    @Column(name = "WarehouseName")
    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    @Basic
    @Column(name = "AccountTitleExpenseName")
    public String getAccountTitleExpenseName() {
        return accountTitleExpenseName;
    }

    public void setAccountTitleExpenseName(String accountTitleExpenseName) {
        this.accountTitleExpenseName = accountTitleExpenseName;
    }

    @Basic
    @Column(name = "AccountTitleExpenseNo")
    public String getAccountTitleExpenseNo() {
        return accountTitleExpenseNo;
    }

    public void setAccountTitleExpenseNo(String accountTitleExpenseNo) {
        this.accountTitleExpenseNo = accountTitleExpenseNo;
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
    @Column(name = "AccountTitlePropertyName")
    public String getAccountTitlePropertyName() {
        return accountTitlePropertyName;
    }

    public void setAccountTitlePropertyName(String accountTitlePropertyName) {
        this.accountTitlePropertyName = accountTitlePropertyName;
    }

    @Basic
    @Column(name = "AccountTitlePropertyNo")
    public String getAccountTitlePropertyNo() {
        return accountTitlePropertyNo;
    }

    public void setAccountTitlePropertyNo(String accountTitlePropertyNo) {
        this.accountTitlePropertyNo = accountTitlePropertyNo;
    }

    @Basic
    @Column(name = "SalaryPeriodName")
    public String getSalaryPeriodName() {
        return salaryPeriodName;
    }

    public void setSalaryPeriodName(String salaryPeriodName) {
        this.salaryPeriodName = salaryPeriodName;
    }

    @Basic
    @Column(name = "State")
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Basic
    @Column(name = "TaxID")
    public Integer getTaxId() {
        return taxId;
    }

    public void setTaxId(Integer taxId) {
        this.taxId = taxId;
    }

    @Basic
    @Column(name = "TaxName")
    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    @Basic
    @Column(name = "TaxNo")
    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
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
        if (state != that.state) return false;
        if (no != null ? !no.equals(that.no) : that.no != null) return false;
        if (accountTitlePayableId != null ? !accountTitlePayableId.equals(that.accountTitlePayableId) : that.accountTitlePayableId != null)
            return false;
        if (accountTitlePropertyId != null ? !accountTitlePropertyId.equals(that.accountTitlePropertyId) : that.accountTitlePropertyId != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (accountTitleExpenseId != null ? !accountTitleExpenseId.equals(that.accountTitleExpenseId) : that.accountTitleExpenseId != null)
            return false;
        if (createPersonName != null ? !createPersonName.equals(that.createPersonName) : that.createPersonName != null)
            return false;
        if (warehouseName != null ? !warehouseName.equals(that.warehouseName) : that.warehouseName != null)
            return false;
        if (accountTitleExpenseName != null ? !accountTitleExpenseName.equals(that.accountTitleExpenseName) : that.accountTitleExpenseName != null)
            return false;
        if (accountTitleExpenseNo != null ? !accountTitleExpenseNo.equals(that.accountTitleExpenseNo) : that.accountTitleExpenseNo != null)
            return false;
        if (accountTitlePayableName != null ? !accountTitlePayableName.equals(that.accountTitlePayableName) : that.accountTitlePayableName != null)
            return false;
        if (accountTitlePayableNo != null ? !accountTitlePayableNo.equals(that.accountTitlePayableNo) : that.accountTitlePayableNo != null)
            return false;
        if (accountTitlePropertyName != null ? !accountTitlePropertyName.equals(that.accountTitlePropertyName) : that.accountTitlePropertyName != null)
            return false;
        if (accountTitlePropertyNo != null ? !accountTitlePropertyNo.equals(that.accountTitlePropertyNo) : that.accountTitlePropertyNo != null)
            return false;
        if (salaryPeriodName != null ? !salaryPeriodName.equals(that.salaryPeriodName) : that.salaryPeriodName != null)
            return false;
        if (taxId != null ? !taxId.equals(that.taxId) : that.taxId != null) return false;
        if (taxName != null ? !taxName.equals(that.taxName) : that.taxName != null) return false;
        if (taxNo != null ? !taxNo.equals(that.taxNo) : that.taxNo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + warehouseId;
        result = 31 * result + salaryPeriodId;
        result = 31 * result + (no != null ? no.hashCode() : 0);
        result = 31 * result + (accountTitlePayableId != null ? accountTitlePayableId.hashCode() : 0);
        result = 31 * result + (accountTitlePropertyId != null ? accountTitlePropertyId.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + createPersonId;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (accountTitleExpenseId != null ? accountTitleExpenseId.hashCode() : 0);
        result = 31 * result + (createPersonName != null ? createPersonName.hashCode() : 0);
        result = 31 * result + (warehouseName != null ? warehouseName.hashCode() : 0);
        result = 31 * result + (accountTitleExpenseName != null ? accountTitleExpenseName.hashCode() : 0);
        result = 31 * result + (accountTitleExpenseNo != null ? accountTitleExpenseNo.hashCode() : 0);
        result = 31 * result + (accountTitlePayableName != null ? accountTitlePayableName.hashCode() : 0);
        result = 31 * result + (accountTitlePayableNo != null ? accountTitlePayableNo.hashCode() : 0);
        result = 31 * result + (accountTitlePropertyName != null ? accountTitlePropertyName.hashCode() : 0);
        result = 31 * result + (accountTitlePropertyNo != null ? accountTitlePropertyNo.hashCode() : 0);
        result = 31 * result + (salaryPeriodName != null ? salaryPeriodName.hashCode() : 0);
        result = 31 * result + state;
        result = 31 * result + (taxId != null ? taxId.hashCode() : 0);
        result = 31 * result + (taxName != null ? taxName.hashCode() : 0);
        result = 31 * result + (taxNo != null ? taxNo.hashCode() : 0);
        return result;
    }
}
