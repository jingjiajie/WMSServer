package com.wms.services.warehouse.controller;

import com.wms.utilities.model.InspectionNoteItem;
import com.wms.utilities.model.InspectionNoteItemView;

public interface InspectionNoteItemController {
    void remove(String accountBook,String strIDs);
    void update(String accountBook,InspectionNoteItem inspectionNoteitems[]);
    InspectionNoteItemView[] find(String accountBook, String condStr);
}
