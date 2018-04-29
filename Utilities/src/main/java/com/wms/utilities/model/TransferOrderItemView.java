package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class TransferOrderItemView {
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
    private String transferOrderNo;
    private String materialNo;
    private String materialName;
    private String supplierNo;
    private String supplierName;
    private String personName;
    private String targetStorageLocationNo;
    private String targetStorageLocationName;

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
    @Column(name = "TransferOrderID", nullable = false)
    public int getTransferOrderId() {
        return transferOrderId;
    }

    public void setTransferOrderId(int transferOrderId) {
        this.transferOrderId = transferOrderId;
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
    @Column(name = "TargetStorageLocationID", nullable = false)
    public int getTargetStorageLocationId() {
        return targetStorageLocationId;
    }

    public void setTargetStorageLocationId(int targetStorageLocationId) {
        this.targetStorageLocationId = targetStorageLocationId;
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
    @Column(name = "ScheduledAmount", nullable = false, precision = 3)
    public BigDecimal getScheduledAmount() {
        return scheduledAmount;
    }

    public void setScheduledAmount(BigDecimal scheduledAmount) {
        this.scheduledAmount = scheduledAmount;
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
    @Column(name = "Comment", nullable = true, length = 64)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Basic
    @Column(name = "OperateTime", nullable = true)
    public Timestamp getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Timestamp operateTime) {
        this.operateTime = operateTime;
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
    @Column(name = "TransferOrderNo", nullable = true, length = 64)
    public String getTransferOrderNo() {
        return transferOrderNo;
    }

    public void setTransferOrderNo(String transferOrderNo) {
        this.transferOrderNo = transferOrderNo;
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
    @Column(name = "TargetStorageLocationNo", nullable = true, length = 64)
    public String getTargetStorageLocationNo() {
        return targetStorageLocationNo;
    }

    public void setTargetStorageLocationNo(String targetStorageLocationNo) {
        this.targetStorageLocationNo = targetStorageLocationNo;
    }

    @Basic
    @Column(name = "TargetStorageLocationName", nullable = true, length = 64)
    public String getTargetStorageLocationName() {
        return targetStorageLocationName;
    }

    public void setTargetStorageLocationName(String targetStorageLocationName) {
        this.targetStorageLocationName = targetStorageLocationName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransferOrderItemView that = (TransferOrderItemView) o;
        return id == that.id &&
                transferOrderId == that.transferOrderId &&
                supplyId == that.supplyId &&
                targetStorageLocationId == that.targetStorageLocationId &&
                state == that.state &&
                Objects.equals(scheduledAmount, that.scheduledAmount) &&
                Objects.equals(realAmount, that.realAmount) &&
                Objects.equals(unit, that.unit) &&
                Objects.equals(unitAmount, that.unitAmount) &&
                Objects.equals(comment, that.comment) &&
                Objects.equals(operateTime, that.operateTime) &&
                Objects.equals(personId, that.personId) &&
                Objects.equals(transferOrderNo, that.transferOrderNo) &&
                Objects.equals(materialNo, that.materialNo) &&
                Objects.equals(materialName, that.materialName) &&
                Objects.equals(supplierNo, that.supplierNo) &&
                Objects.equals(supplierName, that.supplierName) &&
                Objects.equals(personName, that.personName) &&
                Objects.equals(targetStorageLocationNo, that.targetStorageLocationNo) &&
                Objects.equals(targetStorageLocationName, that.targetStorageLocationName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, transferOrderId, supplyId, targetStorageLocationId, state, scheduledAmount, realAmount, unit, unitAmount, comment, operateTime, personId, transferOrderNo, materialNo, materialName, supplierNo, supplierName, personName, targetStorageLocationNo, targetStorageLocationName);
    }
}
