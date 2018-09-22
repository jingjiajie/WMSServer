package com.wms.services.settlement.controller;

import com.wms.utilities.controller.BaseController;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.SummaryNote;
import com.wms.utilities.model.SummaryNoteView;

public interface SummaryNoteController extends BaseController<SummaryNote,SummaryNoteView> {
    public void generateSummaryNotes(String accountBook,int warehouseId,int summaryNoteId) throws WMSServiceException;
}
