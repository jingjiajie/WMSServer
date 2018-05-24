package com.wms.utilities.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class StockTakingOrderItem {
    private int id;
    private int stockTakingOrderId;
    private Integer storageLocationId;
    private int supplyId;
    private BigDecimal amount;
    private BigDecimal realAmount;
    private String unit;
    private BigDecimal unitAmount;
    private Integer personId;
    private String comment;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "StockTakingOrderID")
    public int getStockTakingOrderId() {
        return stockTakingOrderId;
    }

    public void setStockTakingOrderId(int stockTakingOrderId) {
        this.stockTakingOrderId = stockTakingOrderId;
    }

    @Basic
    @Column(name = "StorageLocationID")
    public Integer getStorageLocationId() {
        return storageLocationId;
    }

    public void setStorageLocationId(Integer storageLocationId) {
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
    @Column(name = "RealAmount")
    public BigDecimal getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
    }

    @Basic
    @Column(name = "Unit")
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Basic
    @Column(name = "UnitAmount")
    public BigDecimal getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(BigDecimal unitAmount) {
        this.unitAmount = unitAmount;
    }

    @Basic
    @Column(name = "PersonID")
    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    @Basic
    @Column(name = "Comment")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        StockTakingOrderItem that = (StockTakingOrderItem) object;

        if (id != that.id) return false;
        if (stockTakingOrderId != that.stockTakingOrderId) return false;
        if (supplyId != that.supplyId) return false;
        if (storageLocationId != null ? !storageLocationId.equals(that.storageLocationId) : that.storageLocationId != null)
            return false;
        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
        if (realAmount != null ? !realAmount.equals(that.realAmount) : that.realAmount != null) return false;
        if (unit != null ? !unit.equals(that.unit) : that.unit != null) return false;
        if (unitAmount != null ? !unitAmount.equals(that.unitAmount) : that.unitAmount != null) return false;
        if (personId != null ? !personId.equals(that.personId) : that.personId != null) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + stockTakingOrderId;
        result = 31 * result + (storageLocationId != null ? storageLocationId.hashCode() : 0);
        result = 31 * result + supplyId;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (realAmount != null ? realAmount.hashCode() : 0);
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + (unitAmount != null ? unitAmount.hashCode() : 0);
        result = 31 * result + (personId != null ? personId.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }
}
