package com.wms.services.warehouse.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class TaxItem {
    private int id;
    private int taxId;
    private BigDecimal startAmount;
    private BigDecimal endAmount;
    private int type;
    private BigDecimal taxAmount;
    private BigDecimal taxRate;

    @Id
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "TaxID", nullable = false)
    public int getTaxId() {
        return taxId;
    }

    public void setTaxId(int taxId) {
        this.taxId = taxId;
    }

    @Basic
    @Column(name = "StartAmount", nullable = true, precision = 3)
    public BigDecimal getStartAmount() {
        return startAmount;
    }

    public void setStartAmount(BigDecimal startAmount) {
        this.startAmount = startAmount;
    }

    @Basic
    @Column(name = "EndAmount", nullable = true, precision = 3)
    public BigDecimal getEndAmount() {
        return endAmount;
    }

    public void setEndAmount(BigDecimal endAmount) {
        this.endAmount = endAmount;
    }

    @Basic
    @Column(name = "Type", nullable = false)
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Basic
    @Column(name = "TaxAmount", nullable = true, precision = 3)
    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    @Basic
    @Column(name = "TaxRate", nullable = true, precision = 3)
    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaxItem taxItem = (TaxItem) o;
        return id == taxItem.id &&
                taxId == taxItem.taxId &&
                type == taxItem.type &&
                Objects.equals(startAmount, taxItem.startAmount) &&
                Objects.equals(endAmount, taxItem.endAmount) &&
                Objects.equals(taxAmount, taxItem.taxAmount) &&
                Objects.equals(taxRate, taxItem.taxRate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, taxId, startAmount, endAmount, type, taxAmount, taxRate);
    }
}
