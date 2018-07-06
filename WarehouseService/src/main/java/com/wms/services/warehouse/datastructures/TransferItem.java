package com.wms.services.warehouse.datastructures;

import com.wms.utilities.model.TransferOrder;
import com.wms.utilities.model.*;
public class TransferItem {
    private TransferOrder transferOrder;

    public TransferOrder getTransferOrder() {
        return transferOrder;
    }

    public TransferOrderItem[] getTransferOrderItems() {
        return transferOrderItems;
    }

    public void setTransferOrder(TransferOrder transferOrder) {
        this.transferOrder = transferOrder;
    }

    public void setTransferOrderItems(TransferOrderItem[] transferOrderItems) {
        this.transferOrderItems = transferOrderItems;
    }

    private TransferOrderItem[] transferOrderItems;
}
