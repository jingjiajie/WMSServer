package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class DeliveryOrderItem {
    private int id;
    private Integer deliveryOrderId;
    private Integer supplyId;
    private Integer sourceStorageLocationId;
    private int state;
    private BigDecimal scheduledAmount;
    private BigDecimal realAmount;
    private Timestamp loadingTime;
    private String unit;
    private BigDecimal unitAmount;
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
    @Column(name = "DeliveryOrderID", nullable = true)
    public Integer getDeliveryOrderId() {
        return deliveryOrderId;
    }

    public void setDeliveryOrderId(Integer deliveryOrderId) {
        this.deliveryOrderId = deliveryOrderId;
    }

    @Basic
    @Column(name = "SupplyID", nullable = true)
    public Integer getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(Integer supplyId) {
        this.supplyId = supplyId;
    }

    @Basic
    @Column(name = "SourceStorageLocationID", nullable = true)
    public Integer getSourceStorageLocationId() {
        return sourceStorageLocationId;
    }

    public void setSourceStorageLocationId(Integer sourceStorageLocationId) {
        this.sourceStorageLocationId = sourceStorageLocationId;
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
    @Column(name = "ScheduledAmount", nullable = true, precision = 3)
    public BigDecimal getScheduledAmount() {
        return scheduledAmount;
    }

    public void setScheduledAmount(BigDecimal scheduledAmount) {
        this.scheduledAmount = scheduledAmount;
    }

    @Basic
    @Column(name = "RealAmount", nullable = true, precision = 3)
    public BigDecimal getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
    }

    @Basic
    @Column(name = "LoadingTime", nullable = true)
    public Timestamp getLoadingTime() {
        return loadingTime;
    }

    public void setLoadingTime(Timestamp loadingTime) {
        this.loadingTime = loadingTime;
    }

    @Basic
    @Column(name = "Unit", nullable = true, length = 64)
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Basic
    @Column(name = "UnitAmount", nullable = true, precision = 3)
    public BigDecimal getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(BigDecimal unitAmount) {
        this.unitAmount = unitAmount;
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
        DeliveryOrderItem that = (DeliveryOrderItem) o;
        return id == that.id &&
                state == that.state &&
                Objects.equals(deliveryOrderId, that.deliveryOrderId) &&
                Objects.equals(supplyId, that.supplyId) &&
                Objects.equals(sourceStorageLocationId, that.sourceStorageLocationId) &&
                Objects.equals(scheduledAmount, that.scheduledAmount) &&
                Objects.equals(realAmount, that.realAmount) &&
                Objects.equals(loadingTime, that.loadingTime) &&
                Objects.equals(unit, that.unit) &&
                Objects.equals(unitAmount, that.unitAmount) &&
                Objects.equals(comment, that.comment) &&
                Objects.equals(personId, that.personId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, deliveryOrderId, supplyId, sourceStorageLocationId, state, scheduledAmount, realAmount, loadingTime, unit, unitAmount, comment, personId);
    }
}
