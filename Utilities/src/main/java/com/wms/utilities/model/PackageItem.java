package com.wms.utilities.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class PackageItem {
    private int id;
    private int packageId;
    private int supplyId;
    private BigDecimal defaultDeliveryAmount;
    private String defaultDeliveryUnit;
    private BigDecimal defaultDeliveryUnitAmount;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "PackageID", nullable = false)
    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
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
    @Column(name = "DefaultDeliveryAmount", nullable = false, precision = 3)
    public BigDecimal getDefaultDeliveryAmount() {
        return defaultDeliveryAmount;
    }

    public void setDefaultDeliveryAmount(BigDecimal defaultDeliveryAmount) {
        this.defaultDeliveryAmount = defaultDeliveryAmount;
    }

    @Basic
    @Column(name = "DefaultDeliveryUnit", nullable = false, length = 64)
    public String getDefaultDeliveryUnit() {
        return defaultDeliveryUnit;
    }

    public void setDefaultDeliveryUnit(String defaultDeliveryUnit) {
        this.defaultDeliveryUnit = defaultDeliveryUnit;
    }

    @Basic
    @Column(name = "DefaultDeliveryUnitAmount", nullable = false, precision = 3)
    public BigDecimal getDefaultDeliveryUnitAmount() {
        return defaultDeliveryUnitAmount;
    }

    public void setDefaultDeliveryUnitAmount(BigDecimal defaultDeliveryUnitAmount) {
        this.defaultDeliveryUnitAmount = defaultDeliveryUnitAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PackageItem that = (PackageItem) o;
        return id == that.id &&
                packageId == that.packageId &&
                supplyId == that.supplyId &&
                Objects.equals(defaultDeliveryAmount, that.defaultDeliveryAmount) &&
                Objects.equals(defaultDeliveryUnit, that.defaultDeliveryUnit) &&
                Objects.equals(defaultDeliveryUnitAmount, that.defaultDeliveryUnitAmount);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, packageId, supplyId, defaultDeliveryAmount, defaultDeliveryUnit, defaultDeliveryUnitAmount);
    }
}
