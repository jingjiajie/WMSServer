package com.wms.services.warehouse.datastructures;

import com.wms.utilities.model.WarehouseEntryItemView;
import com.wms.utilities.model.WarehouseEntryView;

import java.util.List;

public class WarehouseEntryAndItems {
    private WarehouseEntryView warehouseEntry;
    private List<WarehouseEntryItemView> warehouseEntryItems;


    public WarehouseEntryView getWarehouseEntry() {
        return warehouseEntry;
    }

    public void setWarehouseEntry(WarehouseEntryView warehouseEntry) {
        this.warehouseEntry = warehouseEntry;
    }

    public List<WarehouseEntryItemView> getWarehouseEntryItems() {
        return warehouseEntryItems;
    }

    public void setWarehouseEntryItems(List<WarehouseEntryItemView> warehouseEntryItems) {
        this.warehouseEntryItems = warehouseEntryItems;
    }
}
