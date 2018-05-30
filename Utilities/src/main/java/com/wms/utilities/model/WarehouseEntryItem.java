package com.wms.utilities.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class WarehouseEntryItem {
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
    @Column(name = "WarehouseEntryID", nullable = false)
    public int getWarehouseEntryId() {
        return warehouseEntryId;
    }

    public void setWarehouseEntryId(int warehouseEntryId) {
        this.warehouseEntryId = warehouseEntryId;
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
    @Column(name = "StorageLocationID", nullable = false)
    public int getStorageLocationId() {
        return storageLocationId;
    }

    public void setStorageLocationId(int storageLocationId) {
        this.storageLocationId = storageLocationId;
    }

    @Basic
    @Column(name = "ExpectedAmount", nullable = false, precision = 3)
    public BigDecimal getExpectedAmount() {
        return expectedAmount;
    }

    public void setExpectedAmount(BigDecimal expectedAmount) {
        this.expectedAmount = expectedAmount;
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
    @Column(name = "InspectionAmount", nullable = false, precision = 3)
    public BigDecimal getInspectionAmount() {
        return inspectionAmount;
    }

    public void setInspectionAmount(BigDecimal inspectionAmount) {
        this.inspectionAmount = inspectionAmount;
    }

    @Basic
    @Column(name = "State", nullable = false)
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Basic
    @Column(name = "RefuseAmount", nullable = false, precision = 3)
    public BigDecimal getRefuseAmount() {
        return refuseAmount;
    }

    public void setRefuseAmount(BigDecimal refuseAmount) {
        this.refuseAmount = refuseAmount;
    }

    @Basic
    @Column(name = "RefuseUnit", nullable = false, length = 64)
    public String getRefuseUnit() {
        return refuseUnit;
    }

    public void setRefuseUnit(String refuseUnit) {
        this.refuseUnit = refuseUnit;
    }

    @Basic
    @Column(name = "RefuseUnitAmount", nullable = false, precision = 3)
    public BigDecimal getRefuseUnitAmount() {
        return refuseUnitAmount;
    }

    public void setRefuseUnitAmount(BigDecimal refuseUnitAmount) {
        this.refuseUnitAmount = refuseUnitAmount;
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
    @Column(name = "ManufactureNo", nullable = true, length = 64)
    public String getManufactureNo() {
        return manufactureNo;
    }

    public void setManufactureNo(String manufactureNo) {
        this.manufactureNo = manufactureNo;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WarehouseEntryItem that = (WarehouseEntryItem) o;
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
                Objects.equals(expiryDate, that.expiryDate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, warehouseEntryId, supplyId, storageLocationId, expectedAmount, realAmount, unit, unitAmount, inspectionAmount, state, refuseAmount, refuseUnit, refuseUnitAmount, personId, comment, manufactureNo, inventoryDate, manufactureDate, expiryDate);
    }

    @Basic
    @Column(name = "QualifiedStorageLocationID", nullable = true)
    public Integer getQualifiedStorageLocationId() {
        return qualifiedStorageLocationId;
    }

    public void setQualifiedStorageLocationId(Integer qualifiedStorageLocationId) {
        this.qualifiedStorageLocationId = qualifiedStorageLocationId;
    }

    @Basic
    @Column(name = "UnqualifiedStorageLocationID", nullable = true)
    public Integer getUnqualifiedStorageLocationId() {
        return unqualifiedStorageLocationId;
    }

    public void setUnqualifiedStorageLocationId(Integer unqualifiedStorageLocationId) {
        this.unqualifiedStorageLocationId = unqualifiedStorageLocationId;
    }
}
