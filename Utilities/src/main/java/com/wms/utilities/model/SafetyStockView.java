package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class SafetyStockView {
    private int id;
    private int warehouseId;
    private int supplyId;
    private String unit;
    private BigDecimal unitAmount;
    private String warehouseName;
    private String materialNo;
    private String materialName;
    private String supplierNo;
    private String supplierName;
    private int type;
    private int targetStorageLocationId;
    private int sourceStorageLocationId;
    private String sourceStorageLocationNo;
    private String sourceStorageLocationName;
    private String targetStorageLocationNo;
    private String targetStorageLocationName;
    private String sourceUnit;
    private BigDecimal sourceUnitAmount;
    private Integer materialId;
    private Integer supplierId;
    private String materialProductLine;
    private String supplySerialNo;
    private BigDecimal amountMax;
    private BigDecimal amountMin;

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
    @Column(name = "WarehouseName")
    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
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
    @Column(name = "SourceStorageLocationID")
    public int getSourceStorageLocationId() {
        return sourceStorageLocationId;
    }

    public void setSourceStorageLocationId(int sourceStorageLocationId) {
        this.sourceStorageLocationId = sourceStorageLocationId;
    }

    @Basic
    @Column(name = "SourceStorageLocationNo")
    public String getSourceStorageLocationNo() {
        return sourceStorageLocationNo;
    }

    public void setSourceStorageLocationNo(String sourceStorageLocationNo) {
        this.sourceStorageLocationNo = sourceStorageLocationNo;
    }

    @Basic
    @Column(name = "SourceStorageLocationName")
    public String getSourceStorageLocationName() {
        return sourceStorageLocationName;
    }

    public void setSourceStorageLocationName(String sourceStorageLocationName) {
        this.sourceStorageLocationName = sourceStorageLocationName;
    }

    @Basic
    @Column(name = "TargetStorageLocationNo")
    public String getTargetStorageLocationNo() {
        return targetStorageLocationNo;
    }

    public void setTargetStorageLocationNo(String targetStorageLocationNo) {
        this.targetStorageLocationNo = targetStorageLocationNo;
    }

    @Basic
    @Column(name = "TargetStorageLocationName")
    public String getTargetStorageLocationName() {
        return targetStorageLocationName;
    }

    public void setTargetStorageLocationName(String targetStorageLocationName) {
        this.targetStorageLocationName = targetStorageLocationName;
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
    @Column(name = "SupplySerialNo")
    public String getSupplySerialNo() {
        return supplySerialNo;
    }

    public void setSupplySerialNo(String supplySerialNo) {
        this.supplySerialNo = supplySerialNo;
    }

    @Basic
    @Column(name = "AmountMax")
    public BigDecimal getAmountMax() {
        return amountMax;
    }

    public void setAmountMax(BigDecimal amountMax) {
        this.amountMax = amountMax;
    }

    @Basic
    @Column(name = "AmountMin")
    public BigDecimal getAmountMin() {
        return amountMin;
    }

    public void setAmountMin(BigDecimal amountMin) {
        this.amountMin = amountMin;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        SafetyStockView that = (SafetyStockView) object;

        if (id != that.id) return false;
        if (warehouseId != that.warehouseId) return false;
        if (supplyId != that.supplyId) return false;
        if (type != that.type) return false;
        if (targetStorageLocationId != that.targetStorageLocationId) return false;
        if (sourceStorageLocationId != that.sourceStorageLocationId) return false;
        if (unit != null ? !unit.equals(that.unit) : that.unit != null) return false;
        if (unitAmount != null ? !unitAmount.equals(that.unitAmount) : that.unitAmount != null) return false;
        if (warehouseName != null ? !warehouseName.equals(that.warehouseName) : that.warehouseName != null)
            return false;
        if (materialNo != null ? !materialNo.equals(that.materialNo) : that.materialNo != null) return false;
        if (materialName != null ? !materialName.equals(that.materialName) : that.materialName != null) return false;
        if (supplierNo != null ? !supplierNo.equals(that.supplierNo) : that.supplierNo != null) return false;
        if (supplierName != null ? !supplierName.equals(that.supplierName) : that.supplierName != null) return false;
        if (sourceStorageLocationNo != null ? !sourceStorageLocationNo.equals(that.sourceStorageLocationNo) : that.sourceStorageLocationNo != null)
            return false;
        if (sourceStorageLocationName != null ? !sourceStorageLocationName.equals(that.sourceStorageLocationName) : that.sourceStorageLocationName != null)
            return false;
        if (targetStorageLocationNo != null ? !targetStorageLocationNo.equals(that.targetStorageLocationNo) : that.targetStorageLocationNo != null)
            return false;
        if (targetStorageLocationName != null ? !targetStorageLocationName.equals(that.targetStorageLocationName) : that.targetStorageLocationName != null)
            return false;
        if (sourceUnit != null ? !sourceUnit.equals(that.sourceUnit) : that.sourceUnit != null) return false;
        if (sourceUnitAmount != null ? !sourceUnitAmount.equals(that.sourceUnitAmount) : that.sourceUnitAmount != null)
            return false;
        if (materialId != null ? !materialId.equals(that.materialId) : that.materialId != null) return false;
        if (supplierId != null ? !supplierId.equals(that.supplierId) : that.supplierId != null) return false;
        if (materialProductLine != null ? !materialProductLine.equals(that.materialProductLine) : that.materialProductLine != null)
            return false;
        if (supplySerialNo != null ? !supplySerialNo.equals(that.supplySerialNo) : that.supplySerialNo != null)
            return false;
        if (amountMax != null ? !amountMax.equals(that.amountMax) : that.amountMax != null) return false;
        if (amountMin != null ? !amountMin.equals(that.amountMin) : that.amountMin != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + warehouseId;
        result = 31 * result + supplyId;
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + (unitAmount != null ? unitAmount.hashCode() : 0);
        result = 31 * result + (warehouseName != null ? warehouseName.hashCode() : 0);
        result = 31 * result + (materialNo != null ? materialNo.hashCode() : 0);
        result = 31 * result + (materialName != null ? materialName.hashCode() : 0);
        result = 31 * result + (supplierNo != null ? supplierNo.hashCode() : 0);
        result = 31 * result + (supplierName != null ? supplierName.hashCode() : 0);
        result = 31 * result + type;
        result = 31 * result + targetStorageLocationId;
        result = 31 * result + sourceStorageLocationId;
        result = 31 * result + (sourceStorageLocationNo != null ? sourceStorageLocationNo.hashCode() : 0);
        result = 31 * result + (sourceStorageLocationName != null ? sourceStorageLocationName.hashCode() : 0);
        result = 31 * result + (targetStorageLocationNo != null ? targetStorageLocationNo.hashCode() : 0);
        result = 31 * result + (targetStorageLocationName != null ? targetStorageLocationName.hashCode() : 0);
        result = 31 * result + (sourceUnit != null ? sourceUnit.hashCode() : 0);
        result = 31 * result + (sourceUnitAmount != null ? sourceUnitAmount.hashCode() : 0);
        result = 31 * result + (materialId != null ? materialId.hashCode() : 0);
        result = 31 * result + (supplierId != null ? supplierId.hashCode() : 0);
        result = 31 * result + (materialProductLine != null ? materialProductLine.hashCode() : 0);
        result = 31 * result + (supplySerialNo != null ? supplySerialNo.hashCode() : 0);
        result = 31 * result + (amountMax != null ? amountMax.hashCode() : 0);
        result = 31 * result + (amountMin != null ? amountMin.hashCode() : 0);
        return result;
    }
}
