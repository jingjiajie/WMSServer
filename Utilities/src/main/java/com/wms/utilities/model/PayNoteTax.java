package com.wms.utilities.model;

import javax.persistence.*;

@Entity
public class PayNoteTax {
    private int id;
    private int payNoteId;
    private int taxId;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PayNoteTax that = (PayNoteTax) o;

        if (id != that.id) return false;
        if (payNoteId != that.payNoteId) return false;
        if (taxId != that.taxId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + payNoteId;
        result = 31 * result + taxId;
        return result;
    }
}
