package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TransferRecord {
    private int id;
    private int warehouseId;
    private Integer sourceStockRecordId;
    private Integer newStockRecordId;
    private Integer supplyId;

    @Id
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "WarehouseID")
    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    @Basic
    @Column(name = "SourceStockRecordID")
    public Integer getSourceStockRecordId() {
        return sourceStockRecordId;
    }

    public void setSourceStockRecordId(Integer sourceStockRecordId) {
        this.sourceStockRecordId = sourceStockRecordId;
    }

    @Basic
    @Column(name = "NewStockRecordID")
    public Integer getNewStockRecordId() {
        return newStockRecordId;
    }

    public void setNewStockRecordId(Integer newStockRecordId) {
        this.newStockRecordId = newStockRecordId;
    }

    @Basic
    @Column(name = "SupplyID")
    public Integer getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(Integer supplyId) {
        this.supplyId = supplyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransferRecord that = (TransferRecord) o;

        if (id != that.id) return false;
        if (warehouseId != that.warehouseId) return false;
        if (sourceStockRecordId != null ? !sourceStockRecordId.equals(that.sourceStockRecordId) : that.sourceStockRecordId != null)
            return false;
        if (newStockRecordId != null ? !newStockRecordId.equals(that.newStockRecordId) : that.newStockRecordId != null)
            return false;
        if (supplyId != null ? !supplyId.equals(that.supplyId) : that.supplyId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + warehouseId;
        result = 31 * result + (sourceStockRecordId != null ? sourceStockRecordId.hashCode() : 0);
        result = 31 * result + (newStockRecordId != null ? newStockRecordId.hashCode() : 0);
        result = 31 * result + (supplyId != null ? supplyId.hashCode() : 0);
        return result;
    }
}
