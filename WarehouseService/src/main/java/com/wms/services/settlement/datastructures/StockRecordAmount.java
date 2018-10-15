package com.wms.services.settlement.datastructures;

import com.wms.utilities.model.StockRecord;
import org.apache.tomcat.jni.BIOCallback;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


public class StockRecordAmount {
    private BigDecimal storageLocations;
    private int supplyId;
    private BigDecimal area;
    private BigDecimal trays;
    private int suplierId;

    public BigDecimal getStorageLocations() {
        return storageLocations;
    }

    public void setStorageLocations(BigDecimal storageLocations) {
        this.storageLocations = storageLocations;
    }

    public int getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(int supplyId) {
        this.supplyId = supplyId;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }


    public BigDecimal getTrays() {
        return trays;
    }

    public void setTrays(BigDecimal trays) {
        this.trays = trays;
    }


    public int getSuplierId() {
        return suplierId;
    }

    public void setSuplierId(int suplierId) {
        this.suplierId = suplierId;
    }


/*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StockRecordAmount that = (StockRecordAmount) o;

        if (storageLocationId != that.storageLocationId) return false;
        if (supplyId != that.supplyId) return false;
        if (trays != null ? !trays.equals(that.trays) : that.trays != null) return false;
        if (area != null ? !area.equals(that.area) : that.area != null) return false;
        if (suplierId != that.suplierId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = supplyId;
        result = 31 * result + storageLocationId;
        result = 31 * result + (trays != null ? trays.hashCode() : 0);
        result = 31 * result + (area != null ? area.hashCode() : 0);
        result = 31 * result + suplierId;
        return result;
    }
*/

}
