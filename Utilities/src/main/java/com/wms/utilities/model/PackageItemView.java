package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class PackageItemView {
    private int id;
    private int packageId;
    private int supplyId;
    private BigDecimal defaultDeliveryAmount;
    private String defaultDeliveryUnit;
    private BigDecimal defaultDeliveryUnitAmount;
    private String packageName;
    private String materialNo;
    private String materialName;
    private String supplierNo;
    private String supplierName;
    private Integer materialId;
    private Integer supplierId;
    private String materialProductLine;
    private int defaultDeliveryStorageLocationId;
    private String defaultDeliveryStorageLocationNo;
    private String defaultDeliveryStorageLocationName;
    private String supplySerialNo;

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
    @Column(name = "PackageName")
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Basic
    @Column(name = "MaterialNo")
    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    @Basic
    @Column(name = "MaterialName")
    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    @Basic
    @Column(name = "SupplierNo")
    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    @Basic
    @Column(name = "SupplierName")
    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    @Basic
    @Column(name = "MaterialID")
    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    @Basic
    @Column(name = "SupplierID")
    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    @Basic
    @Column(name = "MaterialProductLine")
    public String getMaterialProductLine() {
        return materialProductLine;
    }

    public void setMaterialProductLine(String materialProductLine) {
        this.materialProductLine = materialProductLine;
    }

    @Basic
    @Column(name = "DefaultDeliveryStorageLocationID")
    public int getDefaultDeliveryStorageLocationId() {
        return defaultDeliveryStorageLocationId;
    }

    public void setDefaultDeliveryStorageLocationId(int defaultDeliveryStorageLocationId) {
        this.defaultDeliveryStorageLocationId = defaultDeliveryStorageLocationId;
    }

    @Basic
    @Column(name = "DefaultDeliveryStorageLocationNo")
    public String getDefaultDeliveryStorageLocationNo() {
        return defaultDeliveryStorageLocationNo;
    }

    public void setDefaultDeliveryStorageLocationNo(String defaultDeliveryStorageLocationNo) {
        this.defaultDeliveryStorageLocationNo = defaultDeliveryStorageLocationNo;
    }

    @Basic
    @Column(name = "DefaultDeliveryStorageLocationName")
    public String getDefaultDeliveryStorageLocationName() {
        return defaultDeliveryStorageLocationName;
    }

    public void setDefaultDeliveryStorageLocationName(String defaultDeliveryStorageLocationName) {
        this.defaultDeliveryStorageLocationName = defaultDeliveryStorageLocationName;
    }

    @Basic
    @Column(name = "SupplySerialNo")
    public String getSupplySerialNo() {
        return supplySerialNo;
    }

    public void setSupplySerialNo(String supplySerialNo) {
        this.supplySerialNo = supplySerialNo;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        PackageItemView that = (PackageItemView) object;

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
        if (packageName != null ? !packageName.equals(that.packageName) : that.packageName != null) return false;
        if (materialNo != null ? !materialNo.equals(that.materialNo) : that.materialNo != null) return false;
        if (materialName != null ? !materialName.equals(that.materialName) : that.materialName != null) return false;
        if (supplierNo != null ? !supplierNo.equals(that.supplierNo) : that.supplierNo != null) return false;
        if (supplierName != null ? !supplierName.equals(that.supplierName) : that.supplierName != null) return false;
        if (materialId != null ? !materialId.equals(that.materialId) : that.materialId != null) return false;
        if (supplierId != null ? !supplierId.equals(that.supplierId) : that.supplierId != null) return false;
        if (materialProductLine != null ? !materialProductLine.equals(that.materialProductLine) : that.materialProductLine != null)
            return false;
        if (defaultDeliveryStorageLocationNo != null ? !defaultDeliveryStorageLocationNo.equals(that.defaultDeliveryStorageLocationNo) : that.defaultDeliveryStorageLocationNo != null)
            return false;
        if (defaultDeliveryStorageLocationName != null ? !defaultDeliveryStorageLocationName.equals(that.defaultDeliveryStorageLocationName) : that.defaultDeliveryStorageLocationName != null)
            return false;
        if (supplySerialNo != null ? !supplySerialNo.equals(that.supplySerialNo) : that.supplySerialNo != null)
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
        result = 31 * result + (packageName != null ? packageName.hashCode() : 0);
        result = 31 * result + (materialNo != null ? materialNo.hashCode() : 0);
        result = 31 * result + (materialName != null ? materialName.hashCode() : 0);
        result = 31 * result + (supplierNo != null ? supplierNo.hashCode() : 0);
        result = 31 * result + (supplierName != null ? supplierName.hashCode() : 0);
        result = 31 * result + (materialId != null ? materialId.hashCode() : 0);
        result = 31 * result + (supplierId != null ? supplierId.hashCode() : 0);
        result = 31 * result + (materialProductLine != null ? materialProductLine.hashCode() : 0);
        result = 31 * result + defaultDeliveryStorageLocationId;
        result = 31 * result + (defaultDeliveryStorageLocationNo != null ? defaultDeliveryStorageLocationNo.hashCode() : 0);
        result = 31 * result + (defaultDeliveryStorageLocationName != null ? defaultDeliveryStorageLocationName.hashCode() : 0);
        result = 31 * result + (supplySerialNo != null ? supplySerialNo.hashCode() : 0);
        return result;
    }
}
