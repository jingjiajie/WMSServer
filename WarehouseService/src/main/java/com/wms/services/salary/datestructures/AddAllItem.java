package com.wms.services.salary.datestructures;

public class AddAllItem {
    public int getWarehouseId() {
        return warehouseId;
    }

    public int getPayNoteId() {
        return payNoteId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public void setPayNoteId(int payNoteId) {
        this.payNoteId = payNoteId;
    }

    int warehouseId;

    int payNoteId;
}
