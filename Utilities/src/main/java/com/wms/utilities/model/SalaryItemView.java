package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class SalaryItemView {
    private int id;
    private Integer salaryTypeId;
    private String name;
    private BigDecimal defaultAmount;
    private int type;
    private int warehouseId;
    private String warehouseName;
    private String salaryTypeName;

    @Basic
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "SalaryTypeID")
    public Integer getSalaryTypeId() {
        return salaryTypeId;
    }

    public void setSalaryTypeId(Integer salaryTypeId) {
        this.salaryTypeId = salaryTypeId;
    }

    @Basic
    @Column(name = "Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "DefaultAmount")
    public BigDecimal getDefaultAmount() {
        return defaultAmount;
    }

    public void setDefaultAmount(BigDecimal defaultAmount) {
        this.defaultAmount = defaultAmount;
    }

    @Basic
    @Column(name = "Type")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
    @Column(name = "WarehouseName")
    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    @Basic
    @Column(name = "SalaryTypeName")
    public String getSalaryTypeName() {
        return salaryTypeName;
    }

    public void setSalaryTypeName(String salaryTypeName) {
        this.salaryTypeName = salaryTypeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SalaryItemView that = (SalaryItemView) o;

        if (id != that.id) return false;
        if (type != that.type) return false;
        if (warehouseId != that.warehouseId) return false;
        if (salaryTypeId != null ? !salaryTypeId.equals(that.salaryTypeId) : that.salaryTypeId != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (defaultAmount != null ? !defaultAmount.equals(that.defaultAmount) : that.defaultAmount != null)
            return false;
        if (warehouseName != null ? !warehouseName.equals(that.warehouseName) : that.warehouseName != null)
            return false;
        if (salaryTypeName != null ? !salaryTypeName.equals(that.salaryTypeName) : that.salaryTypeName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (salaryTypeId != null ? salaryTypeId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (defaultAmount != null ? defaultAmount.hashCode() : 0);
        result = 31 * result + type;
        result = 31 * result + warehouseId;
        result = 31 * result + (warehouseName != null ? warehouseName.hashCode() : 0);
        result = 31 * result + (salaryTypeName != null ? salaryTypeName.hashCode() : 0);
        return result;
    }
}
