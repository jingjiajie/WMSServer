package com.wms.services.warehouse.datastructures;

public class TransferArgs {
    public void setTransferItems(TransferItem[] transferItems) {
        this.transferItems = transferItems;
    }

    public TransferItem[] getTransferItems() {
        return transferItems;
    }

    private TransferItem[] transferItems;
}
