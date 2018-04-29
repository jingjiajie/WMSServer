package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class TransferRecord {
    private int id;
    private int warehouseId;
    private int sourceStockRecordId;
    private int newStockRecordId;

    @Id
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "WarehouseID", nullable = false)
    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    @Basic
    @Column(name = "SourceStockRecordID", nullable = false)
    public int getSourceStockRecordId() {
        return sourceStockRecordId;
    }

    public void setSourceStockRecordId(int sourceStockRecordId) {
        this.sourceStockRecordId = sourceStockRecordId;
    }

    @Basic
    @Column(name = "NewStockRecordID", nullable = false)
    public int getNewStockRecordId() {
        return newStockRecordId;
    }

    public void setNewStockRecordId(int newStockRecordId) {
        this.newStockRecordId = newStockRecordId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransferRecord that = (TransferRecord) o;
        return id == that.id &&
                warehouseId == that.warehouseId &&
                sourceStockRecordId == that.sourceStockRecordId &&
                newStockRecordId == that.newStockRecordId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, warehouseId, sourceStockRecordId, newStockRecordId);
    }
}
