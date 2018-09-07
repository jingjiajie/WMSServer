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
    private BigDecimal area;
    private BigDecimal days;
    private BigDecimal deliveryTimes;
    private String supplierName;
    private String supplierNo;

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
    @Column(name = "Area")
    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
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
    @Column(name = "DeliveryTimes")
    public BigDecimal getDeliveryTimes() {
        return deliveryTimes;
    }

    public void setDeliveryTimes(BigDecimal deliveryTimes) {
        this.deliveryTimes = deliveryTimes;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SummaryNoteItemView that = (SummaryNoteItemView) o;

        if (id != that.id) return false;
        if (summaryNoteId != that.summaryNoteId) return false;
        if (supplierId != that.supplierId) return false;
        if (area != null ? !area.equals(that.area) : that.area != null) return false;
        if (days != null ? !days.equals(that.days) : that.days != null) return false;
        if (deliveryTimes != null ? !deliveryTimes.equals(that.deliveryTimes) : that.deliveryTimes != null)
            return false;
        if (supplierName != null ? !supplierName.equals(that.supplierName) : that.supplierName != null) return false;
        if (supplierNo != null ? !supplierNo.equals(that.supplierNo) : that.supplierNo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + summaryNoteId;
        result = 31 * result + supplierId;
        result = 31 * result + (area != null ? area.hashCode() : 0);
        result = 31 * result + (days != null ? days.hashCode() : 0);
        result = 31 * result + (deliveryTimes != null ? deliveryTimes.hashCode() : 0);
        result = 31 * result + (supplierName != null ? supplierName.hashCode() : 0);
        result = 31 * result + (supplierNo != null ? supplierNo.hashCode() : 0);
        return result;
    }
}
