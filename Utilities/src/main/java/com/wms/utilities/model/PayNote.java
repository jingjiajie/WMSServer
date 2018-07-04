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
    private Integer accountTitlePaidId;
    private String description;
    private int createPersonId;
    private Timestamp createTime;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PayNote payNote = (PayNote) o;

        if (id != payNote.id) return false;
        if (warehouseId != payNote.warehouseId) return false;
        if (salaryPeriodId != payNote.salaryPeriodId) return false;
        if (createPersonId != payNote.createPersonId) return false;
        if (no != null ? !no.equals(payNote.no) : payNote.no != null) return false;
        if (accountTitlePayableId != null ? !accountTitlePayableId.equals(payNote.accountTitlePayableId) : payNote.accountTitlePayableId != null)
            return false;
        if (accountTitlePaidId != null ? !accountTitlePaidId.equals(payNote.accountTitlePaidId) : payNote.accountTitlePaidId != null)
            return false;
        if (description != null ? !description.equals(payNote.description) : payNote.description != null) return false;
        if (createTime != null ? !createTime.equals(payNote.createTime) : payNote.createTime != null) return false;

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
        return result;
    }
}
