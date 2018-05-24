package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class InspectionNoteItemView {
    private int id;
    private int inspectionNoteId;
    private int warehouseEntryItemId;
    private int state;
    private BigDecimal amount;
    private String unit;
    private BigDecimal unitAmount;
    private int inspectionStorageLocationId;
    private BigDecimal returnAmount;
    private String returnUnit;
    private BigDecimal returnUnitAmount;
    private Integer returnStorageLocationId;
    private String comment;
    private Integer personId;
    private String inspectionNoteNo;
    private String materialNo;
    private String materialName;
    private String supplierNo;
    private String supplierName;
    private String personName;
    private String inspectionStorageLocationNo;
    private String inspectionStorageLocationName;
    private String returnStorageLocationNo;
    private String returnStorageLocationName;
    private String materialProductLine;
    private Integer supplierId;
    private Integer materialId;

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
    @Column(name = "InspectionNoteID", nullable = false)
    public int getInspectionNoteId() {
        return inspectionNoteId;
    }

    public void setInspectionNoteId(int inspectionNoteId) {
        this.inspectionNoteId = inspectionNoteId;
    }

    @Basic
    @Column(name = "WarehouseEntryItemID", nullable = false)
    public int getWarehouseEntryItemId() {
        return warehouseEntryItemId;
    }

    public void setWarehouseEntryItemId(int warehouseEntryItemId) {
        this.warehouseEntryItemId = warehouseEntryItemId;
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
    @Column(name = "InspectionStorageLocationID", nullable = false)
    public int getInspectionStorageLocationId() {
        return inspectionStorageLocationId;
    }

    public void setInspectionStorageLocationId(int inspectionStorageLocationId) {
        this.inspectionStorageLocationId = inspectionStorageLocationId;
    }

    @Basic
    @Column(name = "ReturnAmount", nullable = true, precision = 3)
    public BigDecimal getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(BigDecimal returnAmount) {
        this.returnAmount = returnAmount;
    }

    @Basic
    @Column(name = "ReturnUnit", nullable = true, length = 64)
    public String getReturnUnit() {
        return returnUnit;
    }

    public void setReturnUnit(String returnUnit) {
        this.returnUnit = returnUnit;
    }

    @Basic
    @Column(name = "ReturnUnitAmount", nullable = true, precision = 3)
    public BigDecimal getReturnUnitAmount() {
        return returnUnitAmount;
    }

    public void setReturnUnitAmount(BigDecimal returnUnitAmount) {
        this.returnUnitAmount = returnUnitAmount;
    }

    @Basic
    @Column(name = "ReturnStorageLocationID", nullable = true)
    public Integer getReturnStorageLocationId() {
        return returnStorageLocationId;
    }

    public void setReturnStorageLocationId(Integer returnStorageLocationId) {
        this.returnStorageLocationId = returnStorageLocationId;
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
    @Column(name = "PersonID", nullable = true)
    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    @Basic
    @Column(name = "InspectionNoteNo", nullable = true, length = 64)
    public String getInspectionNoteNo() {
        return inspectionNoteNo;
    }

    public void setInspectionNoteNo(String inspectionNoteNo) {
        this.inspectionNoteNo = inspectionNoteNo;
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
    @Column(name = "InspectionStorageLocationNo", nullable = true, length = 64)
    public String getInspectionStorageLocationNo() {
        return inspectionStorageLocationNo;
    }

    public void setInspectionStorageLocationNo(String inspectionStorageLocationNo) {
        this.inspectionStorageLocationNo = inspectionStorageLocationNo;
    }

    @Basic
    @Column(name = "InspectionStorageLocationName", nullable = true, length = 64)
    public String getInspectionStorageLocationName() {
        return inspectionStorageLocationName;
    }

    public void setInspectionStorageLocationName(String inspectionStorageLocationName) {
        this.inspectionStorageLocationName = inspectionStorageLocationName;
    }

    @Basic
    @Column(name = "ReturnStorageLocationNo", nullable = true, length = 64)
    public String getReturnStorageLocationNo() {
        return returnStorageLocationNo;
    }

    public void setReturnStorageLocationNo(String returnStorageLocationNo) {
        this.returnStorageLocationNo = returnStorageLocationNo;
    }

    @Basic
    @Column(name = "ReturnStorageLocationName", nullable = true, length = 64)
    public String getReturnStorageLocationName() {
        return returnStorageLocationName;
    }

    public void setReturnStorageLocationName(String returnStorageLocationName) {
        this.returnStorageLocationName = returnStorageLocationName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InspectionNoteItemView that = (InspectionNoteItemView) o;
        return id == that.id &&
                inspectionNoteId == that.inspectionNoteId &&
                warehouseEntryItemId == that.warehouseEntryItemId &&
                state == that.state &&
                inspectionStorageLocationId == that.inspectionStorageLocationId &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(unit, that.unit) &&
                Objects.equals(unitAmount, that.unitAmount) &&
                Objects.equals(returnAmount, that.returnAmount) &&
                Objects.equals(returnUnit, that.returnUnit) &&
                Objects.equals(returnUnitAmount, that.returnUnitAmount) &&
                Objects.equals(returnStorageLocationId, that.returnStorageLocationId) &&
                Objects.equals(comment, that.comment) &&
                Objects.equals(personId, that.personId) &&
                Objects.equals(inspectionNoteNo, that.inspectionNoteNo) &&
                Objects.equals(materialNo, that.materialNo) &&
                Objects.equals(materialName, that.materialName) &&
                Objects.equals(supplierNo, that.supplierNo) &&
                Objects.equals(supplierName, that.supplierName) &&
                Objects.equals(personName, that.personName) &&
                Objects.equals(inspectionStorageLocationNo, that.inspectionStorageLocationNo) &&
                Objects.equals(inspectionStorageLocationName, that.inspectionStorageLocationName) &&
                Objects.equals(returnStorageLocationNo, that.returnStorageLocationNo) &&
                Objects.equals(returnStorageLocationName, that.returnStorageLocationName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, inspectionNoteId, warehouseEntryItemId, state, amount, unit, unitAmount, inspectionStorageLocationId, returnAmount, returnUnit, returnUnitAmount, returnStorageLocationId, comment, personId, inspectionNoteNo, materialNo, materialName, supplierNo, supplierName, personName, inspectionStorageLocationNo, inspectionStorageLocationName, returnStorageLocationNo, returnStorageLocationName);
    }

    @Basic
    @Column(name = "MaterialProductLine", nullable = true, length = 64)
    public String getMaterialProductLine() {
        return materialProductLine;
    }

    public void setMaterialProductLine(String materialProductLine) {
        this.materialProductLine = materialProductLine;
    }

    @Basic
    @Column(name = "SupplierID", nullable = true)
    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    @Basic
    @Column(name = "MaterialID", nullable = true)
    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }
}
