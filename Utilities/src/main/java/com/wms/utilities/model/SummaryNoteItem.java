package com.wms.utilities.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class SummaryNoteItem {
    private int id;
    private int summaryNoteId;
    private int supplierId;
    private BigDecimal area;
    private BigDecimal days;
    private BigDecimal deliveryTimes;
    private int state;

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
    @Column(name = "State")
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SummaryNoteItem that = (SummaryNoteItem) o;

        if (id != that.id) return false;
        if (summaryNoteId != that.summaryNoteId) return false;
        if (supplierId != that.supplierId) return false;
        if (state != that.state) return false;
        if (area != null ? !area.equals(that.area) : that.area != null) return false;
        if (days != null ? !days.equals(that.days) : that.days != null) return false;
        if (deliveryTimes != null ? !deliveryTimes.equals(that.deliveryTimes) : that.deliveryTimes != null)
            return false;

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
        result = 31 * result + state;
        return result;
    }
}
