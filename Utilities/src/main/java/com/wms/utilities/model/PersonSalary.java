package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class PersonSalary {
    private int id;
    private Integer warehouseId;
    private Integer salaryPeriodId;
    private Integer personId;
    private Integer salaryItemId;
    private BigDecimal amount;

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
    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    @Basic
    @Column(name = "SalaryPeriodID")
    public Integer getSalaryPeriodId() {
        return salaryPeriodId;
    }

    public void setSalaryPeriodId(Integer salaryPeriodId) {
        this.salaryPeriodId = salaryPeriodId;
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
    @Column(name = "SalaryItemID")
    public Integer getSalaryItemId() {
        return salaryItemId;
    }

    public void setSalaryItemId(Integer salaryItemId) {
        this.salaryItemId = salaryItemId;
    }

    @Basic
    @Column(name = "Amount")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonSalary that = (PersonSalary) o;

        if (id != that.id) return false;
        if (warehouseId != null ? !warehouseId.equals(that.warehouseId) : that.warehouseId != null) return false;
        if (salaryPeriodId != null ? !salaryPeriodId.equals(that.salaryPeriodId) : that.salaryPeriodId != null)
            return false;
        if (personId != null ? !personId.equals(that.personId) : that.personId != null) return false;
        if (salaryItemId != null ? !salaryItemId.equals(that.salaryItemId) : that.salaryItemId != null) return false;
        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (warehouseId != null ? warehouseId.hashCode() : 0);
        result = 31 * result + (salaryPeriodId != null ? salaryPeriodId.hashCode() : 0);
        result = 31 * result + (personId != null ? personId.hashCode() : 0);
        result = 31 * result + (salaryItemId != null ? salaryItemId.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }
}
