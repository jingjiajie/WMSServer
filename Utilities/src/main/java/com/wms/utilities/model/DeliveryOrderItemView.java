package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;

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
    private Integer deliveryOrderType;

    private String deliveryRandomCode;
    private String destinationName;
    private Integer destinationId;

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

    @Basic
    @Column(name = "DeliveryOrderNo")
    public String getDeliveryOrderNo() {
        return deliveryOrderNo;
    }

    public void setDeliveryOrderNo(String deliveryOrderNo) {
        this.deliveryOrderNo = deliveryOrderNo;
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
    @Column(name = "PersonName")
    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    @Basic
    @Column(name = "SourceStorageLocationNo")
    public String getSourceStorageLocationNo() {
        return sourceStorageLocationNo;
    }

    public void setSourceStorageLocationNo(String sourceStorageLocationNo) {
        this.sourceStorageLocationNo = sourceStorageLocationNo;
    }

    @Basic
    @Column(name = "SourceStorageLocationName")
    public String getSourceStorageLocationName() {
        return sourceStorageLocationName;
    }

    public void setSourceStorageLocationName(String sourceStorageLocationName) {
        this.sourceStorageLocationName = sourceStorageLocationName;
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

    @Basic
    @Column(name = "DeliveryOrderType")
    public Integer getDeliveryOrderType() {
        return deliveryOrderType;
    }

    public void setDeliveryOrderType(Integer deliveryOrderType) {
        this.deliveryOrderType = deliveryOrderType;
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

    @Basic
    @Column(name = "DestinationName")
    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    @Basic
    @Column(name = "DestinationID")
    public Integer getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Integer destinationId) {
        this.destinationId = destinationId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        DeliveryOrderItemView that = (DeliveryOrderItemView) object;

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
        if (deliveryOrderNo != null ? !deliveryOrderNo.equals(that.deliveryOrderNo) : that.deliveryOrderNo != null)
            return false;
        if (materialNo != null ? !materialNo.equals(that.materialNo) : that.materialNo != null) return false;
        if (materialName != null ? !materialName.equals(that.materialName) : that.materialName != null) return false;
        if (supplierNo != null ? !supplierNo.equals(that.supplierNo) : that.supplierNo != null) return false;
        if (supplierName != null ? !supplierName.equals(that.supplierName) : that.supplierName != null) return false;
        if (personName != null ? !personName.equals(that.personName) : that.personName != null) return false;
        if (sourceStorageLocationNo != null ? !sourceStorageLocationNo.equals(that.sourceStorageLocationNo) : that.sourceStorageLocationNo != null)
            return false;
        if (sourceStorageLocationName != null ? !sourceStorageLocationName.equals(that.sourceStorageLocationName) : that.sourceStorageLocationName != null)
            return false;
        if (materialProductLine != null ? !materialProductLine.equals(that.materialProductLine) : that.materialProductLine != null)
            return false;
        if (supplierId != null ? !supplierId.equals(that.supplierId) : that.supplierId != null) return false;
        if (materialId != null ? !materialId.equals(that.materialId) : that.materialId != null) return false;
        if (deliveryOrderCreateTime != null ? !deliveryOrderCreateTime.equals(that.deliveryOrderCreateTime) : that.deliveryOrderCreateTime != null)
            return false;
        if (supplySerialNo != null ? !supplySerialNo.equals(that.supplySerialNo) : that.supplySerialNo != null)
            return false;
        if (deliveryOrderType != null ? !deliveryOrderType.equals(that.deliveryOrderType) : that.deliveryOrderType != null)
            return false;

        if (deliveryRandomCode != null ? !deliveryRandomCode.equals(that.deliveryRandomCode) : that.deliveryRandomCode != null)
            return false;
        if (destinationName != null ? !destinationName.equals(that.destinationName) : that.destinationName != null)
            return false;
        if (destinationId != null ? !destinationId.equals(that.destinationId) : that.destinationId != null)
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
        result = 31 * result + (deliveryOrderNo != null ? deliveryOrderNo.hashCode() : 0);
        result = 31 * result + (materialNo != null ? materialNo.hashCode() : 0);
        result = 31 * result + (materialName != null ? materialName.hashCode() : 0);
        result = 31 * result + (supplierNo != null ? supplierNo.hashCode() : 0);
        result = 31 * result + (supplierName != null ? supplierName.hashCode() : 0);
        result = 31 * result + (personName != null ? personName.hashCode() : 0);
        result = 31 * result + (sourceStorageLocationNo != null ? sourceStorageLocationNo.hashCode() : 0);
        result = 31 * result + (sourceStorageLocationName != null ? sourceStorageLocationName.hashCode() : 0);
        result = 31 * result + (materialProductLine != null ? materialProductLine.hashCode() : 0);
        result = 31 * result + (supplierId != null ? supplierId.hashCode() : 0);
        result = 31 * result + (materialId != null ? materialId.hashCode() : 0);
        result = 31 * result + (deliveryOrderCreateTime != null ? deliveryOrderCreateTime.hashCode() : 0);
        result = 31 * result + (supplySerialNo != null ? supplySerialNo.hashCode() : 0);
        result = 31 * result + (deliveryOrderType != null ? deliveryOrderType.hashCode() : 0);

        result = 31 * result + (deliveryRandomCode != null ? deliveryRandomCode.hashCode() : 0);
        result = 31 * result + (destinationName != null ? destinationName.hashCode() : 0);
        result = 31 * result + (destinationId != null ? destinationId.hashCode() : 0);
        return result;
    }
}
