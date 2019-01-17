package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class DeliveryAmountDetails {
    private int id;
    private int summaryDetailsId;
    private int supplyId;
    private BigDecimal deliveryAmount;
    private int destinationId;

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
    @Column(name = "DestinationID")
    public int getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(int destinationId) {
        this.destinationId = destinationId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        DeliveryAmountDetails that = (DeliveryAmountDetails) object;

        if (id != that.id) return false;
        if (summaryDetailsId != that.summaryDetailsId) return false;
        if (supplyId != that.supplyId) return false;
        if (destinationId != that.destinationId) return false;
        if (deliveryAmount != null ? !deliveryAmount.equals(that.deliveryAmount) : that.deliveryAmount != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + summaryDetailsId;
        result = 31 * result + supplyId;
        result = 31 * result + (deliveryAmount != null ? deliveryAmount.hashCode() : 0);
        result = 31 * result + destinationId;
        return result;
    }
}
