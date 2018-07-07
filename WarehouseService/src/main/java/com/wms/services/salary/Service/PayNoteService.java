package com.wms.services.salary.service;

import com.wms.services.salary.datestructures.CalculateTax;
import com.wms.utilities.model.PayNote;
import com.wms.utilities.model.PayNoteView;
import com.wms.utilities.service.BaseService;

public interface PayNoteService extends BaseService<PayNote,PayNoteView> {
    void confirmToAccountTitle(String accountBook, int payNoteId );
    void realPay(String accountBook,int payNoteId);
}
