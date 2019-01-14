package com.wms.utilities.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

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
//    private Integer version;
    private String deliveryRandomCode;

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
    @Column(name = "DeliveryOrderID")
    public Integer getDeliveryOrderId() {
        return deliveryOrderId;
    }

    public void setDeliveryOrderId(Integer deliveryOrderId) {
        this.deliveryOrderId = deliveryOrderId;
    }

    @Basic
    @Column(name = "SupplyID")
    public Integer getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(Integer supplyId) {
        this.supplyId = supplyId;
    }

    @Basic
    @Column(name = "SourceStorageLocationID")
    public Integer getSourceStorageLocationId() {
        return sourceStorageLocationId;
    }

    public void setSourceStorageLocationId(Integer sourceStorageLocationId) {
        this.sourceStorageLocationId = sourceStorageLocationId;
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
    @Column(name = "LoadingTime")
    public Timestamp getLoadingTime() {
        return loadingTime;
    }

    public void setLoadingTime(Timestamp loadingTime) {
        this.loadingTime = loadingTime;
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

    @Basic
    @Column(name = "DeliveryRandomCode")
    public String getDeliveryRandomCode() {
        return deliveryRandomCode;
    }

    public void setDeliveryRandomCode(String deliveryRandomCode) {
        this.deliveryRandomCode = deliveryRandomCode;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        DeliveryOrderItem that = (DeliveryOrderItem) object;

        if (id != that.id) return false;
        if (state != that.state) return false;
        if (deliveryOrderId != null ? !deliveryOrderId.equals(that.deliveryOrderId) : that.deliveryOrderId != null)
            return false;
        if (supplyId != null ? !supplyId.equals(that.supplyId) : that.supplyId != null) return false;
        if (sourceStorageLocationId != null ? !sourceStorageLocationId.equals(that.sourceStorageLocationId) : that.sourceStorageLocationId != null)
            return false;
        if (scheduledAmount != null ? !scheduledAmount.equals(that.scheduledAmount) : that.scheduledAmount != null)
            return false;
        if (realAmount != null ? !realAmount.equals(that.realAmount) : that.realAmount != null) return false;
        if (loadingTime != null ? !loadingTime.equals(that.loadingTime) : that.loadingTime != null) return false;
        if (unit != null ? !unit.equals(that.unit) : that.unit != null) return false;
        if (unitAmount != null ? !unitAmount.equals(that.unitAmount) : that.unitAmount != null) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        if (personId != null ? !personId.equals(that.personId) : that.personId != null) return false;
//        if (version != null ? !version.equals(that.version) : that.version != null) return false;
        if (deliveryRandomCode != null ? !deliveryRandomCode.equals(that.deliveryRandomCode) : that.deliveryRandomCode != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (deliveryOrderId != null ? deliveryOrderId.hashCode() : 0);
        result = 31 * result + (supplyId != null ? supplyId.hashCode() : 0);
        result = 31 * result + (sourceStorageLocationId != null ? sourceStorageLocationId.hashCode() : 0);
        result = 31 * result + state;
        result = 31 * result + (scheduledAmount != null ? scheduledAmount.hashCode() : 0);
        result = 31 * result + (realAmount != null ? realAmount.hashCode() : 0);
        result = 31 * result + (loadingTime != null ? loadingTime.hashCode() : 0);
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + (unitAmount != null ? unitAmount.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (personId != null ? personId.hashCode() : 0);
//        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (deliveryRandomCode != null ? deliveryRandomCode.hashCode() : 0);
        return result;
    }
}
