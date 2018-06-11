package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class PackageItem {
    private int id;
    private int packageId;
    private int supplyId;
    private BigDecimal defaultDeliveryAmount;
    private String defaultDeliveryUnit;
    private BigDecimal defaultDeliveryUnitAmount;
    private int defaultDeliveryStorageLocationId;

    @Id
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "PackageID")
    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
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
    @Column(name = "DefaultDeliveryAmount")
    public BigDecimal getDefaultDeliveryAmount() {
        return defaultDeliveryAmount;
    }

    public void setDefaultDeliveryAmount(BigDecimal defaultDeliveryAmount) {
        this.defaultDeliveryAmount = defaultDeliveryAmount;
    }

    @Basic
    @Column(name = "DefaultDeliveryUnit")
    public String getDefaultDeliveryUnit() {
        return defaultDeliveryUnit;
    }

    public void setDefaultDeliveryUnit(String defaultDeliveryUnit) {
        this.defaultDeliveryUnit = defaultDeliveryUnit;
    }

    @Basic
    @Column(name = "DefaultDeliveryUnitAmount")
    public BigDecimal getDefaultDeliveryUnitAmount() {
        return defaultDeliveryUnitAmount;
    }

    public void setDefaultDeliveryUnitAmount(BigDecimal defaultDeliveryUnitAmount) {
        this.defaultDeliveryUnitAmount = defaultDeliveryUnitAmount;
    }

    @Basic
    @Column(name = "DefaultDeliveryStorageLocationID")
    public int getDefaultDeliveryStorageLocationId() {
        return defaultDeliveryStorageLocationId;
    }

    public void setDefaultDeliveryStorageLocationId(int defaultDeliveryStorageLocationId) {
        this.defaultDeliveryStorageLocationId = defaultDeliveryStorageLocationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PackageItem that = (PackageItem) o;

        if (id != that.id) return false;
        if (packageId != that.packageId) return false;
        if (supplyId != that.supplyId) return false;
        if (defaultDeliveryStorageLocationId != that.defaultDeliveryStorageLocationId) return false;
        if (defaultDeliveryAmount != null ? !defaultDeliveryAmount.equals(that.defaultDeliveryAmount) : that.defaultDeliveryAmount != null)
            return false;
        if (defaultDeliveryUnit != null ? !defaultDeliveryUnit.equals(that.defaultDeliveryUnit) : that.defaultDeliveryUnit != null)
            return false;
        if (defaultDeliveryUnitAmount != null ? !defaultDeliveryUnitAmount.equals(that.defaultDeliveryUnitAmount) : that.defaultDeliveryUnitAmount != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + packageId;
        result = 31 * result + supplyId;
        result = 31 * result + (defaultDeliveryAmount != null ? defaultDeliveryAmount.hashCode() : 0);
        result = 31 * result + (defaultDeliveryUnit != null ? defaultDeliveryUnit.hashCode() : 0);
        result = 31 * result + (defaultDeliveryUnitAmount != null ? defaultDeliveryUnitAmount.hashCode() : 0);
        result = 31 * result + defaultDeliveryStorageLocationId;
        return result;
    }
}
