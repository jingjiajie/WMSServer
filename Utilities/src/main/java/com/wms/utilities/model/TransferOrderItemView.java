package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;

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
    private int sourceStorageLocationId;
    private String sourceStorageLocationNo;
    private String sourceStorageLocationName;
    private String materialProductLine;
    private Integer supplierId;
    private Integer materialId;
    private String sourceUnit;
    private BigDecimal sourceUnitAmount;
    private Timestamp transferOrderCreateTime;
    private String supplySerialNo;

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
    @Column(name = "TransferOrderNo")
    public String getTransferOrderNo() {
        return transferOrderNo;
    }

    public void setTransferOrderNo(String transferOrderNo) {
        this.transferOrderNo = transferOrderNo;
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
    @Column(name = "TargetStorageLocationNo")
    public String getTargetStorageLocationNo() {
        return targetStorageLocationNo;
    }

    public void setTargetStorageLocationNo(String targetStorageLocationNo) {
        this.targetStorageLocationNo = targetStorageLocationNo;
    }

    @Basic
    @Column(name = "TargetStorageLocationName")
    public String getTargetStorageLocationName() {
        return targetStorageLocationName;
    }

    public void setTargetStorageLocationName(String targetStorageLocationName) {
        this.targetStorageLocationName = targetStorageLocationName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransferOrderItemView that = (TransferOrderItemView) o;

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
        if (transferOrderNo != null ? !transferOrderNo.equals(that.transferOrderNo) : that.transferOrderNo != null)
            return false;
        if (materialNo != null ? !materialNo.equals(that.materialNo) : that.materialNo != null) return false;
        if (materialName != null ? !materialName.equals(that.materialName) : that.materialName != null) return false;
        if (supplierNo != null ? !supplierNo.equals(that.supplierNo) : that.supplierNo != null) return false;
        if (supplierName != null ? !supplierName.equals(that.supplierName) : that.supplierName != null) return false;
        if (personName != null ? !personName.equals(that.personName) : that.personName != null) return false;
        if (targetStorageLocationNo != null ? !targetStorageLocationNo.equals(that.targetStorageLocationNo) : that.targetStorageLocationNo != null)
            return false;
        if (targetStorageLocationName != null ? !targetStorageLocationName.equals(that.targetStorageLocationName) : that.targetStorageLocationName != null)
            return false;
        if (sourceStorageLocationNo != null ? !sourceStorageLocationNo.equals(that.sourceStorageLocationNo) : that.sourceStorageLocationNo != null)
            return false;
        if (sourceStorageLocationName != null ? !sourceStorageLocationName.equals(that.sourceStorageLocationName) : that.sourceStorageLocationName != null)
            return false;
        if (materialProductLine != null ? !materialProductLine.equals(that.materialProductLine) : that.materialProductLine != null)
            return false;
        if (supplierId != null ? !supplierId.equals(that.supplierId) : that.supplierId != null) return false;
        if (materialId != null ? !materialId.equals(that.materialId) : that.materialId != null) return false;
        if (sourceUnit != null ? !sourceUnit.equals(that.sourceUnit) : that.sourceUnit != null) return false;
        if (sourceUnitAmount != null ? !sourceUnitAmount.equals(that.sourceUnitAmount) : that.sourceUnitAmount != null)
            return false;

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
        result = 31 * result + (transferOrderNo != null ? transferOrderNo.hashCode() : 0);
        result = 31 * result + (materialNo != null ? materialNo.hashCode() : 0);
        result = 31 * result + (materialName != null ? materialName.hashCode() : 0);
        result = 31 * result + (supplierNo != null ? supplierNo.hashCode() : 0);
        result = 31 * result + (supplierName != null ? supplierName.hashCode() : 0);
        result = 31 * result + (personName != null ? personName.hashCode() : 0);
        result = 31 * result + (targetStorageLocationNo != null ? targetStorageLocationNo.hashCode() : 0);
        result = 31 * result + (targetStorageLocationName != null ? targetStorageLocationName.hashCode() : 0);
        result = 31 * result + sourceStorageLocationId;
        result = 31 * result + (sourceStorageLocationNo != null ? sourceStorageLocationNo.hashCode() : 0);
        result = 31 * result + (sourceStorageLocationName != null ? sourceStorageLocationName.hashCode() : 0);
        result = 31 * result + (materialProductLine != null ? materialProductLine.hashCode() : 0);
        result = 31 * result + (supplierId != null ? supplierId.hashCode() : 0);
        result = 31 * result + (materialId != null ? materialId.hashCode() : 0);
        result = 31 * result + (sourceUnit != null ? sourceUnit.hashCode() : 0);
        result = 31 * result + (sourceUnitAmount != null ? sourceUnitAmount.hashCode() : 0);
        return result;
    }

    @Basic
    @Column(name = "TransferOrderCreateTime")
    public Timestamp getTransferOrderCreateTime() {
        return transferOrderCreateTime;
    }

    public void setTransferOrderCreateTime(Timestamp transferOrderCreateTime) {
        this.transferOrderCreateTime = transferOrderCreateTime;
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
