package com.wms.services.warehouse.controller;

import com.wms.utilities.controller.BaseController;
import com.wms.utilities.model.InspectionNote;
import com.wms.utilities.model.InspectionNoteView;

public interface InspectionNoteController {
    void remove(String accountBook,String strIDs);
    void update(String accountBook,InspectionNote inspectionNotes[]);
    InspectionNoteView[] find(String accountBook, String condStr);
}
