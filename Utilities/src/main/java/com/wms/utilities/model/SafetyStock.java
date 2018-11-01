package com.wms.utilities.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class SafetyStock {
    private int id;
    private int warehouseId;
    private int supplyId;
    private int sourceStorageLocationId;
    private BigDecimal amount;
    private String unit;
    private BigDecimal unitAmount;
    private int type;
    private int targetStorageLocationId;
    private String sourceUnit;
    private BigDecimal sourceUnitAmount;

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
    @Column(name = "SupplyID")
    public int getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(int supplyId) {
        this.supplyId = supplyId;
    }

    @Basic
    @Column(name = "SourceStorageLocationID")
    public int getSourceStorageLocationId() {
        return sourceStorageLocationId;
    }

    public void setSourceStorageLocationId(int sourceStorageLocationId) {
        this.sourceStorageLocationId = sourceStorageLocationId;
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
    @Column(name = "Unit")
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Basic
    @Column(name = "UnitAmount")
    public BigDecimal getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(BigDecimal unitAmount) {
        this.unitAmount = unitAmount;
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
    @Column(name = "TargetStorageLocationID")
    public int getTargetStorageLocationId() {
        return targetStorageLocationId;
    }

    public void setTargetStorageLocationId(int targetStorageLocationId) {
        this.targetStorageLocationId = targetStorageLocationId;
    }

    @Basic
    @Column(name = "SourceUnit")
    public String getSourceUnit() {
        return sourceUnit;
    }

    public void setSourceUnit(String sourceUnit) {
        this.sourceUnit = sourceUnit;
    }

    @Basic
    @Column(name = "SourceUnitAmount")
    public BigDecimal getSourceUnitAmount() {
        return sourceUnitAmount;
    }

    public void setSourceUnitAmount(BigDecimal sourceUnitAmount) {
        this.sourceUnitAmount = sourceUnitAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SafetyStock that = (SafetyStock) o;

        if (id != that.id) return false;
        if (warehouseId != that.warehouseId) return false;
        if (supplyId != that.supplyId) return false;
        if (sourceStorageLocationId != that.sourceStorageLocationId) return false;
        if (type != that.type) return false;
        if (targetStorageLocationId != that.targetStorageLocationId) return false;
        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
        if (unit != null ? !unit.equals(that.unit) : that.unit != null) return false;
        if (unitAmount != null ? !unitAmount.equals(that.unitAmount) : that.unitAmount != null) return false;
        if (sourceUnit != null ? !sourceUnit.equals(that.sourceUnit) : that.sourceUnit != null) return false;
        if (sourceUnitAmount != null ? !sourceUnitAmount.equals(that.sourceUnitAmount) : that.sourceUnitAmount != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + warehouseId;
        result = 31 * result + supplyId;
        result = 31 * result + sourceStorageLocationId;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + (unitAmount != null ? unitAmount.hashCode() : 0);
        result = 31 * result + type;
        result = 31 * result + targetStorageLocationId;
        result = 31 * result + (sourceUnit != null ? sourceUnit.hashCode() : 0);
        result = 31 * result + (sourceUnitAmount != null ? sourceUnitAmount.hashCode() : 0);
        return result;
    }
}
