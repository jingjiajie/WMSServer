package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class PersonSalaryView {
    private int id;
    private Integer warehouseId;
    private Integer salaryPeriodId;
    private Integer personId;
    private Integer salaryItemId;
    private BigDecimal amount;
    private String warehouseName;
    private String personName;
    private String salaryPeriodName;
    private String salaryItemName;

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

    @Basic
    @Column(name = "SalaryPeriodName")
    public String getSalaryPeriodName() {
        return salaryPeriodName;
    }

    public void setSalaryPeriodName(String salaryPeriodName) {
        this.salaryPeriodName = salaryPeriodName;
    }

    @Basic
    @Column(name = "SalaryItemName")
    public String getSalaryItemName() {
        return salaryItemName;
    }

    public void setSalaryItemName(String salaryItemName) {
        this.salaryItemName = salaryItemName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonSalaryView that = (PersonSalaryView) o;

        if (id != that.id) return false;
        if (warehouseId != null ? !warehouseId.equals(that.warehouseId) : that.warehouseId != null) return false;
        if (salaryPeriodId != null ? !salaryPeriodId.equals(that.salaryPeriodId) : that.salaryPeriodId != null)
            return false;
        if (personId != null ? !personId.equals(that.personId) : that.personId != null) return false;
        if (salaryItemId != null ? !salaryItemId.equals(that.salaryItemId) : that.salaryItemId != null) return false;
        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
        if (warehouseName != null ? !warehouseName.equals(that.warehouseName) : that.warehouseName != null)
            return false;
        if (personName != null ? !personName.equals(that.personName) : that.personName != null) return false;
        if (salaryPeriodName != null ? !salaryPeriodName.equals(that.salaryPeriodName) : that.salaryPeriodName != null)
            return false;
        if (salaryItemName != null ? !salaryItemName.equals(that.salaryItemName) : that.salaryItemName != null)
            return false;

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
        result = 31 * result + (warehouseName != null ? warehouseName.hashCode() : 0);
        result = 31 * result + (personName != null ? personName.hashCode() : 0);
        result = 31 * result + (salaryPeriodName != null ? salaryPeriodName.hashCode() : 0);
        result = 31 * result + (salaryItemName != null ? salaryItemName.hashCode() : 0);
        return result;
    }
}
