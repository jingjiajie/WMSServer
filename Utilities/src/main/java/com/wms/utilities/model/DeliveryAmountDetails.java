package com.wms.utilities.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class DeliveryAmountDetails {
    private int id;
    private int summaryNoteItemId;
    private int supplyId;
    private BigDecimal deliveryAmount;
    private int destinationId;

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
    @Column(name = "DeliveryAmount")
    public BigDecimal getDeliveryAmount() {
        return deliveryAmount;
    }

    public void setDeliveryAmount(BigDecimal deliveryAmount) {
        this.deliveryAmount = deliveryAmount;
    }

    @Basic
    @Column(name = "DestinationID")
    public int getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(int destinationId) {
        this.destinationId = destinationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeliveryAmountDetails that = (DeliveryAmountDetails) o;

        if (id != that.id) return false;
        if (summaryNoteItemId != that.summaryNoteItemId) return false;
        if (supplyId != that.supplyId) return false;
        if (destinationId != that.destinationId) return false;
        if (deliveryAmount != null ? !deliveryAmount.equals(that.deliveryAmount) : that.deliveryAmount != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + summaryNoteItemId;
        result = 31 * result + supplyId;
        result = 31 * result + (deliveryAmount != null ? deliveryAmount.hashCode() : 0);
        result = 31 * result + destinationId;
        return result;
    }
}
