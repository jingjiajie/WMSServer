package com.wms.services.settlement.controller;

import com.wms.services.settlement.datastructures.SummaryNoteItemAndDeliveryDetails;
import com.wms.utilities.controller.BaseController;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.SummaryNoteItem;
import com.wms.utilities.model.SummaryNoteItemView;

import java.util.List;

public interface SummaryNoteItemController extends BaseController<SummaryNoteItem,SummaryNoteItemView> {
    List<SummaryNoteItemAndDeliveryDetails> getPreviewData(String accountBook, String strIDs);
}
