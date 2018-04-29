package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class BarCode {
    private int id;
    private String barCodeNo;
    private int supplyId;
    private String unit;
    private BigDecimal unitAmount;
    private int warehouseId;
    private int enabled;

    @Id
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "BarCodeNo", nullable = false, length = 64)
    public String getBarCodeNo() {
        return barCodeNo;
    }

    public void setBarCodeNo(String barCodeNo) {
        this.barCodeNo = barCodeNo;
    }

    @Basic
    @Column(name = "SupplyID", nullable = false)
    public int getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(int supplyId) {
        this.supplyId = supplyId;
    }

    @Basic
    @Column(name = "Unit", nullable = true, length = 64)
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Basic
    @Column(name = "UnitAmount", nullable = true, precision = 3)
    public BigDecimal getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(BigDecimal unitAmount) {
        this.unitAmount = unitAmount;
    }

    @Basic
    @Column(name = "WarehouseID", nullable = false)
    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    @Basic
    @Column(name = "Enabled", nullable = false)
    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BarCode barCode = (BarCode) o;
        return id == barCode.id &&
                supplyId == barCode.supplyId &&
                warehouseId == barCode.warehouseId &&
                enabled == barCode.enabled &&
                Objects.equals(barCodeNo, barCode.barCodeNo) &&
                Objects.equals(unit, barCode.unit) &&
                Objects.equals(unitAmount, barCode.unitAmount);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, barCodeNo, supplyId, unit, unitAmount, warehouseId, enabled);
    }
}
