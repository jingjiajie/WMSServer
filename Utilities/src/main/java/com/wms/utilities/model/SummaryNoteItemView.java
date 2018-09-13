package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class SummaryNoteItemView {
    private int id;
    private int summaryNoteId;
    private int supplierId;
    private BigDecimal days;
    private String supplierName;
    private String supplierNo;
    private BigDecimal totalArea;
    private BigDecimal totalDeliveryAmount;

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
    @Column(name = "SummaryNoteID")
    public int getSummaryNoteId() {
        return summaryNoteId;
    }

    public void setSummaryNoteId(int summaryNoteId) {
        this.summaryNoteId = summaryNoteId;
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
    @Column(name = "Days")
    public BigDecimal getDays() {
        return days;
    }

    public void setDays(BigDecimal days) {
        this.days = days;
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
    @Column(name = "TotalArea")
    public BigDecimal getTotalArea() {
        return totalArea;
    }

    public void setTotalArea(BigDecimal totalArea) {
        this.totalArea = totalArea;
    }

    @Basic
    @Column(name = "TotalDeliveryAmount")
    public BigDecimal getTotalDeliveryAmount() {
        return totalDeliveryAmount;
    }

    public void setTotalDeliveryAmount(BigDecimal totalDeliveryAmount) {
        this.totalDeliveryAmount = totalDeliveryAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SummaryNoteItemView that = (SummaryNoteItemView) o;

        if (id != that.id) return false;
        if (summaryNoteId != that.summaryNoteId) return false;
        if (supplierId != that.supplierId) return false;
        if (days != null ? !days.equals(that.days) : that.days != null) return false;
        if (supplierName != null ? !supplierName.equals(that.supplierName) : that.supplierName != null) return false;
        if (supplierNo != null ? !supplierNo.equals(that.supplierNo) : that.supplierNo != null) return false;
        if (totalArea != null ? !totalArea.equals(that.totalArea) : that.totalArea != null) return false;
        if (totalDeliveryAmount != null ? !totalDeliveryAmount.equals(that.totalDeliveryAmount) : that.totalDeliveryAmount != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + summaryNoteId;
        result = 31 * result + supplierId;
        result = 31 * result + (days != null ? days.hashCode() : 0);
        result = 31 * result + (supplierName != null ? supplierName.hashCode() : 0);
        result = 31 * result + (supplierNo != null ? supplierNo.hashCode() : 0);
        result = 31 * result + (totalArea != null ? totalArea.hashCode() : 0);
        result = 31 * result + (totalDeliveryAmount != null ? totalDeliveryAmount.hashCode() : 0);
        return result;
    }
}
