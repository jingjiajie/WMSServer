package com.wms.services.warehouse.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class StockRecordView {
    private int id;
    private int warehouseId;
    private int storageLocationId;
    private int supplyId;
    private BigDecimal amount;
    private String unit;
    private BigDecimal unitAmount;
    private String relatedOrderNo;
    private Timestamp inventoryDate;
    private Timestamp manufactureDate;
    private Timestamp expiryDate;
    private Timestamp time;
    private String warehouseName;
    private String materialNo;
    private String materialName;
    private String supplierNo;
    private String supplierName;
    private String storageLocationNo;
    private String storageLocationName;

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
    @Column(name = "StorageLocationID", nullable = false)
    public int getStorageLocationId() {
        return storageLocationId;
    }

    public void setStorageLocationId(int storageLocationId) {
        this.storageLocationId = storageLocationId;
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
    @Column(name = "Amount", nullable = false, precision = 3)
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Basic
    @Column(name = "Unit", nullable = false, length = 64)
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Basic
    @Column(name = "UnitAmount", nullable = false, precision = 3)
    public BigDecimal getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(BigDecimal unitAmount) {
        this.unitAmount = unitAmount;
    }

    @Basic
    @Column(name = "RelatedOrderNo", nullable = true, length = 64)
    public String getRelatedOrderNo() {
        return relatedOrderNo;
    }

    public void setRelatedOrderNo(String relatedOrderNo) {
        this.relatedOrderNo = relatedOrderNo;
    }

    @Basic
    @Column(name = "InventoryDate", nullable = true)
    public Timestamp getInventoryDate() {
        return inventoryDate;
    }

    public void setInventoryDate(Timestamp inventoryDate) {
        this.inventoryDate = inventoryDate;
    }

    @Basic
    @Column(name = "ManufactureDate", nullable = true)
    public Timestamp getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(Timestamp manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    @Basic
    @Column(name = "ExpiryDate", nullable = true)
    public Timestamp getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Timestamp expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Basic
    @Column(name = "Time", nullable = false)
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
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

    @Basic
    @Column(name = "StorageLocationNo", nullable = true, length = 64)
    public String getStorageLocationNo() {
        return storageLocationNo;
    }

    public void setStorageLocationNo(String storageLocationNo) {
        this.storageLocationNo = storageLocationNo;
    }

    @Basic
    @Column(name = "StorageLocationName", nullable = true, length = 64)
    public String getStorageLocationName() {
        return storageLocationName;
    }

    public void setStorageLocationName(String storageLocationName) {
        this.storageLocationName = storageLocationName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockRecordView that = (StockRecordView) o;
        return id == that.id &&
                warehouseId == that.warehouseId &&
                storageLocationId == that.storageLocationId &&
                supplyId == that.supplyId &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(unit, that.unit) &&
                Objects.equals(unitAmount, that.unitAmount) &&
                Objects.equals(relatedOrderNo, that.relatedOrderNo) &&
                Objects.equals(inventoryDate, that.inventoryDate) &&
                Objects.equals(manufactureDate, that.manufactureDate) &&
                Objects.equals(expiryDate, that.expiryDate) &&
                Objects.equals(time, that.time) &&
                Objects.equals(warehouseName, that.warehouseName) &&
                Objects.equals(materialNo, that.materialNo) &&
                Objects.equals(materialName, that.materialName) &&
                Objects.equals(supplierNo, that.supplierNo) &&
                Objects.equals(supplierName, that.supplierName) &&
                Objects.equals(storageLocationNo, that.storageLocationNo) &&
                Objects.equals(storageLocationName, that.storageLocationName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, warehouseId, storageLocationId, supplyId, amount, unit, unitAmount, relatedOrderNo, inventoryDate, manufactureDate, expiryDate, time, warehouseName, materialNo, materialName, supplierNo, supplierName, storageLocationNo, storageLocationName);
    }
}
