package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;

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
    private String entryRandomCode;
    private Timestamp entryItemCreatTime;

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

    @Basic
    @Column(name = "EntryRandomCode")
    public String getEntryRandomCode() {
        return entryRandomCode;
    }

    public void setEntryRandomCode(String entryRandomCode) {
        this.entryRandomCode = entryRandomCode;
    }

    @Basic
    @Column(name = "EntryItemCreatTime")
    public Timestamp getEntryItemCreatTime() {
        return entryItemCreatTime;
    }

    public void setEntryItemCreatTime(Timestamp entryItemCreatTime) {
        this.entryItemCreatTime = entryItemCreatTime;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        WarehouseEntryItemView that = (WarehouseEntryItemView) object;

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
        if (warehouseEntryNo != null ? !warehouseEntryNo.equals(that.warehouseEntryNo) : that.warehouseEntryNo != null)
            return false;
        if (warehouseEntryCreateTime != null ? !warehouseEntryCreateTime.equals(that.warehouseEntryCreateTime) : that.warehouseEntryCreateTime != null)
            return false;
        if (materialId != null ? !materialId.equals(that.materialId) : that.materialId != null) return false;
        if (materialNo != null ? !materialNo.equals(that.materialNo) : that.materialNo != null) return false;
        if (materialName != null ? !materialName.equals(that.materialName) : that.materialName != null) return false;
        if (materialProductLine != null ? !materialProductLine.equals(that.materialProductLine) : that.materialProductLine != null)
            return false;
        if (supplierId != null ? !supplierId.equals(that.supplierId) : that.supplierId != null) return false;
        if (supplierNo != null ? !supplierNo.equals(that.supplierNo) : that.supplierNo != null) return false;
        if (supplierName != null ? !supplierName.equals(that.supplierName) : that.supplierName != null) return false;
        if (storageLocationNo != null ? !storageLocationNo.equals(that.storageLocationNo) : that.storageLocationNo != null)
            return false;
        if (storageLocationName != null ? !storageLocationName.equals(that.storageLocationName) : that.storageLocationName != null)
            return false;
        if (personName != null ? !personName.equals(that.personName) : that.personName != null) return false;
        if (supplySerialNo != null ? !supplySerialNo.equals(that.supplySerialNo) : that.supplySerialNo != null)
            return false;
        if (supplyDefaultInspectionAmount != null ? !supplyDefaultInspectionAmount.equals(that.supplyDefaultInspectionAmount) : that.supplyDefaultInspectionAmount != null)
            return false;
        if (supplyDefaultInspectionUnit != null ? !supplyDefaultInspectionUnit.equals(that.supplyDefaultInspectionUnit) : that.supplyDefaultInspectionUnit != null)
            return false;
        if (supplyDefaultInspectionUnitAmount != null ? !supplyDefaultInspectionUnitAmount.equals(that.supplyDefaultInspectionUnitAmount) : that.supplyDefaultInspectionUnitAmount != null)
            return false;
        if (supplyDefaultInspectionStorageLocationNo != null ? !supplyDefaultInspectionStorageLocationNo.equals(that.supplyDefaultInspectionStorageLocationNo) : that.supplyDefaultInspectionStorageLocationNo != null)
            return false;
        if (qualifiedStorageLocationNo != null ? !qualifiedStorageLocationNo.equals(that.qualifiedStorageLocationNo) : that.qualifiedStorageLocationNo != null)
            return false;
        if (qualifiedStorageLocationName != null ? !qualifiedStorageLocationName.equals(that.qualifiedStorageLocationName) : that.qualifiedStorageLocationName != null)
            return false;
        if (unqualifiedStorageLocationNo != null ? !unqualifiedStorageLocationNo.equals(that.unqualifiedStorageLocationNo) : that.unqualifiedStorageLocationNo != null)
            return false;
        if (unqualifiedStorageLocationName != null ? !unqualifiedStorageLocationName.equals(that.unqualifiedStorageLocationName) : that.unqualifiedStorageLocationName != null)
            return false;
        if (entryRandomCode != null ? !entryRandomCode.equals(that.entryRandomCode) : that.entryRandomCode != null)
            return false;
        if (entryItemCreatTime != null ? !entryItemCreatTime.equals(that.entryItemCreatTime) : that.entryItemCreatTime != null)
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
        result = 31 * result + (warehouseEntryNo != null ? warehouseEntryNo.hashCode() : 0);
        result = 31 * result + (warehouseEntryCreateTime != null ? warehouseEntryCreateTime.hashCode() : 0);
        result = 31 * result + (materialId != null ? materialId.hashCode() : 0);
        result = 31 * result + (materialNo != null ? materialNo.hashCode() : 0);
        result = 31 * result + (materialName != null ? materialName.hashCode() : 0);
        result = 31 * result + (materialProductLine != null ? materialProductLine.hashCode() : 0);
        result = 31 * result + (supplierId != null ? supplierId.hashCode() : 0);
        result = 31 * result + (supplierNo != null ? supplierNo.hashCode() : 0);
        result = 31 * result + (supplierName != null ? supplierName.hashCode() : 0);
        result = 31 * result + (storageLocationNo != null ? storageLocationNo.hashCode() : 0);
        result = 31 * result + (storageLocationName != null ? storageLocationName.hashCode() : 0);
        result = 31 * result + (personName != null ? personName.hashCode() : 0);
        result = 31 * result + (supplySerialNo != null ? supplySerialNo.hashCode() : 0);
        result = 31 * result + (supplyDefaultInspectionAmount != null ? supplyDefaultInspectionAmount.hashCode() : 0);
        result = 31 * result + (supplyDefaultInspectionUnit != null ? supplyDefaultInspectionUnit.hashCode() : 0);
        result = 31 * result + (supplyDefaultInspectionUnitAmount != null ? supplyDefaultInspectionUnitAmount.hashCode() : 0);
        result = 31 * result + (supplyDefaultInspectionStorageLocationNo != null ? supplyDefaultInspectionStorageLocationNo.hashCode() : 0);
        result = 31 * result + (qualifiedStorageLocationNo != null ? qualifiedStorageLocationNo.hashCode() : 0);
        result = 31 * result + (qualifiedStorageLocationName != null ? qualifiedStorageLocationName.hashCode() : 0);
        result = 31 * result + (unqualifiedStorageLocationNo != null ? unqualifiedStorageLocationNo.hashCode() : 0);
        result = 31 * result + (unqualifiedStorageLocationName != null ? unqualifiedStorageLocationName.hashCode() : 0);
        result = 31 * result + (entryRandomCode != null ? entryRandomCode.hashCode() : 0);
        result = 31 * result + (entryItemCreatTime != null ? entryItemCreatTime.hashCode() : 0);
        return result;
    }
}
