package com.wms.services.salary.datestructures;

import com.wms.utilities.model.PayNoteItemView;
import com.wms.utilities.model.PayNoteView;

import java.util.List;

public class PayNoteAndItems {
    public PayNoteView getPayNoteView() {
        return payNoteView;
    }

    public List<PayNoteItemView> getPayNoteItemViews() {
        return payNoteItemViews;
    }

    private PayNoteView payNoteView;

    public void setPayNoteView(PayNoteView payNoteView) {
        this.payNoteView = payNoteView;
    }

    public void setPayNoteItemViews(List<PayNoteItemView> payNoteItemViews) {
        this.payNoteItemViews = payNoteItemViews;
    }

    private List<PayNoteItemView> payNoteItemViews;
}
