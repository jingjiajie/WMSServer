package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class DeliveryOrderItemView {
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
    private String deliveryOrderNo;
    private String materialNo;
    private String materialName;
    private String supplierNo;
    private String supplierName;
    private String personName;
    private String sourceStorageLocationNo;
    private String sourceStorageLocationName;
    private String materialProductLine;
    private Integer supplierId;
    private Integer materialId;
    private Timestamp deliveryOrderCreateTime;
    private String supplySerialNo;

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

    @Basic
    @Column(name = "DeliveryOrderNo", nullable = true, length = 64)
    public String getDeliveryOrderNo() {
        return deliveryOrderNo;
    }

    public void setDeliveryOrderNo(String deliveryOrderNo) {
        this.deliveryOrderNo = deliveryOrderNo;
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
    @Column(name = "SourceStorageLocationNo", nullable = true, length = 64)
    public String getSourceStorageLocationNo() {
        return sourceStorageLocationNo;
    }

    public void setSourceStorageLocationNo(String sourceStorageLocationNo) {
        this.sourceStorageLocationNo = sourceStorageLocationNo;
    }

    @Basic
    @Column(name = "SourceStorageLocationName", nullable = true, length = 64)
    public String getSourceStorageLocationName() {
        return sourceStorageLocationName;
    }

    public void setSourceStorageLocationName(String sourceStorageLocationName) {
        this.sourceStorageLocationName = sourceStorageLocationName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryOrderItemView that = (DeliveryOrderItemView) o;
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
                Objects.equals(personId, that.personId) &&
                Objects.equals(deliveryOrderNo, that.deliveryOrderNo) &&
                Objects.equals(materialNo, that.materialNo) &&
                Objects.equals(materialName, that.materialName) &&
                Objects.equals(supplierNo, that.supplierNo) &&
                Objects.equals(supplierName, that.supplierName) &&
                Objects.equals(personName, that.personName) &&
                Objects.equals(sourceStorageLocationNo, that.sourceStorageLocationNo) &&
                Objects.equals(sourceStorageLocationName, that.sourceStorageLocationName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, deliveryOrderId, supplyId, sourceStorageLocationId, state, scheduledAmount, realAmount, loadingTime, unit, unitAmount, comment, personId, deliveryOrderNo, materialNo, materialName, supplierNo, supplierName, personName, sourceStorageLocationNo, sourceStorageLocationName);
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
    @Column(name = "MaterialID")
    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    @Basic
    @Column(name = "DeliveryOrderCreateTime")
    public Timestamp getDeliveryOrderCreateTime() {
        return deliveryOrderCreateTime;
    }

    public void setDeliveryOrderCreateTime(Timestamp deliveryOrderCreateTime) {
        this.deliveryOrderCreateTime = deliveryOrderCreateTime;
    }

    @Basic
    @Column(name = "SupplySerialNo")
    public String getSupplySerialNo() {
        return supplySerialNo;
    }

    public void setSupplySerialNo(String supplySerialNo) {
        this.supplySerialNo = supplySerialNo;
    }
}
