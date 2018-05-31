package com.wms.services.warehouse.controller;

import com.wms.services.warehouse.datastructures.InspectFinishArgs;
import com.wms.utilities.controller.BaseController;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.InspectionNote;
import com.wms.utilities.model.InspectionNoteView;

import java.util.List;

public interface InspectionNoteController {
    void remove(String accountBook,String strIDs);
    void update(String accountBook,InspectionNote inspectionNotes[]);
    InspectionNoteView[] find(String accountBook, String condStr);
    void inspectFinish(String accountBook, InspectFinishArgs inspectFinishArgs);
    long findCount(String accountBook,String condStr);
    void updateState(String accountBook, List<Integer> ids) throws WMSServiceException;
}
