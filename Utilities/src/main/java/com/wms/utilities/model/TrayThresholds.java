package com.wms.utilities.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class TrayThresholds {
    private int id;
    private int warehouseId;
    private BigDecimal threshold;

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
    @Column(name = "Threshold")
    public BigDecimal getThreshold() {
        return threshold;
    }

    public void setThreshold(BigDecimal threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        TrayThresholds that = (TrayThresholds) object;

        if (id != that.id) return false;
        if (warehouseId != that.warehouseId) return false;
        if (threshold != null ? !threshold.equals(that.threshold) : that.threshold != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + warehouseId;
        result = 31 * result + (threshold != null ? threshold.hashCode() : 0);
        return result;
    }
}
