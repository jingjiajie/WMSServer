package com.wms.services.salary.datestructures;

import java.math.BigDecimal;

public class PayNoteItemPay {

    public int getPayNoteItemId() {
        return payNoteItemId;
    }

    public int getPayNoteId() {
        return payNoteId;
    }

    public BigDecimal getRealAmount() {
        return RealAmount;
    }

    public void setPayNoteItemId(int payNoteItemId) {
        this.payNoteItemId = payNoteItemId;
    }

    public void setPayNoteId(int payNoteId) {
        this.payNoteId = payNoteId;
    }

    public void setRealAmount(BigDecimal realAmount) {
        RealAmount = realAmount;
    }

    private int payNoteItemId;

    private int payNoteId=-1;

    private BigDecimal RealAmount;

}
