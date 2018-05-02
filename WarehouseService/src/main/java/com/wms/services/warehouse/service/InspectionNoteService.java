package com.wms.services.warehouse.service;

import com.wms.services.warehouse.datastructures.InspectFinishArgs;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.InspectionNote;
import com.wms.utilities.model.InspectionNoteView;
import com.wms.utilities.service.BaseService;

public interface InspectionNoteService
        extends BaseService<InspectionNote,InspectionNoteView> {
    void inspectFinish(String accountBook, InspectFinishArgs inspectFinishArgs) throws WMSServiceException;
}
