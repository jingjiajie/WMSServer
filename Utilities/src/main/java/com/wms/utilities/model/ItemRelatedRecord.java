package com.wms.utilities.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class ItemRelatedRecord {
    private int id;
    private String stockRecordBatchNo;
    private int relatedItemId;
    private int itemType;
    private BigDecimal batchAmount;
    private BigDecimal batchAvailableAmount;

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
    @Column(name = "StockRecordBatchNo")
    public String getStockRecordBatchNo() {
        return stockRecordBatchNo;
    }

    public void setStockRecordBatchNo(String stockRecordBatchNo) {
        this.stockRecordBatchNo = stockRecordBatchNo;
    }

    @Basic
    @Column(name = "RelatedItemID")
    public int getRelatedItemId() {
        return relatedItemId;
    }

    public void setRelatedItemId(int relatedItemId) {
        this.relatedItemId = relatedItemId;
    }

    @Basic
    @Column(name = "ItemType")
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Basic
    @Column(name = "BatchAmount")
    public BigDecimal getBatchAmount() {
        return batchAmount;
    }

    public void setBatchAmount(BigDecimal batchAmount) {
        this.batchAmount = batchAmount;
    }

    @Basic
    @Column(name = "BatchAvailableAmount")
    public BigDecimal getBatchAvailableAmount() {
        return batchAvailableAmount;
    }

    public void setBatchAvailableAmount(BigDecimal batchAvailableAmount) {
        this.batchAvailableAmount = batchAvailableAmount;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        ItemRelatedRecord that = (ItemRelatedRecord) object;

        if (id != that.id) return false;
        if (relatedItemId != that.relatedItemId) return false;
        if (itemType != that.itemType) return false;
        if (stockRecordBatchNo != null ? !stockRecordBatchNo.equals(that.stockRecordBatchNo) : that.stockRecordBatchNo != null)
            return false;
        if (batchAmount != null ? !batchAmount.equals(that.batchAmount) : that.batchAmount != null) return false;
        if (batchAvailableAmount != null ? !batchAvailableAmount.equals(that.batchAvailableAmount) : that.batchAvailableAmount != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (stockRecordBatchNo != null ? stockRecordBatchNo.hashCode() : 0);
        result = 31 * result + relatedItemId;
        result = 31 * result + itemType;
        result = 31 * result + (batchAmount != null ? batchAmount.hashCode() : 0);
        result = 31 * result + (batchAvailableAmount != null ? batchAvailableAmount.hashCode() : 0);
        return result;
    }
}
