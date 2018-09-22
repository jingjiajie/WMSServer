package com.wms.services.settlement.service;

import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.SummaryNote;
import com.wms.utilities.model.SummaryNoteView;
import com.wms.utilities.service.BaseService;

public interface SummaryNoteService
        extends BaseService<SummaryNote,SummaryNoteView> {
    void summaryDelivery(String accountBook,SummaryNote summaryNote);
    void generateSummaryNotes(String accountBook,int warehouseId,int summaryNoteId) throws WMSServiceException;
}
