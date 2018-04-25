package com.wms.services.warehouse.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class TransferRecordView {
    private int id;
    private int warehouseId;
    private int sourceStockRecordId;
    private int newStockRecordId;
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

    @Id
    @Basic
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    @Column(name = "SourceStockRecordID", nullable = false)
    public int getSourceStockRecordId() {
        return sourceStockRecordId;
    }

    public void setSourceStockRecordId(int sourceStockRecordId) {
        this.sourceStockRecordId = sourceStockRecordId;
    }

    @Basic
    @Column(name = "NewStockRecordID", nullable = false)
    public int getNewStockRecordId() {
        return newStockRecordId;
    }

    public void setNewStockRecordId(int newStockRecordId) {
        this.newStockRecordId = newStockRecordId;
    }

    @Basic
    @Column(name = "OriginalAmount", nullable = true, precision = 3)
    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
    }

    @Basic
    @Column(name = "OriginalUnit", nullable = true, length = 64)
    public String getOriginalUnit() {
        return originalUnit;
    }

    public void setOriginalUnit(String originalUnit) {
        this.originalUnit = originalUnit;
    }

    @Basic
    @Column(name = "OriginalUnitAmount", nullable = true, precision = 3)
    public BigDecimal getOriginalUnitAmount() {
        return originalUnitAmount;
    }

    public void setOriginalUnitAmount(BigDecimal originalUnitAmount) {
        this.originalUnitAmount = originalUnitAmount;
    }

    @Basic
    @Column(name = "TransferAmount", nullable = true, precision = 3)
    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    @Basic
    @Column(name = "TransferUnit", nullable = true, length = 64)
    public String getTransferUnit() {
        return transferUnit;
    }

    public void setTransferUnit(String transferUnit) {
        this.transferUnit = transferUnit;
    }

    @Basic
    @Column(name = "TransferUnitAmount", nullable = true, precision = 3)
    public BigDecimal getTransferUnitAmount() {
        return transferUnitAmount;
    }

    public void setTransferUnitAmount(BigDecimal transferUnitAmount) {
        this.transferUnitAmount = transferUnitAmount;
    }

    @Basic
    @Column(name = "SourceStorageLocationID", nullable = true)
    public Integer getSourceStorageLocationId() {
        return sourceStorageLocationId;
    }

    public void setSourceStorageLocationId(Integer sourceStorageLocationId) {
        this.sourceStorageLocationId = sourceStorageLocationId;
    }

    @Basic
    @Column(name = "SourceStorageLocationNo", nullable = true, length = 64)
    public String getSourceStorageLocationNo() {
        return sourceStorageLocationNo;
    }

    public void setSourceStorageLocationNo(String sourceStorageLocationNo) {
        this.sourceStorageLocationNo = sourceStorageLocationNo;
    }

    @Basic
    @Column(name = "SourceStorageLocationName", nullable = true, length = 64)
    public String getSourceStorageLocationName() {
        return sourceStorageLocationName;
    }

    public void setSourceStorageLocationName(String sourceStorageLocationName) {
        this.sourceStorageLocationName = sourceStorageLocationName;
    }

    @Basic
    @Column(name = "NewStorageLocationID", nullable = true)
    public Integer getNewStorageLocationId() {
        return newStorageLocationId;
    }

    public void setNewStorageLocationId(Integer newStorageLocationId) {
        this.newStorageLocationId = newStorageLocationId;
    }

    @Basic
    @Column(name = "NewStorageLocationNo", nullable = true, length = 64)
    public String getNewStorageLocationNo() {
        return newStorageLocationNo;
    }

    public void setNewStorageLocationNo(String newStorageLocationNo) {
        this.newStorageLocationNo = newStorageLocationNo;
    }

    @Basic
    @Column(name = "NewStorageLocationName", nullable = true, length = 64)
    public String getNewStorageLocationName() {
        return newStorageLocationName;
    }

    public void setNewStorageLocationName(String newStorageLocationName) {
        this.newStorageLocationName = newStorageLocationName;
    }

    @Basic
    @Column(name = "WarehouseName", nullable = true, length = 64)
    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    @Basic
    @Column(name = "MaterialNo", nullable = true, length = 64)
    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    @Basic
    @Column(name = "MaterialName", nullable = true, length = 64)
    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    @Basic
    @Column(name = "SupplierNo", nullable = true, length = 64)
    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    @Basic
    @Column(name = "SupplierName", nullable = true, length = 64)
    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransferRecordView that = (TransferRecordView) o;
        return id == that.id &&
                warehouseId == that.warehouseId &&
                sourceStockRecordId == that.sourceStockRecordId &&
                newStockRecordId == that.newStockRecordId &&
                Objects.equals(originalAmount, that.originalAmount) &&
                Objects.equals(originalUnit, that.originalUnit) &&
                Objects.equals(originalUnitAmount, that.originalUnitAmount) &&
                Objects.equals(transferAmount, that.transferAmount) &&
                Objects.equals(transferUnit, that.transferUnit) &&
                Objects.equals(transferUnitAmount, that.transferUnitAmount) &&
                Objects.equals(sourceStorageLocationId, that.sourceStorageLocationId) &&
                Objects.equals(sourceStorageLocationNo, that.sourceStorageLocationNo) &&
                Objects.equals(sourceStorageLocationName, that.sourceStorageLocationName) &&
                Objects.equals(newStorageLocationId, that.newStorageLocationId) &&
                Objects.equals(newStorageLocationNo, that.newStorageLocationNo) &&
                Objects.equals(newStorageLocationName, that.newStorageLocationName) &&
                Objects.equals(warehouseName, that.warehouseName) &&
                Objects.equals(materialNo, that.materialNo) &&
                Objects.equals(materialName, that.materialName) &&
                Objects.equals(supplierNo, that.supplierNo) &&
                Objects.equals(supplierName, that.supplierName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, warehouseId, sourceStockRecordId, newStockRecordId, originalAmount, originalUnit, originalUnitAmount, transferAmount, transferUnit, transferUnitAmount, sourceStorageLocationId, sourceStorageLocationNo, sourceStorageLocationName, newStorageLocationId, newStorageLocationNo, newStorageLocationName, warehouseName, materialNo, materialName, supplierNo, supplierName);
    }
}
