package com.wms.services.salary.datestructures;

import java.math.BigDecimal;

public class CalculateTax {
    public int[] getPayNoteItemId() {
        return payNoteItemId;
    }

    public int getPayNoteId() {
        return payNoteId;
    }


    public void setPayNoteItemId(int[] payNoteItemId) {
        this.payNoteItemId = payNoteItemId;
    }

    public void setPayNoteId(int payNoteId) {
        this.payNoteId = payNoteId;
    }

    private int[] payNoteItemId;

    private int payNoteId;

    public int getTaxId() {
        return taxId;
    }

    public void setTaxId(int taxId) {
        this.taxId = taxId;
    }

    int taxId;
}
