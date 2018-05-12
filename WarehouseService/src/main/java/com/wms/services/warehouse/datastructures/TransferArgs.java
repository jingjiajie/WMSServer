package com.wms.services.warehouse.datastructures;

public class TransferArgs {
    public void setTransferItems(TransferItem[] transferItems) {
        this.transferItems = transferItems;
    }

    public TransferItem[] getTransferItems() {
        return transferItems;
    }

    private TransferItem[] transferItems;

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    private boolean autoCommit =true;
}
