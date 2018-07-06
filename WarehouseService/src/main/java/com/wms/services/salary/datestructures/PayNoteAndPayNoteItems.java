package com.wms.services.salary.datestructures;

import com.wms.utilities.model.PayNoteItemView;
import com.wms.utilities.model.PayNoteView;

import java.util.List;

public class PayNoteAndPayNoteItems {
    public PayNoteView getPayNoteView() {
        return payNoteView;
    }

    public List<PayNoteItemView> getPayNoteItemViews() {
        return payNoteItemViews;
    }

    public void setPayNoteView(PayNoteView payNoteView) {
        this.payNoteView = payNoteView;
    }

    public void setPayNoteItemViews(List<PayNoteItemView> payNoteItemViews) {
        this.payNoteItemViews = payNoteItemViews;
    }

    public PayNoteView payNoteView;

    public List<PayNoteItemView> payNoteItemViews;
}
