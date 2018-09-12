package com.wms.utilities.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class SummaryDetails {
    private int id;
    private int summaryNoteItemId;
    private int supplyId;
    private BigDecimal storageLocations;
    private BigDecimal trays;
    private BigDecimal area;
    private BigDecimal deliveryAmount;

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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        SummaryDetails that = (SummaryDetails) object;

        if (id != that.id) return false;
        if (summaryNoteItemId != that.summaryNoteItemId) return false;
        if (supplyId != that.supplyId) return false;
        if (storageLocations != null ? !storageLocations.equals(that.storageLocations) : that.storageLocations != null)
            return false;
        if (trays != null ? !trays.equals(that.trays) : that.trays != null) return false;
        if (area != null ? !area.equals(that.area) : that.area != null) return false;
        if (deliveryAmount != null ? !deliveryAmount.equals(that.deliveryAmount) : that.deliveryAmount != null)
            return false;

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
        return result;
    }
}
