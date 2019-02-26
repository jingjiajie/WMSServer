package com.wms.services.settlement.service;

import com.wms.services.settlement.datastructures.SummaryNoteItemAndDeliveryDetails;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.SummaryNoteItem;
import com.wms.utilities.model.SummaryNoteItemView;
import com.wms.utilities.service.BaseService;

import java.util.List;

public interface SummaryNoteItemService
extends BaseService<SummaryNoteItem,SummaryNoteItemView>
{
    SummaryNoteItem[] findTable(String accountBook, Condition cond) throws WMSServiceException;
    public List<SummaryNoteItemAndDeliveryDetails> getPreviewData(String accountBook, List<Integer> summaryNoteItemIds) throws WMSServiceException;
}
