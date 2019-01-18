package com.wms.services.salary.service;

import com.wms.services.salary.datestructures.AccountSynchronize;
import com.wms.services.salary.datestructures.PayNoteAndItems;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.AccountTitleView;
import com.wms.utilities.model.PayNote;
import com.wms.utilities.model.PayNoteView;
import com.wms.utilities.model.SalaryItem;
import com.wms.utilities.service.BaseService;

import java.util.List;

public interface PayNoteService extends BaseService<PayNote,PayNoteView> {
    void realPayToAccountTitle(String accountBook,AccountSynchronize accountSynchronize);
    List<PayNoteAndItems> getPreviewData(String accountBook, List<Integer> payNoteIds) throws WMSServiceException;
    PayNote[] findTable(String accountBook, Condition cond) throws WMSServiceException;
    AccountTitleView[] findSonTitleForAssociation(String accountBook, Condition condition);
}
