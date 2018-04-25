package com.wms.services.warehouse.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class StockTakingOrderItemView {
    private int id;
    private int stockTakingOrderId;
    private Integer storageLocationId;
    private int supplyId;
    private BigDecimal amount;
    private BigDecimal realAmount;
    private String unit;
    private BigDecimal unitAmount;
    private Integer personId;
    private String comment;
    private String stockTakingOrderNo;
    private String materialNo;
    private String materialName;
    private String supplierNo;
    private String supplierName;
    private String personName;
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
    @Column(name = "StockTakingOrderID", nullable = false)
    public int getStockTakingOrderId() {
        return stockTakingOrderId;
    }

    public void setStockTakingOrderId(int stockTakingOrderId) {
        this.stockTakingOrderId = stockTakingOrderId;
    }

    @Basic
    @Column(name = "StorageLocationID", nullable = true)
    public Integer getStorageLocationId() {
        return storageLocationId;
    }

    public void setStorageLocationId(Integer storageLocationId) {
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
    @Column(name = "RealAmount", nullable = false, precision = 3)
    public BigDecimal getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
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
    @Column(name = "PersonID", nullable = true)
    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    @Basic
    @Column(name = "Comment", nullable = true, length = 64)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Basic
    @Column(name = "StockTakingOrderNo", nullable = true, length = 64)
    public String getStockTakingOrderNo() {
        return stockTakingOrderNo;
    }

    public void setStockTakingOrderNo(String stockTakingOrderNo) {
        this.stockTakingOrderNo = stockTakingOrderNo;
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
    @Column(name = "PersonName", nullable = true, length = 64)
    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
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
        StockTakingOrderItemView that = (StockTakingOrderItemView) o;
        return id == that.id &&
                stockTakingOrderId == that.stockTakingOrderId &&
                supplyId == that.supplyId &&
                Objects.equals(storageLocationId, that.storageLocationId) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(realAmount, that.realAmount) &&
                Objects.equals(unit, that.unit) &&
                Objects.equals(unitAmount, that.unitAmount) &&
                Objects.equals(personId, that.personId) &&
                Objects.equals(comment, that.comment) &&
                Objects.equals(stockTakingOrderNo, that.stockTakingOrderNo) &&
                Objects.equals(materialNo, that.materialNo) &&
                Objects.equals(materialName, that.materialName) &&
                Objects.equals(supplierNo, that.supplierNo) &&
                Objects.equals(supplierName, that.supplierName) &&
                Objects.equals(personName, that.personName) &&
                Objects.equals(storageLocationNo, that.storageLocationNo) &&
                Objects.equals(storageLocationName, that.storageLocationName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, stockTakingOrderId, storageLocationId, supplyId, amount, realAmount, unit, unitAmount, personId, comment, stockTakingOrderNo, materialNo, materialName, supplierNo, supplierName, personName, storageLocationNo, storageLocationName);
    }
}
