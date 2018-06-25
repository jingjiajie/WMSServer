package com.wms.services.warehouse.datastructures;

import com.wms.utilities.model.TransferOrderItemView;
import com.wms.utilities.model.TransferOrderView;

import java.util.List;

public class TransferOrderAndItems {

    private TransferOrderView transferOrder;
    private List<TransferOrderItemView> transferOrderItems;

    public TransferOrderView getTransferOrder() {
        return transferOrder;
    }

    public List<TransferOrderItemView> getTransferOrderItems() {
        return transferOrderItems;
    }

    public void setTransferOrder(TransferOrderView transferOrder) {
        this.transferOrder = transferOrder;
    }

    public void setTransferOrderItems(List<TransferOrderItemView> transferOrderItems) {
        this.transferOrderItems = transferOrderItems;
    }


}
