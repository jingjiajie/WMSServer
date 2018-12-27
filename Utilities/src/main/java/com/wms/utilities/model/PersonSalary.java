package com.wms.utilities.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class PersonSalary {
    private int id;
    private Integer warehouseId;
    private Integer salaryPeriodId;
    private Integer personId;
    private Integer salaryItemId;
    private BigDecimal amount;
    private Integer edited;

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

    @Basic
    @Column(name = "Edited")
    public Integer getEdited() {
        return edited;
    }

    public void setEdited(Integer edited) {
        this.edited = edited;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        PersonSalary that = (PersonSalary) object;

        if (id != that.id) return false;
        if (warehouseId != null ? !warehouseId.equals(that.warehouseId) : that.warehouseId != null) return false;
        if (salaryPeriodId != null ? !salaryPeriodId.equals(that.salaryPeriodId) : that.salaryPeriodId != null)
            return false;
        if (personId != null ? !personId.equals(that.personId) : that.personId != null) return false;
        if (salaryItemId != null ? !salaryItemId.equals(that.salaryItemId) : that.salaryItemId != null) return false;
        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
        if (edited != null ? !edited.equals(that.edited) : that.edited != null) return false;

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
        result = 31 * result + (edited != null ? edited.hashCode() : 0);
        return result;
    }
}
