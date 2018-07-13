package com.wms.services.salary.datestructures;

import antlr.collections.List;

import java.math.BigDecimal;

public class CalculateTax {
    public java.util.List<Integer> getPayNoteItemId() {
        return payNoteItemId;
    }

    public int getPayNoteId() {
        return payNoteId;
    }

    public void setPayNoteItemId(java.util.List<Integer> payNoteItemId) {
        this.payNoteItemId = payNoteItemId;
    }

    public void setPayNoteId(int payNoteId) {
        this.payNoteId = payNoteId;
    }

    private java.util.List<Integer> payNoteItemId;

    private int payNoteId;

    public int getTaxId() {
        return taxId;
    }

    public void setTaxId(int taxId) {
        this.taxId = taxId;
    }

    private int taxId;

}
