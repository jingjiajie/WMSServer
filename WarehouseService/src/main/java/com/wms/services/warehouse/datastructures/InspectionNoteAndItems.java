package com.wms.services.warehouse.datastructures;

import com.wms.utilities.model.InspectionNoteItemView;
import com.wms.utilities.model.InspectionNoteView;
import com.wms.utilities.model.WarehouseEntryItemView;
import com.wms.utilities.model.WarehouseEntryView;

import java.util.List;

public class InspectionNoteAndItems {
    private InspectionNoteView inspectionNote;
    private List<InspectionNoteItemView> inspectionNoteItems;

    public InspectionNoteView getInspectionNote() {
        return inspectionNote;
    }

    public void setInspectionNote(InspectionNoteView inspectionNote) {
        this.inspectionNote = inspectionNote;
    }

    public List<InspectionNoteItemView> getInspectionNoteItems() {
        return inspectionNoteItems;
    }

    public void setInspectionNoteItems(List<InspectionNoteItemView> inspectionNoteItems) {
        this.inspectionNoteItems = inspectionNoteItems;
    }
}
