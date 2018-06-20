package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class TransferRecordView {
    private int id;
    private int warehouseId;
    private Integer sourceStockRecordId;
    private Integer newStockRecordId;
    private BigDecimal originalAmount;
    private String originalUnit;
    private BigDecimal originalUnitAmount;
    private BigDecimal transferAmount;
    private String transferUnit;
    private BigDecimal transferUnitAmount;
    private Integer sourceStorageLocationId;
    private String sourceStorageLocationNo;
    private String sourceStorageLocationName;
    private Integer newStorageLocationId;
    private String newStorageLocationNo;
    private String newStorageLocationName;
    private String warehouseName;
    private String materialNo;
    private String materialName;
    private String supplierNo;
    private String supplierName;
    private Integer supplyId;

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
    @Column(name = "SourceStockRecordID")
    public Integer getSourceStockRecordId() {
        return sourceStockRecordId;
    }

    public void setSourceStockRecordId(Integer sourceStockRecordId) {
        this.sourceStockRecordId = sourceStockRecordId;
    }

    @Basic
    @Column(name = "NewStockRecordID")
    public Integer getNewStockRecordId() {
        return newStockRecordId;
    }

    public void setNewStockRecordId(Integer newStockRecordId) {
        this.newStockRecordId = newStockRecordId;
    }

    @Basic
    @Column(name = "OriginalAmount")
    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
    }

    @Basic
    @Column(name = "OriginalUnit")
    public String getOriginalUnit() {
        return originalUnit;
    }

    public void setOriginalUnit(String originalUnit) {
        this.originalUnit = originalUnit;
    }

    @Basic
    @Column(name = "OriginalUnitAmount")
    public BigDecimal getOriginalUnitAmount() {
        return originalUnitAmount;
    }

    public void setOriginalUnitAmount(BigDecimal originalUnitAmount) {
        this.originalUnitAmount = originalUnitAmount;
    }

    @Basic
    @Column(name = "TransferAmount")
    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    @Basic
    @Column(name = "TransferUnit")
    public String getTransferUnit() {
        return transferUnit;
    }

    public void setTransferUnit(String transferUnit) {
        this.transferUnit = transferUnit;
    }

    @Basic
    @Column(name = "TransferUnitAmount")
    public BigDecimal getTransferUnitAmount() {
        return transferUnitAmount;
    }

    public void setTransferUnitAmount(BigDecimal transferUnitAmount) {
        this.transferUnitAmount = transferUnitAmount;
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
    @Column(name = "NewStorageLocationID")
    public Integer getNewStorageLocationId() {
        return newStorageLocationId;
    }

    public void setNewStorageLocationId(Integer newStorageLocationId) {
        this.newStorageLocationId = newStorageLocationId;
    }

    @Basic
    @Column(name = "NewStorageLocationNo")
    public String getNewStorageLocationNo() {
        return newStorageLocationNo;
    }

    public void setNewStorageLocationNo(String newStorageLocationNo) {
        this.newStorageLocationNo = newStorageLocationNo;
    }

    @Basic
    @Column(name = "NewStorageLocationName")
    public String getNewStorageLocationName() {
        return newStorageLocationName;
    }

    public void setNewStorageLocationName(String newStorageLocationName) {
        this.newStorageLocationName = newStorageLocationName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransferRecordView that = (TransferRecordView) o;

        if (id != that.id) return false;
        if (warehouseId != that.warehouseId) return false;
        if (sourceStockRecordId != null ? !sourceStockRecordId.equals(that.sourceStockRecordId) : that.sourceStockRecordId != null)
            return false;
        if (newStockRecordId != null ? !newStockRecordId.equals(that.newStockRecordId) : that.newStockRecordId != null)
            return false;
        if (originalAmount != null ? !originalAmount.equals(that.originalAmount) : that.originalAmount != null)
            return false;
        if (originalUnit != null ? !originalUnit.equals(that.originalUnit) : that.originalUnit != null) return false;
        if (originalUnitAmount != null ? !originalUnitAmount.equals(that.originalUnitAmount) : that.originalUnitAmount != null)
            return false;
        if (transferAmount != null ? !transferAmount.equals(that.transferAmount) : that.transferAmount != null)
            return false;
        if (transferUnit != null ? !transferUnit.equals(that.transferUnit) : that.transferUnit != null) return false;
        if (transferUnitAmount != null ? !transferUnitAmount.equals(that.transferUnitAmount) : that.transferUnitAmount != null)
            return false;
        if (sourceStorageLocationId != null ? !sourceStorageLocationId.equals(that.sourceStorageLocationId) : that.sourceStorageLocationId != null)
            return false;
        if (sourceStorageLocationNo != null ? !sourceStorageLocationNo.equals(that.sourceStorageLocationNo) : that.sourceStorageLocationNo != null)
            return false;
        if (sourceStorageLocationName != null ? !sourceStorageLocationName.equals(that.sourceStorageLocationName) : that.sourceStorageLocationName != null)
            return false;
        if (newStorageLocationId != null ? !newStorageLocationId.equals(that.newStorageLocationId) : that.newStorageLocationId != null)
            return false;
        if (newStorageLocationNo != null ? !newStorageLocationNo.equals(that.newStorageLocationNo) : that.newStorageLocationNo != null)
            return false;
        if (newStorageLocationName != null ? !newStorageLocationName.equals(that.newStorageLocationName) : that.newStorageLocationName != null)
            return false;
        if (warehouseName != null ? !warehouseName.equals(that.warehouseName) : that.warehouseName != null)
            return false;
        if (materialNo != null ? !materialNo.equals(that.materialNo) : that.materialNo != null) return false;
        if (materialName != null ? !materialName.equals(that.materialName) : that.materialName != null) return false;
        if (supplierNo != null ? !supplierNo.equals(that.supplierNo) : that.supplierNo != null) return false;
        if (supplierName != null ? !supplierName.equals(that.supplierName) : that.supplierName != null) return false;
        if (supplyId != null ? !supplyId.equals(that.supplyId) : that.supplyId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + warehouseId;
        result = 31 * result + (sourceStockRecordId != null ? sourceStockRecordId.hashCode() : 0);
        result = 31 * result + (newStockRecordId != null ? newStockRecordId.hashCode() : 0);
        result = 31 * result + (originalAmount != null ? originalAmount.hashCode() : 0);
        result = 31 * result + (originalUnit != null ? originalUnit.hashCode() : 0);
        result = 31 * result + (originalUnitAmount != null ? originalUnitAmount.hashCode() : 0);
        result = 31 * result + (transferAmount != null ? transferAmount.hashCode() : 0);
        result = 31 * result + (transferUnit != null ? transferUnit.hashCode() : 0);
        result = 31 * result + (transferUnitAmount != null ? transferUnitAmount.hashCode() : 0);
        result = 31 * result + (sourceStorageLocationId != null ? sourceStorageLocationId.hashCode() : 0);
        result = 31 * result + (sourceStorageLocationNo != null ? sourceStorageLocationNo.hashCode() : 0);
        result = 31 * result + (sourceStorageLocationName != null ? sourceStorageLocationName.hashCode() : 0);
        result = 31 * result + (newStorageLocationId != null ? newStorageLocationId.hashCode() : 0);
        result = 31 * result + (newStorageLocationNo != null ? newStorageLocationNo.hashCode() : 0);
        result = 31 * result + (newStorageLocationName != null ? newStorageLocationName.hashCode() : 0);
        result = 31 * result + (warehouseName != null ? warehouseName.hashCode() : 0);
        result = 31 * result + (materialNo != null ? materialNo.hashCode() : 0);
        result = 31 * result + (materialName != null ? materialName.hashCode() : 0);
        result = 31 * result + (supplierNo != null ? supplierNo.hashCode() : 0);
        result = 31 * result + (supplierName != null ? supplierName.hashCode() : 0);
        result = 31 * result + (supplyId != null ? supplyId.hashCode() : 0);
        return result;
    }
}
