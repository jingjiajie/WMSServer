package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class SummaryDetailsView {
    private int id;
    private int summaryNoteItemId;
    private int supplyId;
    private BigDecimal storageLocations;
    private BigDecimal trays;
    private BigDecimal area;
    private BigDecimal deliveryAmount;
    private Integer materialId;
    private String materialNo;
    private String materialName;
    private String materialProductLine;
    private Integer supplierId;
    private String supplierNo;
    private String supplierName;

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
    @Column(name = "SummaryNoteItemID")
    public int getSummaryNoteItemId() {
        return summaryNoteItemId;
    }

    public void setSummaryNoteItemId(int summaryNoteItemId) {
        this.summaryNoteItemId = summaryNoteItemId;
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
    @Column(name = "StorageLocations")
    public BigDecimal getStorageLocations() {
        return storageLocations;
    }

    public void setStorageLocations(BigDecimal storageLocations) {
        this.storageLocations = storageLocations;
    }

    @Basic
    @Column(name = "Trays")
    public BigDecimal getTrays() {
        return trays;
    }

    public void setTrays(BigDecimal trays) {
        this.trays = trays;
    }

    @Basic
    @Column(name = "Area")
    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    @Basic
    @Column(name = "DeliveryAmount")
    public BigDecimal getDeliveryAmount() {
        return deliveryAmount;
    }

    public void setDeliveryAmount(BigDecimal deliveryAmount) {
        this.deliveryAmount = deliveryAmount;
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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        SummaryDetailsView that = (SummaryDetailsView) object;

        if (id != that.id) return false;
        if (summaryNoteItemId != that.summaryNoteItemId) return false;
        if (supplyId != that.supplyId) return false;
        if (storageLocations != null ? !storageLocations.equals(that.storageLocations) : that.storageLocations != null)
            return false;
        if (trays != null ? !trays.equals(that.trays) : that.trays != null) return false;
        if (area != null ? !area.equals(that.area) : that.area != null) return false;
        if (deliveryAmount != null ? !deliveryAmount.equals(that.deliveryAmount) : that.deliveryAmount != null)
            return false;
        if (materialId != null ? !materialId.equals(that.materialId) : that.materialId != null) return false;
        if (materialNo != null ? !materialNo.equals(that.materialNo) : that.materialNo != null) return false;
        if (materialName != null ? !materialName.equals(that.materialName) : that.materialName != null) return false;
        if (materialProductLine != null ? !materialProductLine.equals(that.materialProductLine) : that.materialProductLine != null)
            return false;
        if (supplierId != null ? !supplierId.equals(that.supplierId) : that.supplierId != null) return false;
        if (supplierNo != null ? !supplierNo.equals(that.supplierNo) : that.supplierNo != null) return false;
        if (supplierName != null ? !supplierName.equals(that.supplierName) : that.supplierName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + summaryNoteItemId;
        result = 31 * result + supplyId;
        result = 31 * result + (storageLocations != null ? storageLocations.hashCode() : 0);
        result = 31 * result + (trays != null ? trays.hashCode() : 0);
        result = 31 * result + (area != null ? area.hashCode() : 0);
        result = 31 * result + (deliveryAmount != null ? deliveryAmount.hashCode() : 0);
        result = 31 * result + (materialId != null ? materialId.hashCode() : 0);
        result = 31 * result + (materialNo != null ? materialNo.hashCode() : 0);
        result = 31 * result + (materialName != null ? materialName.hashCode() : 0);
        result = 31 * result + (materialProductLine != null ? materialProductLine.hashCode() : 0);
        result = 31 * result + (supplierId != null ? supplierId.hashCode() : 0);
        result = 31 * result + (supplierNo != null ? supplierNo.hashCode() : 0);
        result = 31 * result + (supplierName != null ? supplierName.hashCode() : 0);
        return result;
    }
}
