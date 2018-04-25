package com.wms.services.warehouse.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class InspectionNoteItem {
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

    @Id
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InspectionNoteItem that = (InspectionNoteItem) o;
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
                Objects.equals(personId, that.personId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, inspectionNoteId, warehouseEntryItemId, state, amount, unit, unitAmount, inspectionStorageLocationId, returnAmount, returnUnit, returnUnitAmount, returnStorageLocationId, comment, personId);
    }
}
