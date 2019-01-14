package com.wms.utilities.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
public class TransferOrderItem {
    private int id;
    private int transferOrderId;
    private int supplyId;
    private int targetStorageLocationId;
    private int state;
    private BigDecimal scheduledAmount;
    private BigDecimal realAmount;
    private String unit;
    private BigDecimal unitAmount;
    private String comment;
    private Timestamp operateTime;
    private Integer personId;
    private int sourceStorageLocationId;
    private String sourceUnit;
    private BigDecimal sourceUnitAmount;
//    private Integer version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "TransferOrderID")
    public int getTransferOrderId() {
        return transferOrderId;
    }

    public void setTransferOrderId(int transferOrderId) {
        this.transferOrderId = transferOrderId;
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
    @Column(name = "TargetStorageLocationID")
    public int getTargetStorageLocationId() {
        return targetStorageLocationId;
    }

    public void setTargetStorageLocationId(int targetStorageLocationId) {
        this.targetStorageLocationId = targetStorageLocationId;
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
    @Column(name = "ScheduledAmount")
    public BigDecimal getScheduledAmount() {
        return scheduledAmount;
    }

    public void setScheduledAmount(BigDecimal scheduledAmount) {
        this.scheduledAmount = scheduledAmount;
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
    @Column(name = "Comment")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Basic
    @Column(name = "OperateTime")
    public Timestamp getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Timestamp operateTime) {
        this.operateTime = operateTime;
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
    @Column(name = "SourceStorageLocationID")
    public int getSourceStorageLocationId() {
        return sourceStorageLocationId;
    }

    public void setSourceStorageLocationId(int sourceStorageLocationId) {
        this.sourceStorageLocationId = sourceStorageLocationId;
    }

    @Basic
    @Column(name = "SourceUnit")
    public String getSourceUnit() {
        return sourceUnit;
    }

    public void setSourceUnit(String sourceUnit) {
        this.sourceUnit = sourceUnit;
    }

    @Basic
    @Column(name = "SourceUnitAmount")
    public BigDecimal getSourceUnitAmount() {
        return sourceUnitAmount;
    }

    public void setSourceUnitAmount(BigDecimal sourceUnitAmount) {
        this.sourceUnitAmount = sourceUnitAmount;
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

        TransferOrderItem that = (TransferOrderItem) o;

        if (id != that.id) return false;
        if (transferOrderId != that.transferOrderId) return false;
        if (supplyId != that.supplyId) return false;
        if (targetStorageLocationId != that.targetStorageLocationId) return false;
        if (state != that.state) return false;
        if (sourceStorageLocationId != that.sourceStorageLocationId) return false;
        if (scheduledAmount != null ? !scheduledAmount.equals(that.scheduledAmount) : that.scheduledAmount != null)
            return false;
        if (realAmount != null ? !realAmount.equals(that.realAmount) : that.realAmount != null) return false;
        if (unit != null ? !unit.equals(that.unit) : that.unit != null) return false;
        if (unitAmount != null ? !unitAmount.equals(that.unitAmount) : that.unitAmount != null) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        if (operateTime != null ? !operateTime.equals(that.operateTime) : that.operateTime != null) return false;
        if (personId != null ? !personId.equals(that.personId) : that.personId != null) return false;
        if (sourceUnit != null ? !sourceUnit.equals(that.sourceUnit) : that.sourceUnit != null) return false;
        if (sourceUnitAmount != null ? !sourceUnitAmount.equals(that.sourceUnitAmount) : that.sourceUnitAmount != null)
            return false;
//        if (version != null ? !version.equals(that.version) : that.version != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + transferOrderId;
        result = 31 * result + supplyId;
        result = 31 * result + targetStorageLocationId;
        result = 31 * result + state;
        result = 31 * result + (scheduledAmount != null ? scheduledAmount.hashCode() : 0);
        result = 31 * result + (realAmount != null ? realAmount.hashCode() : 0);
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + (unitAmount != null ? unitAmount.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (operateTime != null ? operateTime.hashCode() : 0);
        result = 31 * result + (personId != null ? personId.hashCode() : 0);
        result = 31 * result + sourceStorageLocationId;
        result = 31 * result + (sourceUnit != null ? sourceUnit.hashCode() : 0);
        result = 31 * result + (sourceUnitAmount != null ? sourceUnitAmount.hashCode() : 0);
//        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }
}
