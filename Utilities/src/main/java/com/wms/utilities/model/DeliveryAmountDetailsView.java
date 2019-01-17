package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class DeliveryAmountDetailsView {
    private int id;
    private int summaryDetailsId;
    private int supplyId;
    private BigDecimal deliveryAmount;
    private String supplierName;
    private String supplierNo;
    private int supplierId;
    private int materialId;
    private String materialNo;
    private String materialProductLine;
    private String materialName;
    private int destinationId;
    private String destinationName;

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
    @Column(name = "SummaryDetailsID")
    public int getSummaryDetailsId() {
        return summaryDetailsId;
    }

    public void setSummaryDetailsId(int summaryDetailsId) {
        this.summaryDetailsId = summaryDetailsId;
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
    @Column(name = "DeliveryAmount")
    public BigDecimal getDeliveryAmount() {
        return deliveryAmount;
    }

    public void setDeliveryAmount(BigDecimal deliveryAmount) {
        this.deliveryAmount = deliveryAmount;
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
    @Column(name = "SupplierNo")
    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    @Basic
    @Column(name = "SupplierID")
    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    @Basic
    @Column(name = "MaterialID")
    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
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
    @Column(name = "MaterialProductLine")
    public String getMaterialProductLine() {
        return materialProductLine;
    }

    public void setMaterialProductLine(String materialProductLine) {
        this.materialProductLine = materialProductLine;
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
    @Column(name = "DestinationID")
    public int getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(int destinationId) {
        this.destinationId = destinationId;
    }

    @Basic
    @Column(name = "DestinationName")
    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        DeliveryAmountDetailsView that = (DeliveryAmountDetailsView) object;

        if (id != that.id) return false;
        if (summaryDetailsId != that.summaryDetailsId) return false;
        if (supplyId != that.supplyId) return false;
        if (supplierId != that.supplierId) return false;
        if (materialId != that.materialId) return false;
        if (destinationId != that.destinationId) return false;
        if (deliveryAmount != null ? !deliveryAmount.equals(that.deliveryAmount) : that.deliveryAmount != null)
            return false;
        if (supplierName != null ? !supplierName.equals(that.supplierName) : that.supplierName != null) return false;
        if (supplierNo != null ? !supplierNo.equals(that.supplierNo) : that.supplierNo != null) return false;
        if (materialNo != null ? !materialNo.equals(that.materialNo) : that.materialNo != null) return false;
        if (materialProductLine != null ? !materialProductLine.equals(that.materialProductLine) : that.materialProductLine != null)
            return false;
        if (materialName != null ? !materialName.equals(that.materialName) : that.materialName != null) return false;
        if (destinationName != null ? !destinationName.equals(that.destinationName) : that.destinationName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + summaryDetailsId;
        result = 31 * result + supplyId;
        result = 31 * result + (deliveryAmount != null ? deliveryAmount.hashCode() : 0);
        result = 31 * result + (supplierName != null ? supplierName.hashCode() : 0);
        result = 31 * result + (supplierNo != null ? supplierNo.hashCode() : 0);
        result = 31 * result + supplierId;
        result = 31 * result + materialId;
        result = 31 * result + (materialNo != null ? materialNo.hashCode() : 0);
        result = 31 * result + (materialProductLine != null ? materialProductLine.hashCode() : 0);
        result = 31 * result + (materialName != null ? materialName.hashCode() : 0);
        result = 31 * result + destinationId;
        result = 31 * result + (destinationName != null ? destinationName.hashCode() : 0);
        return result;
    }
}
