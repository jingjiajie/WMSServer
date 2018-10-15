package com.wms.services.settlement.datastructures;

public class AddAllItem {
    public int getWarehouseId() {
        return warehouseId;
    }

    public int getSummaryNoteId() {
        return summaryNoteId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public void setSummaryNoteId(int summaryNoteId) {
        this.summaryNoteId = summaryNoteId;
    }

    private int warehouseId;
    private int summaryNoteId;
}
