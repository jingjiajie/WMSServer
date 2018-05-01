package com.wms.services.warehouse.datastructures;

public class InspectArgs {
    private InspectItem[] inspectItems;

    public InspectItem[] getInspectItems() {
        return inspectItems;
    }

    public void setInspectItems(InspectItem[] inspectItems) {
        this.inspectItems = inspectItems;
    }
}