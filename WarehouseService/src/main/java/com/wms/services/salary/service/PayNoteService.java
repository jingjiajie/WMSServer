package com.wms.services.salary.service;

import com.wms.services.salary.datestructures.CalculateTax;
import com.wms.services.salary.datestructures.PayNoteAndItems;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.PayNote;
import com.wms.utilities.model.PayNoteView;
import com.wms.utilities.service.BaseService;

import java.util.List;

public interface PayNoteService extends BaseService<PayNote,PayNoteView> {
    void confirmToAccountTitle(String accountBook, int payNoteId );
    void realPayToAccountTitle(String accountBook,int payNoteId);
    List<PayNoteAndItems> getPreviewData(String accountBook, List<Integer> payNoteIds) throws WMSServiceException;
}
