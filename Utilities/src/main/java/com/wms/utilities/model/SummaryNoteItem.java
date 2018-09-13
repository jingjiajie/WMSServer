package com.wms.utilities.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class SummaryNoteItem {
    private int id;
    private int summaryNoteId;
    private int supplierId;
    private BigDecimal totalArea;
    private BigDecimal days;
    private BigDecimal totalDeliveryAmount;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(name = "TotalArea")
    public BigDecimal getTotalArea() {
        return totalArea;
    }

    public void setTotalArea(BigDecimal totalArea) {
        this.totalArea = totalArea;
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

        SummaryNoteItem that = (SummaryNoteItem) o;

        if (id != that.id) return false;
        if (summaryNoteId != that.summaryNoteId) return false;
        if (supplierId != that.supplierId) return false;
        if (totalArea != null ? !totalArea.equals(that.totalArea) : that.totalArea != null) return false;
        if (days != null ? !days.equals(that.days) : that.days != null) return false;
        if (totalDeliveryAmount != null ? !totalDeliveryAmount.equals(that.totalDeliveryAmount) : that.totalDeliveryAmount != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + summaryNoteId;
        result = 31 * result + supplierId;
        result = 31 * result + (totalArea != null ? totalArea.hashCode() : 0);
        result = 31 * result + (days != null ? days.hashCode() : 0);
        result = 31 * result + (totalDeliveryAmount != null ? totalDeliveryAmount.hashCode() : 0);
        return result;
    }
}
