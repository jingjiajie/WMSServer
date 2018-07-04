package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class PayNoteTaxView {
    private int id;
    private int payNoteId;
    private int taxId;
    private String payNoteNo;
    private String taxName;
    private String taxNo;

    @Basic
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "PayNoteID")
    public int getPayNoteId() {
        return payNoteId;
    }

    public void setPayNoteId(int payNoteId) {
        this.payNoteId = payNoteId;
    }

    @Basic
    @Column(name = "TaxID")
    public int getTaxId() {
        return taxId;
    }

    public void setTaxId(int taxId) {
        this.taxId = taxId;
    }

    @Basic
    @Column(name = "PayNoteNo")
    public String getPayNoteNo() {
        return payNoteNo;
    }

    public void setPayNoteNo(String payNoteNo) {
        this.payNoteNo = payNoteNo;
    }

    @Basic
    @Column(name = "TaxName")
    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    @Basic
    @Column(name = "TaxNo")
    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PayNoteTaxView that = (PayNoteTaxView) o;

        if (id != that.id) return false;
        if (payNoteId != that.payNoteId) return false;
        if (taxId != that.taxId) return false;
        if (payNoteNo != null ? !payNoteNo.equals(that.payNoteNo) : that.payNoteNo != null) return false;
        if (taxName != null ? !taxName.equals(that.taxName) : that.taxName != null) return false;
        if (taxNo != null ? !taxNo.equals(that.taxNo) : that.taxNo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + payNoteId;
        result = 31 * result + taxId;
        result = 31 * result + (payNoteNo != null ? payNoteNo.hashCode() : 0);
        result = 31 * result + (taxName != null ? taxName.hashCode() : 0);
        result = 31 * result + (taxNo != null ? taxNo.hashCode() : 0);
        return result;
    }
}
