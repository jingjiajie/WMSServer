package com.wms.utilities.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class InspectionNoteItem {
    private int id;
    private int inspectionNoteId;
    private int warehouseEntryItemId;
    private int state;
    private BigDecimal amount;
    private String unit;
    private BigDecimal unitAmount;
    private BigDecimal returnAmount;
    private String returnUnit;
    private BigDecimal returnUnitAmount;
    private String comment;
    private Integer personId;


    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "InspectionNoteID")
    public int getInspectionNoteId() {
        return inspectionNoteId;
    }

    public void setInspectionNoteId(int inspectionNoteId) {
        this.inspectionNoteId = inspectionNoteId;
    }

    @Basic
    @Column(name = "WarehouseEntryItemID")
    public int getWarehouseEntryItemId() {
        return warehouseEntryItemId;
    }

    public void setWarehouseEntryItemId(int warehouseEntryItemId) {
        this.warehouseEntryItemId = warehouseEntryItemId;
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
    @Column(name = "Amount")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
    @Column(name = "ReturnAmount")
    public BigDecimal getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(BigDecimal returnAmount) {
        this.returnAmount = returnAmount;
    }

    @Basic
    @Column(name = "ReturnUnit")
    public String getReturnUnit() {
        return returnUnit;
    }

    public void setReturnUnit(String returnUnit) {
        this.returnUnit = returnUnit;
    }

    @Basic
    @Column(name = "ReturnUnitAmount")
    public BigDecimal getReturnUnitAmount() {
        return returnUnitAmount;
    }

    public void setReturnUnitAmount(BigDecimal returnUnitAmount) {
        this.returnUnitAmount = returnUnitAmount;
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
    @Column(name = "PersonID")
    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InspectionNoteItem that = (InspectionNoteItem) o;

        if (id != that.id) return false;
        if (inspectionNoteId != that.inspectionNoteId) return false;
        if (warehouseEntryItemId != that.warehouseEntryItemId) return false;
        if (state != that.state) return false;
        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
        if (unit != null ? !unit.equals(that.unit) : that.unit != null) return false;
        if (unitAmount != null ? !unitAmount.equals(that.unitAmount) : that.unitAmount != null) return false;
        if (returnAmount != null ? !returnAmount.equals(that.returnAmount) : that.returnAmount != null) return false;
        if (returnUnit != null ? !returnUnit.equals(that.returnUnit) : that.returnUnit != null) return false;
        if (returnUnitAmount != null ? !returnUnitAmount.equals(that.returnUnitAmount) : that.returnUnitAmount != null)
            return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        if (personId != null ? !personId.equals(that.personId) : that.personId != null) return false;


        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + inspectionNoteId;
        result = 31 * result + warehouseEntryItemId;
        result = 31 * result + state;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + (unitAmount != null ? unitAmount.hashCode() : 0);
        result = 31 * result + (returnAmount != null ? returnAmount.hashCode() : 0);
        result = 31 * result + (returnUnit != null ? returnUnit.hashCode() : 0);
        result = 31 * result + (returnUnitAmount != null ? returnUnitAmount.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (personId != null ? personId.hashCode() : 0);

        return result;
    }
}
