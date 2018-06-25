package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
public class TransferRecordView {
    private int id;
    private int warehouseId;
    private String sourceStorageLocationNo;
    private String sourceStorageLocationName;
    private String targetStorageLocationNo;
    private String targetStorageLocationName;
    private String warehouseName;
    private String materialNo;
    private String materialName;
    private String supplierNo;
    private String supplierName;
    private Integer supplyId;
    private Timestamp time;
    private Integer sourceStorageLocationId;
    private BigDecimal sourceStorageLocationOriginalAmount;
    private BigDecimal sourceStorageLocationNewAmount;
    private String sourceStorageLocationUnit;
    private BigDecimal sourceStorageLocationUnitAmount;
    private Integer targetStorageLocationId;
    private BigDecimal targetStorageLocationOriginalAmount;
    private BigDecimal targetStorageLocationNewAmount;
    private String targetStorageLocationUnit;
    private BigDecimal targetStorageLocationAmount;

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
    @Column(name = "SupplyID")
    public Integer getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(Integer supplyId) {
        this.supplyId = supplyId;
    }

    @Basic
    @Column(name = "Time")
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Basic
    @Column(name = "SourceStorageLocationID")
    public Integer getSourceStorageLocationId() {
        return sourceStorageLocationId;
    }

    public void setSourceStorageLocationId(Integer sourceStorageLocationId) {
        this.sourceStorageLocationId = sourceStorageLocationId;
    }

    @Basic
    @Column(name = "SourceStorageLocationOriginalAmount")
    public BigDecimal getSourceStorageLocationOriginalAmount() {
        return sourceStorageLocationOriginalAmount;
    }

    public void setSourceStorageLocationOriginalAmount(BigDecimal sourceStorageLocationOriginalAmount) {
        this.sourceStorageLocationOriginalAmount = sourceStorageLocationOriginalAmount;
    }

    @Basic
    @Column(name = "SourceStorageLocationNewAmount")
    public BigDecimal getSourceStorageLocationNewAmount() {
        return sourceStorageLocationNewAmount;
    }

    public void setSourceStorageLocationNewAmount(BigDecimal sourceStorageLocationNewAmount) {
        this.sourceStorageLocationNewAmount = sourceStorageLocationNewAmount;
    }

    @Basic
    @Column(name = "SourceStorageLocationUnit")
    public String getSourceStorageLocationUnit() {
        return sourceStorageLocationUnit;
    }

    public void setSourceStorageLocationUnit(String sourceStorageLocationUnit) {
        this.sourceStorageLocationUnit = sourceStorageLocationUnit;
    }

    @Basic
    @Column(name = "SourceStorageLocationUnitAmount")
    public BigDecimal getSourceStorageLocationUnitAmount() {
        return sourceStorageLocationUnitAmount;
    }

    public void setSourceStorageLocationUnitAmount(BigDecimal sourceStorageLocationUnitAmount) {
        this.sourceStorageLocationUnitAmount = sourceStorageLocationUnitAmount;
    }

    @Basic
    @Column(name = "TargetStorageLocationID")
    public Integer getTargetStorageLocationId() {
        return targetStorageLocationId;
    }

    public void setTargetStorageLocationId(Integer targetStorageLocationId) {
        this.targetStorageLocationId = targetStorageLocationId;
    }

    @Basic
    @Column(name = "TargetStorageLocationOriginalAmount")
    public BigDecimal getTargetStorageLocationOriginalAmount() {
        return targetStorageLocationOriginalAmount;
    }

    public void setTargetStorageLocationOriginalAmount(BigDecimal targetStorageLocationOriginalAmount) {
        this.targetStorageLocationOriginalAmount = targetStorageLocationOriginalAmount;
    }

    @Basic
    @Column(name = "TargetStorageLocationNewAmount")
    public BigDecimal getTargetStorageLocationNewAmount() {
        return targetStorageLocationNewAmount;
    }

    public void setTargetStorageLocationNewAmount(BigDecimal targetStorageLocationNewAmount) {
        this.targetStorageLocationNewAmount = targetStorageLocationNewAmount;
    }

    @Basic
    @Column(name = "TargetStorageLocationUnit")
    public String getTargetStorageLocationUnit() {
        return targetStorageLocationUnit;
    }

    public void setTargetStorageLocationUnit(String targetStorageLocationUnit) {
        this.targetStorageLocationUnit = targetStorageLocationUnit;
    }

    @Basic
    @Column(name = "TargetStorageLocationAmount")
    public BigDecimal getTargetStorageLocationAmount() {
        return targetStorageLocationAmount;
    }

    public void setTargetStorageLocationAmount(BigDecimal targetStorageLocationAmount) {
        this.targetStorageLocationAmount = targetStorageLocationAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransferRecordView that = (TransferRecordView) o;

        if (id != that.id) return false;
        if (warehouseId != that.warehouseId) return false;
        if (sourceStorageLocationNo != null ? !sourceStorageLocationNo.equals(that.sourceStorageLocationNo) : that.sourceStorageLocationNo != null)
            return false;
        if (sourceStorageLocationName != null ? !sourceStorageLocationName.equals(that.sourceStorageLocationName) : that.sourceStorageLocationName != null)
            return false;
        if (targetStorageLocationNo != null ? !targetStorageLocationNo.equals(that.targetStorageLocationNo) : that.targetStorageLocationNo != null)
            return false;
        if (targetStorageLocationName != null ? !targetStorageLocationName.equals(that.targetStorageLocationName) : that.targetStorageLocationName != null)
            return false;
        if (warehouseName != null ? !warehouseName.equals(that.warehouseName) : that.warehouseName != null)
            return false;
        if (materialNo != null ? !materialNo.equals(that.materialNo) : that.materialNo != null) return false;
        if (materialName != null ? !materialName.equals(that.materialName) : that.materialName != null) return false;
        if (supplierNo != null ? !supplierNo.equals(that.supplierNo) : that.supplierNo != null) return false;
        if (supplierName != null ? !supplierName.equals(that.supplierName) : that.supplierName != null) return false;
        if (supplyId != null ? !supplyId.equals(that.supplyId) : that.supplyId != null) return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (sourceStorageLocationId != null ? !sourceStorageLocationId.equals(that.sourceStorageLocationId) : that.sourceStorageLocationId != null)
            return false;
        if (sourceStorageLocationOriginalAmount != null ? !sourceStorageLocationOriginalAmount.equals(that.sourceStorageLocationOriginalAmount) : that.sourceStorageLocationOriginalAmount != null)
            return false;
        if (sourceStorageLocationNewAmount != null ? !sourceStorageLocationNewAmount.equals(that.sourceStorageLocationNewAmount) : that.sourceStorageLocationNewAmount != null)
            return false;
        if (sourceStorageLocationUnit != null ? !sourceStorageLocationUnit.equals(that.sourceStorageLocationUnit) : that.sourceStorageLocationUnit != null)
            return false;
        if (sourceStorageLocationUnitAmount != null ? !sourceStorageLocationUnitAmount.equals(that.sourceStorageLocationUnitAmount) : that.sourceStorageLocationUnitAmount != null)
            return false;
        if (targetStorageLocationId != null ? !targetStorageLocationId.equals(that.targetStorageLocationId) : that.targetStorageLocationId != null)
            return false;
        if (targetStorageLocationOriginalAmount != null ? !targetStorageLocationOriginalAmount.equals(that.targetStorageLocationOriginalAmount) : that.targetStorageLocationOriginalAmount != null)
            return false;
        if (targetStorageLocationNewAmount != null ? !targetStorageLocationNewAmount.equals(that.targetStorageLocationNewAmount) : that.targetStorageLocationNewAmount != null)
            return false;
        if (targetStorageLocationUnit != null ? !targetStorageLocationUnit.equals(that.targetStorageLocationUnit) : that.targetStorageLocationUnit != null)
            return false;
        if (targetStorageLocationAmount != null ? !targetStorageLocationAmount.equals(that.targetStorageLocationAmount) : that.targetStorageLocationAmount != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + warehouseId;
        result = 31 * result + (sourceStorageLocationNo != null ? sourceStorageLocationNo.hashCode() : 0);
        result = 31 * result + (sourceStorageLocationName != null ? sourceStorageLocationName.hashCode() : 0);
        result = 31 * result + (targetStorageLocationNo != null ? targetStorageLocationNo.hashCode() : 0);
        result = 31 * result + (targetStorageLocationName != null ? targetStorageLocationName.hashCode() : 0);
        result = 31 * result + (warehouseName != null ? warehouseName.hashCode() : 0);
        result = 31 * result + (materialNo != null ? materialNo.hashCode() : 0);
        result = 31 * result + (materialName != null ? materialName.hashCode() : 0);
        result = 31 * result + (supplierNo != null ? supplierNo.hashCode() : 0);
        result = 31 * result + (supplierName != null ? supplierName.hashCode() : 0);
        result = 31 * result + (supplyId != null ? supplyId.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (sourceStorageLocationId != null ? sourceStorageLocationId.hashCode() : 0);
        result = 31 * result + (sourceStorageLocationOriginalAmount != null ? sourceStorageLocationOriginalAmount.hashCode() : 0);
        result = 31 * result + (sourceStorageLocationNewAmount != null ? sourceStorageLocationNewAmount.hashCode() : 0);
        result = 31 * result + (sourceStorageLocationUnit != null ? sourceStorageLocationUnit.hashCode() : 0);
        result = 31 * result + (sourceStorageLocationUnitAmount != null ? sourceStorageLocationUnitAmount.hashCode() : 0);
        result = 31 * result + (targetStorageLocationId != null ? targetStorageLocationId.hashCode() : 0);
        result = 31 * result + (targetStorageLocationOriginalAmount != null ? targetStorageLocationOriginalAmount.hashCode() : 0);
        result = 31 * result + (targetStorageLocationNewAmount != null ? targetStorageLocationNewAmount.hashCode() : 0);
        result = 31 * result + (targetStorageLocationUnit != null ? targetStorageLocationUnit.hashCode() : 0);
        result = 31 * result + (targetStorageLocationAmount != null ? targetStorageLocationAmount.hashCode() : 0);
        return result;
    }
}
