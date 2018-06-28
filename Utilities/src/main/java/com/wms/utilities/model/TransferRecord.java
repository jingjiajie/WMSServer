package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
public class TransferRecord {
    private int id;
    private int warehouseId;
    private Integer sourceStockRecordId;
    private Integer newStockRecordId;
    private Integer supplyId;
    private Timestamp time;
    private Integer sourceStorageLocationId;
    private BigDecimal sourceStorageLocationOriginalAmount;
    private BigDecimal sourceStorageLocationNewAmount;
    private String sourceStorageLocationUnit;
    private BigDecimal sourceStorageLocationUnitAmount;
    private Integer targetStorageLocationId;
    private BigDecimal targetStorageLocationOriginalAmount;
    private BigDecimal targetStorageLocationNewAmount;
    private String targetStorageLocationUnit;
    private BigDecimal targetStorageLocationAmount;
    private BigDecimal transferAmount;
    private String transferUnit;
    private BigDecimal transferUnitAmount;

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

    @Basic
    @Column(name = "Time")
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Basic
    @Column(name = "SourceStorageLocationID")
    public Integer getSourceStorageLocationId() {
        return sourceStorageLocationId;
    }

    public void setSourceStorageLocationId(Integer sourceStorageLocationId) {
        this.sourceStorageLocationId = sourceStorageLocationId;
    }

    @Basic
    @Column(name = "SourceStorageLocationOriginalAmount")
    public BigDecimal getSourceStorageLocationOriginalAmount() {
        return sourceStorageLocationOriginalAmount;
    }

    public void setSourceStorageLocationOriginalAmount(BigDecimal sourceStorageLocationOriginalAmount) {
        this.sourceStorageLocationOriginalAmount = sourceStorageLocationOriginalAmount;
    }

    @Basic
    @Column(name = "SourceStorageLocationNewAmount")
    public BigDecimal getSourceStorageLocationNewAmount() {
        return sourceStorageLocationNewAmount;
    }

    public void setSourceStorageLocationNewAmount(BigDecimal sourceStorageLocationNewAmount) {
        this.sourceStorageLocationNewAmount = sourceStorageLocationNewAmount;
    }

    @Basic
    @Column(name = "SourceStorageLocationUnit")
    public String getSourceStorageLocationUnit() {
        return sourceStorageLocationUnit;
    }

    public void setSourceStorageLocationUnit(String sourceStorageLocationUnit) {
        this.sourceStorageLocationUnit = sourceStorageLocationUnit;
    }

    @Basic
    @Column(name = "SourceStorageLocationUnitAmount")
    public BigDecimal getSourceStorageLocationUnitAmount() {
        return sourceStorageLocationUnitAmount;
    }

    public void setSourceStorageLocationUnitAmount(BigDecimal sourceStorageLocationUnitAmount) {
        this.sourceStorageLocationUnitAmount = sourceStorageLocationUnitAmount;
    }

    @Basic
    @Column(name = "TargetStorageLocationID")
    public Integer getTargetStorageLocationId() {
        return targetStorageLocationId;
    }

    public void setTargetStorageLocationId(Integer targetStorageLocationId) {
        this.targetStorageLocationId = targetStorageLocationId;
    }

    @Basic
    @Column(name = "TargetStorageLocationOriginalAmount")
    public BigDecimal getTargetStorageLocationOriginalAmount() {
        return targetStorageLocationOriginalAmount;
    }

    public void setTargetStorageLocationOriginalAmount(BigDecimal targetStorageLocationOriginalAmount) {
        this.targetStorageLocationOriginalAmount = targetStorageLocationOriginalAmount;
    }

    @Basic
    @Column(name = "TargetStorageLocationNewAmount")
    public BigDecimal getTargetStorageLocationNewAmount() {
        return targetStorageLocationNewAmount;
    }

    public void setTargetStorageLocationNewAmount(BigDecimal targetStorageLocationNewAmount) {
        this.targetStorageLocationNewAmount = targetStorageLocationNewAmount;
    }

    @Basic
    @Column(name = "TargetStorageLocationUnit")
    public String getTargetStorageLocationUnit() {
        return targetStorageLocationUnit;
    }

    public void setTargetStorageLocationUnit(String targetStorageLocationUnit) {
        this.targetStorageLocationUnit = targetStorageLocationUnit;
    }

    @Basic
    @Column(name = "TargetStorageLocationAmount")
    public BigDecimal getTargetStorageLocationAmount() {
        return targetStorageLocationAmount;
    }

    public void setTargetStorageLocationAmount(BigDecimal targetStorageLocationAmount) {
        this.targetStorageLocationAmount = targetStorageLocationAmount;
    }

    @Basic
    @Column(name = "TransferAmount")
    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    @Basic
    @Column(name = "TransferUnit")
    public String getTransferUnit() {
        return transferUnit;
    }

    public void setTransferUnit(String transferUnit) {
        this.transferUnit = transferUnit;
    }

    @Basic
    @Column(name = "TransferUnitAmount")
    public BigDecimal getTransferUnitAmount() {
        return transferUnitAmount;
    }

    public void setTransferUnitAmount(BigDecimal transferUnitAmount) {
        this.transferUnitAmount = transferUnitAmount;
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
        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (sourceStorageLocationId != null ? !sourceStorageLocationId.equals(that.sourceStorageLocationId) : that.sourceStorageLocationId != null)
            return false;
        if (sourceStorageLocationOriginalAmount != null ? !sourceStorageLocationOriginalAmount.equals(that.sourceStorageLocationOriginalAmount) : that.sourceStorageLocationOriginalAmount != null)
            return false;
        if (sourceStorageLocationNewAmount != null ? !sourceStorageLocationNewAmount.equals(that.sourceStorageLocationNewAmount) : that.sourceStorageLocationNewAmount != null)
            return false;
        if (sourceStorageLocationUnit != null ? !sourceStorageLocationUnit.equals(that.sourceStorageLocationUnit) : that.sourceStorageLocationUnit != null)
            return false;
        if (sourceStorageLocationUnitAmount != null ? !sourceStorageLocationUnitAmount.equals(that.sourceStorageLocationUnitAmount) : that.sourceStorageLocationUnitAmount != null)
            return false;
        if (targetStorageLocationId != null ? !targetStorageLocationId.equals(that.targetStorageLocationId) : that.targetStorageLocationId != null)
            return false;
        if (targetStorageLocationOriginalAmount != null ? !targetStorageLocationOriginalAmount.equals(that.targetStorageLocationOriginalAmount) : that.targetStorageLocationOriginalAmount != null)
            return false;
        if (targetStorageLocationNewAmount != null ? !targetStorageLocationNewAmount.equals(that.targetStorageLocationNewAmount) : that.targetStorageLocationNewAmount != null)
            return false;
        if (targetStorageLocationUnit != null ? !targetStorageLocationUnit.equals(that.targetStorageLocationUnit) : that.targetStorageLocationUnit != null)
            return false;
        if (targetStorageLocationAmount != null ? !targetStorageLocationAmount.equals(that.targetStorageLocationAmount) : that.targetStorageLocationAmount != null)
            return false;
        if (transferAmount != null ? !transferAmount.equals(that.transferAmount) : that.transferAmount != null)
            return false;
        if (transferUnit != null ? !transferUnit.equals(that.transferUnit) : that.transferUnit != null) return false;
        if (transferUnitAmount != null ? !transferUnitAmount.equals(that.transferUnitAmount) : that.transferUnitAmount != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + warehouseId;
        result = 31 * result + (sourceStockRecordId != null ? sourceStockRecordId.hashCode() : 0);
        result = 31 * result + (newStockRecordId != null ? newStockRecordId.hashCode() : 0);
        result = 31 * result + (supplyId != null ? supplyId.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (sourceStorageLocationId != null ? sourceStorageLocationId.hashCode() : 0);
        result = 31 * result + (sourceStorageLocationOriginalAmount != null ? sourceStorageLocationOriginalAmount.hashCode() : 0);
        result = 31 * result + (sourceStorageLocationNewAmount != null ? sourceStorageLocationNewAmount.hashCode() : 0);
        result = 31 * result + (sourceStorageLocationUnit != null ? sourceStorageLocationUnit.hashCode() : 0);
        result = 31 * result + (sourceStorageLocationUnitAmount != null ? sourceStorageLocationUnitAmount.hashCode() : 0);
        result = 31 * result + (targetStorageLocationId != null ? targetStorageLocationId.hashCode() : 0);
        result = 31 * result + (targetStorageLocationOriginalAmount != null ? targetStorageLocationOriginalAmount.hashCode() : 0);
        result = 31 * result + (targetStorageLocationNewAmount != null ? targetStorageLocationNewAmount.hashCode() : 0);
        result = 31 * result + (targetStorageLocationUnit != null ? targetStorageLocationUnit.hashCode() : 0);
        result = 31 * result + (targetStorageLocationAmount != null ? targetStorageLocationAmount.hashCode() : 0);
        result = 31 * result + (transferAmount != null ? transferAmount.hashCode() : 0);
        result = 31 * result + (transferUnit != null ? transferUnit.hashCode() : 0);
        result = 31 * result + (transferUnitAmount != null ? transferUnitAmount.hashCode() : 0);
        return result;
    }
}
