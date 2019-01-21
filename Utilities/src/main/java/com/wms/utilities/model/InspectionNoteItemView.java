package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
public class InspectionNoteItemView {
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
    private String inspectionNoteNo;
    private String materialNo;
    private String materialName;
    private String supplierNo;
    private String supplierName;
    private String personName;
    private String materialProductLine;
    private Integer supplierId;
    private Integer materialId;
    private String warehouseEntryItemUnit;
    private BigDecimal warehouseEntryItemUnitAmount;
    private BigDecimal warehouseEntryItemRealAmount;
    private Timestamp inspectionNoteCreateTime;
    //private Integer version;

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

    @Basic
    @Column(name = "InspectionNoteNo")
    public String getInspectionNoteNo() {
        return inspectionNoteNo;
    }

    public void setInspectionNoteNo(String inspectionNoteNo) {
        this.inspectionNoteNo = inspectionNoteNo;
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
    @Column(name = "WarehouseEntryItemUnit")
    public String getWarehouseEntryItemUnit() {
        return warehouseEntryItemUnit;
    }

    public void setWarehouseEntryItemUnit(String warehouseEntryItemUnit) {
        this.warehouseEntryItemUnit = warehouseEntryItemUnit;
    }

    @Basic
    @Column(name = "WarehouseEntryItemUnitAmount")
    public BigDecimal getWarehouseEntryItemUnitAmount() {
        return warehouseEntryItemUnitAmount;
    }

    public void setWarehouseEntryItemUnitAmount(BigDecimal warehouseEntryItemUnitAmount) {
        this.warehouseEntryItemUnitAmount = warehouseEntryItemUnitAmount;
    }

    @Basic
    @Column(name = "WarehouseEntryItemRealAmount")
    public BigDecimal getWarehouseEntryItemRealAmount() {
        return warehouseEntryItemRealAmount;
    }

    public void setWarehouseEntryItemRealAmount(BigDecimal warehouseEntryItemRealAmount) {
        this.warehouseEntryItemRealAmount = warehouseEntryItemRealAmount;
    }

    @Basic
    @Column(name = "InspectionNoteCreateTime")
    public Timestamp getInspectionNoteCreateTime() {
        return inspectionNoteCreateTime;
    }

    public void setInspectionNoteCreateTime(Timestamp inspectionNoteCreateTime) {
        this.inspectionNoteCreateTime = inspectionNoteCreateTime;
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

        InspectionNoteItemView that = (InspectionNoteItemView) o;

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
        if (inspectionNoteNo != null ? !inspectionNoteNo.equals(that.inspectionNoteNo) : that.inspectionNoteNo != null)
            return false;
        if (materialNo != null ? !materialNo.equals(that.materialNo) : that.materialNo != null) return false;
        if (materialName != null ? !materialName.equals(that.materialName) : that.materialName != null) return false;
        if (supplierNo != null ? !supplierNo.equals(that.supplierNo) : that.supplierNo != null) return false;
        if (supplierName != null ? !supplierName.equals(that.supplierName) : that.supplierName != null) return false;
        if (personName != null ? !personName.equals(that.personName) : that.personName != null) return false;
        if (materialProductLine != null ? !materialProductLine.equals(that.materialProductLine) : that.materialProductLine != null)
            return false;
        if (supplierId != null ? !supplierId.equals(that.supplierId) : that.supplierId != null) return false;
        if (materialId != null ? !materialId.equals(that.materialId) : that.materialId != null) return false;
        if (warehouseEntryItemUnit != null ? !warehouseEntryItemUnit.equals(that.warehouseEntryItemUnit) : that.warehouseEntryItemUnit != null)
            return false;
        if (warehouseEntryItemUnitAmount != null ? !warehouseEntryItemUnitAmount.equals(that.warehouseEntryItemUnitAmount) : that.warehouseEntryItemUnitAmount != null)
            return false;
        if (warehouseEntryItemRealAmount != null ? !warehouseEntryItemRealAmount.equals(that.warehouseEntryItemRealAmount) : that.warehouseEntryItemRealAmount != null)
            return false;
        if (inspectionNoteCreateTime != null ? !inspectionNoteCreateTime.equals(that.inspectionNoteCreateTime) : that.inspectionNoteCreateTime != null)
            return false;
        //if (version != null ? !version.equals(that.version) : that.version != null) return false;

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
        result = 31 * result + (inspectionNoteNo != null ? inspectionNoteNo.hashCode() : 0);
        result = 31 * result + (materialNo != null ? materialNo.hashCode() : 0);
        result = 31 * result + (materialName != null ? materialName.hashCode() : 0);
        result = 31 * result + (supplierNo != null ? supplierNo.hashCode() : 0);
        result = 31 * result + (supplierName != null ? supplierName.hashCode() : 0);
        result = 31 * result + (personName != null ? personName.hashCode() : 0);
        result = 31 * result + (materialProductLine != null ? materialProductLine.hashCode() : 0);
        result = 31 * result + (supplierId != null ? supplierId.hashCode() : 0);
        result = 31 * result + (materialId != null ? materialId.hashCode() : 0);
        result = 31 * result + (warehouseEntryItemUnit != null ? warehouseEntryItemUnit.hashCode() : 0);
        result = 31 * result + (warehouseEntryItemUnitAmount != null ? warehouseEntryItemUnitAmount.hashCode() : 0);
        result = 31 * result + (warehouseEntryItemRealAmount != null ? warehouseEntryItemRealAmount.hashCode() : 0);
        result = 31 * result + (inspectionNoteCreateTime != null ? inspectionNoteCreateTime.hashCode() : 0);
        //result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }
}
