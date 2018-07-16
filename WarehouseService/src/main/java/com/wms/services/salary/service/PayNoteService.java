package com.wms.services.salary.service;

import com.wms.services.salary.datestructures.AccountSynchronize;
import com.wms.services.salary.datestructures.PayNoteAndItems;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.PayNote;
import com.wms.utilities.model.PayNoteView;
import com.wms.utilities.service.BaseService;

import java.util.List;

public interface PayNoteService extends BaseService<PayNote,PayNoteView> {
    void confirmToAccountTitle(String accountBook, AccountSynchronize accountSynchronize);
    void realPayToAccountTitle(String accountBook,AccountSynchronize accountSynchronize);
    List<PayNoteAndItems> getPreviewData(String accountBook, List<Integer> payNoteIds) throws WMSServiceException;
}
