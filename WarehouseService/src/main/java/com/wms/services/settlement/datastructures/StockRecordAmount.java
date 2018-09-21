package com.wms.services.settlement.datastructures;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class StockRecordAmount {

    private int storageLocationId;
    private int supplyId;
    private BigDecimal amount;
    private int trays;
    private int isStorageLocation;
    private BigDecimal area;



    @Basic
    @Column(name = "StorageLocationID")
    public int getStorageLocationId() {
        return storageLocationId;
    }

    public void setStorageLocationId(int storageLocationId) {
        this.storageLocationId = storageLocationId;
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
    @Column(name = "Amount")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Basic
    @Column(name = "trays")
    public int getTrays() {
        return trays;
    }

    public void setTrays(int trays) {
        this.trays = trays;
    }

    @Basic
    @Column(name = "isStorageLocation")
    public int getIsStorageLocation() {
        return isStorageLocation;
    }

    public void setIsStorageLocations(int isStorageLocation) {
        this.isStorageLocation = isStorageLocation;
    }

    @Basic
    @Column(name = "area")
    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StockRecordAmount that = (StockRecordAmount) o;

        if (storageLocationId != that.storageLocationId) return false;
        if (supplyId != that.supplyId) return false;
        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
        if (trays != that.trays) return false;
        if (supplyId != that.supplyId) return false;
        if (isStorageLocation != that.isStorageLocation) return false;
        if (area != null ? !area.equals(that.area) : that.area != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = trays;
        result = 31 * result + storageLocationId;
        result = 31 * result + supplyId;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + isStorageLocation;
        result = 31 * result + (area != null ? area.hashCode() : 0);
        return result;
    }
}
