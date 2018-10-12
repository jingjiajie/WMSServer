package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class WarehouseEntryItemView {
    private int id;
    private int warehouseEntryId;
    private int supplyId;
    private int storageLocationId;
    private BigDecimal expectedAmount;
    private BigDecimal realAmount;
    private String unit;
    private BigDecimal unitAmount;
    private BigDecimal inspectionAmount;
    private int state;
    private BigDecimal refuseAmount;
    private String refuseUnit;
    private BigDecimal refuseUnitAmount;
    private Integer personId;
    private String comment;
    private String manufactureNo;
    private Timestamp inventoryDate;
    private Timestamp manufactureDate;
    private Timestamp expiryDate;
    private Integer qualifiedStorageLocationId;
    private Integer unqualifiedStorageLocationId;
    private String warehouseEntryNo;
    private Timestamp warehouseEntryCreateTime;
    private Integer materialId;
    private String materialNo;
    private String materialName;
    private String materialProductLine;
    private Integer supplierId;
    private String supplierNo;
    private String supplierName;
    private String storageLocationNo;
    private String storageLocationName;
    private String personName;
    private String supplySerialNo;
    private BigDecimal supplyDefaultInspectionAmount;
    private String supplyDefaultInspectionUnit;
    private BigDecimal supplyDefaultInspectionUnitAmount;
    private String supplyDefaultInspectionStorageLocationNo;
    private String qualifiedStorageLocationNo;
    private String qualifiedStorageLocationName;
    private String unqualifiedStorageLocationNo;
    private String unqualifiedStorageLocationName;

    @Id
    @Basic
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "WarehouseEntryID")
    public int getWarehouseEntryId() {
        return warehouseEntryId;
    }

    public void setWarehouseEntryId(int warehouseEntryId) {
        this.warehouseEntryId = warehouseEntryId;
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
    @Column(name = "StorageLocationID")
    public int getStorageLocationId() {
        return storageLocationId;
    }

    public void setStorageLocationId(int storageLocationId) {
        this.storageLocationId = storageLocationId;
    }

    @Basic
    @Column(name = "ExpectedAmount")
    public BigDecimal getExpectedAmount() {
        return expectedAmount;
    }

    public void setExpectedAmount(BigDecimal expectedAmount) {
        this.expectedAmount = expectedAmount;
    }

    @Basic
    @Column(name = "RealAmount")
    public BigDecimal getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
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
    @Column(name = "InspectionAmount")
    public BigDecimal getInspectionAmount() {
        return inspectionAmount;
    }

    public void setInspectionAmount(BigDecimal inspectionAmount) {
        this.inspectionAmount = inspectionAmount;
    }

    @Basic
    @Column(name = "State")
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Basic
    @Column(name = "RefuseAmount")
    public BigDecimal getRefuseAmount() {
        return refuseAmount;
    }

    public void setRefuseAmount(BigDecimal refuseAmount) {
        this.refuseAmount = refuseAmount;
    }

    @Basic
    @Column(name = "RefuseUnit")
    public String getRefuseUnit() {
        return refuseUnit;
    }

    public void setRefuseUnit(String refuseUnit) {
        this.refuseUnit = refuseUnit;
    }

    @Basic
    @Column(name = "RefuseUnitAmount")
    public BigDecimal getRefuseUnitAmount() {
        return refuseUnitAmount;
    }

    public void setRefuseUnitAmount(BigDecimal refuseUnitAmount) {
        this.refuseUnitAmount = refuseUnitAmount;
    }

    @Basic
    @Column(name = "PersonID")
    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    @Basic
    @Column(name = "Comment")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Basic
    @Column(name = "ManufactureNo")
    public String getManufactureNo() {
        return manufactureNo;
    }

    public void setManufactureNo(String manufactureNo) {
        this.manufactureNo = manufactureNo;
    }

    @Basic
    @Column(name = "InventoryDate")
    public Timestamp getInventoryDate() {
        return inventoryDate;
    }

    public void setInventoryDate(Timestamp inventoryDate) {
        this.inventoryDate = inventoryDate;
    }

    @Basic
    @Column(name = "ManufactureDate")
    public Timestamp getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(Timestamp manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    @Basic
    @Column(name = "ExpiryDate")
    public Timestamp getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Timestamp expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Basic
    @Column(name = "QualifiedStorageLocationID")
    public Integer getQualifiedStorageLocationId() {
        return qualifiedStorageLocationId;
    }

    public void setQualifiedStorageLocationId(Integer qualifiedStorageLocationId) {
        this.qualifiedStorageLocationId = qualifiedStorageLocationId;
    }

    @Basic
    @Column(name = "UnqualifiedStorageLocationID")
    public Integer getUnqualifiedStorageLocationId() {
        return unqualifiedStorageLocationId;
    }

    public void setUnqualifiedStorageLocationId(Integer unqualifiedStorageLocationId) {
        this.unqualifiedStorageLocationId = unqualifiedStorageLocationId;
    }

    @Basic
    @Column(name = "WarehouseEntryNo")
    public String getWarehouseEntryNo() {
        return warehouseEntryNo;
    }

    public void setWarehouseEntryNo(String warehouseEntryNo) {
        this.warehouseEntryNo = warehouseEntryNo;
    }

    @Basic
    @Column(name = "WarehouseEntryCreateTime")
    public Timestamp getWarehouseEntryCreateTime() {
        return warehouseEntryCreateTime;
    }

    public void setWarehouseEntryCreateTime(Timestamp warehouseEntryCreateTime) {
        this.warehouseEntryCreateTime = warehouseEntryCreateTime;
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
    @Column(name = "MaterialProductLine")
    public String getMaterialProductLine() {
        return materialProductLine;
    }

    public void setMaterialProductLine(String materialProductLine) {
        this.materialProductLine = materialProductLine;
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
    @Column(name = "StorageLocationNo")
    public String getStorageLocationNo() {
        return storageLocationNo;
    }

    public void setStorageLocationNo(String storageLocationNo) {
        this.storageLocationNo = storageLocationNo;
    }

    @Basic
    @Column(name = "StorageLocationName")
    public String getStorageLocationName() {
        return storageLocationName;
    }

    public void setStorageLocationName(String storageLocationName) {
        this.storageLocationName = storageLocationName;
    }

    @Basic
    @Column(name = "PersonName")
    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
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
    @Column(name = "SupplyDefaultInspectionAmount")
    public BigDecimal getSupplyDefaultInspectionAmount() {
        return supplyDefaultInspectionAmount;
    }

    public void setSupplyDefaultInspectionAmount(BigDecimal supplyDefaultInspectionAmount) {
        this.supplyDefaultInspectionAmount = supplyDefaultInspectionAmount;
    }

    @Basic
    @Column(name = "SupplyDefaultInspectionUnit")
    public String getSupplyDefaultInspectionUnit() {
        return supplyDefaultInspectionUnit;
    }

    public void setSupplyDefaultInspectionUnit(String supplyDefaultInspectionUnit) {
        this.supplyDefaultInspectionUnit = supplyDefaultInspectionUnit;
    }

    @Basic
    @Column(name = "SupplyDefaultInspectionUnitAmount")
    public BigDecimal getSupplyDefaultInspectionUnitAmount() {
        return supplyDefaultInspectionUnitAmount;
    }

    public void setSupplyDefaultInspectionUnitAmount(BigDecimal supplyDefaultInspectionUnitAmount) {
        this.supplyDefaultInspectionUnitAmount = supplyDefaultInspectionUnitAmount;
    }

    @Basic
    @Column(name = "SupplyDefaultInspectionStorageLocationNo")
    public String getSupplyDefaultInspectionStorageLocationNo() {
        return supplyDefaultInspectionStorageLocationNo;
    }

    public void setSupplyDefaultInspectionStorageLocationNo(String supplyDefaultInspectionStorageLocationNo) {
        this.supplyDefaultInspectionStorageLocationNo = supplyDefaultInspectionStorageLocationNo;
    }

    @Basic
    @Column(name = "QualifiedStorageLocationNo")
    public String getQualifiedStorageLocationNo() {
        return qualifiedStorageLocationNo;
    }

    public void setQualifiedStorageLocationNo(String qualifiedStorageLocationNo) {
        this.qualifiedStorageLocationNo = qualifiedStorageLocationNo;
    }

    @Basic
    @Column(name = "QualifiedStorageLocationName")
    public String getQualifiedStorageLocationName() {
        return qualifiedStorageLocationName;
    }

    public void setQualifiedStorageLocationName(String qualifiedStorageLocationName) {
        this.qualifiedStorageLocationName = qualifiedStorageLocationName;
    }

    @Basic
    @Column(name = "UnqualifiedStorageLocationNo")
    public String getUnqualifiedStorageLocationNo() {
        return unqualifiedStorageLocationNo;
    }

    public void setUnqualifiedStorageLocationNo(String unqualifiedStorageLocationNo) {
        this.unqualifiedStorageLocationNo = unqualifiedStorageLocationNo;
    }

    @Basic
    @Column(name = "UnqualifiedStorageLocationName")
    public String getUnqualifiedStorageLocationName() {
        return unqualifiedStorageLocationName;
    }

    public void setUnqualifiedStorageLocationName(String unqualifiedStorageLocationName) {
        this.unqualifiedStorageLocationName = unqualifiedStorageLocationName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WarehouseEntryItemView that = (WarehouseEntryItemView) o;
        return id == that.id &&
                warehouseEntryId == that.warehouseEntryId &&
                supplyId == that.supplyId &&
                storageLocationId == that.storageLocationId &&
                state == that.state &&
                Objects.equals(expectedAmount, that.expectedAmount) &&
                Objects.equals(realAmount, that.realAmount) &&
                Objects.equals(unit, that.unit) &&
                Objects.equals(unitAmount, that.unitAmount) &&
                Objects.equals(inspectionAmount, that.inspectionAmount) &&
                Objects.equals(refuseAmount, that.refuseAmount) &&
                Objects.equals(refuseUnit, that.refuseUnit) &&
                Objects.equals(refuseUnitAmount, that.refuseUnitAmount) &&
                Objects.equals(personId, that.personId) &&
                Objects.equals(comment, that.comment) &&
                Objects.equals(manufactureNo, that.manufactureNo) &&
                Objects.equals(inventoryDate, that.inventoryDate) &&
                Objects.equals(manufactureDate, that.manufactureDate) &&
                Objects.equals(expiryDate, that.expiryDate) &&
                Objects.equals(qualifiedStorageLocationId, that.qualifiedStorageLocationId) &&
                Objects.equals(unqualifiedStorageLocationId, that.unqualifiedStorageLocationId) &&
                Objects.equals(warehouseEntryNo, that.warehouseEntryNo) &&
                Objects.equals(warehouseEntryCreateTime, that.warehouseEntryCreateTime) &&
                Objects.equals(materialId, that.materialId) &&
                Objects.equals(materialNo, that.materialNo) &&
                Objects.equals(materialName, that.materialName) &&
                Objects.equals(materialProductLine, that.materialProductLine) &&
                Objects.equals(supplierId, that.supplierId) &&
                Objects.equals(supplierNo, that.supplierNo) &&
                Objects.equals(supplierName, that.supplierName) &&
                Objects.equals(storageLocationNo, that.storageLocationNo) &&
                Objects.equals(storageLocationName, that.storageLocationName) &&
                Objects.equals(personName, that.personName) &&
                Objects.equals(supplySerialNo, that.supplySerialNo) &&
                Objects.equals(supplyDefaultInspectionAmount, that.supplyDefaultInspectionAmount) &&
                Objects.equals(supplyDefaultInspectionUnit, that.supplyDefaultInspectionUnit) &&
                Objects.equals(supplyDefaultInspectionUnitAmount, that.supplyDefaultInspectionUnitAmount) &&
                Objects.equals(supplyDefaultInspectionStorageLocationNo, that.supplyDefaultInspectionStorageLocationNo) &&
                Objects.equals(qualifiedStorageLocationNo, that.qualifiedStorageLocationNo) &&
                Objects.equals(qualifiedStorageLocationName, that.qualifiedStorageLocationName) &&
                Objects.equals(unqualifiedStorageLocationNo, that.unqualifiedStorageLocationNo) &&
                Objects.equals(unqualifiedStorageLocationName, that.unqualifiedStorageLocationName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, warehouseEntryId, supplyId, storageLocationId, expectedAmount, realAmount, unit, unitAmount, inspectionAmount, state, refuseAmount, refuseUnit, refuseUnitAmount, personId, comment, manufactureNo, inventoryDate, manufactureDate, expiryDate, qualifiedStorageLocationId, unqualifiedStorageLocationId, warehouseEntryNo, warehouseEntryCreateTime, materialId, materialNo, materialName, materialProductLine, supplierId, supplierNo, supplierName, storageLocationNo, storageLocationName, personName, supplySerialNo, supplyDefaultInspectionAmount, supplyDefaultInspectionUnit, supplyDefaultInspectionUnitAmount, supplyDefaultInspectionStorageLocationNo, qualifiedStorageLocationNo, qualifiedStorageLocationName, unqualifiedStorageLocationNo, unqualifiedStorageLocationName);
    }
}
