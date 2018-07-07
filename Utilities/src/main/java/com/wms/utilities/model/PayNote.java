package com.wms.utilities.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class PayNote {
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        PayNote payNote = (PayNote) object;

        if (id != payNote.id) return false;
        if (warehouseId != payNote.warehouseId) return false;
        if (salaryPeriodId != payNote.salaryPeriodId) return false;
        if (createPersonId != payNote.createPersonId) return false;
        if (no != null ? !no.equals(payNote.no) : payNote.no != null) return false;
        if (accountTitlePayableId != null ? !accountTitlePayableId.equals(payNote.accountTitlePayableId) : payNote.accountTitlePayableId != null)
            return false;
        if (accountTitlePropertyId != null ? !accountTitlePropertyId.equals(payNote.accountTitlePropertyId) : payNote.accountTitlePropertyId != null)
            return false;
        if (description != null ? !description.equals(payNote.description) : payNote.description != null) return false;
        if (createTime != null ? !createTime.equals(payNote.createTime) : payNote.createTime != null) return false;
        if (accountTitleExpenseId != null ? !accountTitleExpenseId.equals(payNote.accountTitleExpenseId) : payNote.accountTitleExpenseId != null)
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
        result = 31 * result + (accountTitlePropertyId != null ? accountTitlePropertyId.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + createPersonId;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (accountTitleExpenseId != null ? accountTitleExpenseId.hashCode() : 0);
        return result;
    }
}
