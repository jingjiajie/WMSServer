package com.wms.services.settlement.datastructures;

import java.util.List;
import com.wms.utilities.model.DeliveryAmountDetailsView;
import com.wms.utilities.model.SummaryNoteItemView;

public class SummaryNoteItemAndDeliveryDetails {
    private SummaryNoteItemView summaryNoteItem;

    public SummaryNoteItemView getSummaryNoteItem() {
        return summaryNoteItem;
    }

    public List<DeliveryAmountDetailsView> getDeliveryAmountDetails() {
        return deliveryAmountDetails;
    }

    public void setSummaryNoteItem(SummaryNoteItemView summaryNoteItem) {
        this.summaryNoteItem = summaryNoteItem;
    }

    public void setDeliveryAmountDetails(List<DeliveryAmountDetailsView> deliveryAmountDetails) {
        this.deliveryAmountDetails = deliveryAmountDetails;
    }

    private List<DeliveryAmountDetailsView> deliveryAmountDetails;
}
