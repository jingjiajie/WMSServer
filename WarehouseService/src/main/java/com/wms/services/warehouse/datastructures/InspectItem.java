package com.wms.services.warehouse.datastructures;

import com.wms.utilities.model.InspectionNote;
import com.wms.utilities.model.InspectionNoteItem;

public class InspectItem {
    private InspectionNote inspectionNote;
    private InspectionNoteItem[] inspectionNoteItems;

    public InspectionNoteItem[] getInspectionNoteItems() {
        return inspectionNoteItems;
    }

    public void setInspectionNoteItems(InspectionNoteItem[] inspectionNoteItems) {
        this.inspectionNoteItems = inspectionNoteItems;
    }

    public InspectionNote getInspectionNote() {
        return inspectionNote;
    }

    public void setInspectionNote(InspectionNote inspectionNote) {
        this.inspectionNote = inspectionNote;
    }

}