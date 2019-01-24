package com.wms.services.settlement.controller;

import com.wms.services.settlement.datastructures.AddAllItem;
import com.wms.utilities.controller.BaseController;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.PriceDetails;
import com.wms.utilities.model.SummaryNote;
import com.wms.utilities.model.SummaryNoteView;

public interface SummaryNoteController extends BaseController<SummaryNote,SummaryNoteView> {
     void generateSummaryNotes(String accountBook,AddAllItem addAllItem) throws WMSServiceException;
     PriceDetails[] find(String accountBook, int summaryNoteItemId);
}
