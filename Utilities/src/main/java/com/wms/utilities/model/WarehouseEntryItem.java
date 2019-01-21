package com.wms.utilities.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

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

    private String entryRandomCode;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

//    @Basic
//    @Column(name = "Version")
//    public Integer getVersion() {
//        return version;
//    }
//
//    public void setVersion(Integer version) {
//        this.version = version;
//    }

    @Basic
    @Column(name = "EntryRandomCode")
    public String getEntryRandomCode() {
        return entryRandomCode;
    }

    public void setEntryRandomCode(String entryRandomCode) {
        this.entryRandomCode = entryRandomCode;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        WarehouseEntryItem that = (WarehouseEntryItem) object;

        if (id != that.id) return false;
        if (warehouseEntryId != that.warehouseEntryId) return false;
        if (supplyId != that.supplyId) return false;
        if (storageLocationId != that.storageLocationId) return false;
        if (state != that.state) return false;
        if (expectedAmount != null ? !expectedAmount.equals(that.expectedAmount) : that.expectedAmount != null)
            return false;
        if (realAmount != null ? !realAmount.equals(that.realAmount) : that.realAmount != null) return false;
        if (unit != null ? !unit.equals(that.unit) : that.unit != null) return false;
        if (unitAmount != null ? !unitAmount.equals(that.unitAmount) : that.unitAmount != null) return false;
        if (inspectionAmount != null ? !inspectionAmount.equals(that.inspectionAmount) : that.inspectionAmount != null)
            return false;
        if (refuseAmount != null ? !refuseAmount.equals(that.refuseAmount) : that.refuseAmount != null) return false;
        if (refuseUnit != null ? !refuseUnit.equals(that.refuseUnit) : that.refuseUnit != null) return false;
        if (refuseUnitAmount != null ? !refuseUnitAmount.equals(that.refuseUnitAmount) : that.refuseUnitAmount != null)
            return false;
        if (personId != null ? !personId.equals(that.personId) : that.personId != null) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        if (manufactureNo != null ? !manufactureNo.equals(that.manufactureNo) : that.manufactureNo != null)
            return false;
        if (inventoryDate != null ? !inventoryDate.equals(that.inventoryDate) : that.inventoryDate != null)
            return false;
        if (manufactureDate != null ? !manufactureDate.equals(that.manufactureDate) : that.manufactureDate != null)
            return false;
        if (expiryDate != null ? !expiryDate.equals(that.expiryDate) : that.expiryDate != null) return false;
        if (qualifiedStorageLocationId != null ? !qualifiedStorageLocationId.equals(that.qualifiedStorageLocationId) : that.qualifiedStorageLocationId != null)
            return false;
        if (unqualifiedStorageLocationId != null ? !unqualifiedStorageLocationId.equals(that.unqualifiedStorageLocationId) : that.unqualifiedStorageLocationId != null)
            return false;

        if (entryRandomCode != null ? !entryRandomCode.equals(that.entryRandomCode) : that.entryRandomCode != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + warehouseEntryId;
        result = 31 * result + supplyId;
        result = 31 * result + storageLocationId;
        result = 31 * result + (expectedAmount != null ? expectedAmount.hashCode() : 0);
        result = 31 * result + (realAmount != null ? realAmount.hashCode() : 0);
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + (unitAmount != null ? unitAmount.hashCode() : 0);
        result = 31 * result + (inspectionAmount != null ? inspectionAmount.hashCode() : 0);
        result = 31 * result + state;
        result = 31 * result + (refuseAmount != null ? refuseAmount.hashCode() : 0);
        result = 31 * result + (refuseUnit != null ? refuseUnit.hashCode() : 0);
        result = 31 * result + (refuseUnitAmount != null ? refuseUnitAmount.hashCode() : 0);
        result = 31 * result + (personId != null ? personId.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (manufactureNo != null ? manufactureNo.hashCode() : 0);
        result = 31 * result + (inventoryDate != null ? inventoryDate.hashCode() : 0);
        result = 31 * result + (manufactureDate != null ? manufactureDate.hashCode() : 0);
        result = 31 * result + (expiryDate != null ? expiryDate.hashCode() : 0);
        result = 31 * result + (qualifiedStorageLocationId != null ? qualifiedStorageLocationId.hashCode() : 0);
        result = 31 * result + (unqualifiedStorageLocationId != null ? unqualifiedStorageLocationId.hashCode() : 0);

        result = 31 * result + (entryRandomCode != null ? entryRandomCode.hashCode() : 0);
        return result;
    }
}
