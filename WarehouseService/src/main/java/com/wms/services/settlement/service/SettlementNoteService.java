package com.wms.services.settlement.service;

import com.wms.services.settlement.datastructures.LedgerSynchronous;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.SettlementNote;
import com.wms.utilities.model.SettlementNoteView;
import com.wms.utilities.service.BaseService;

public interface SettlementNoteService
        extends BaseService<SettlementNote,SettlementNoteView> {
    int To_be_confirmed=0;
    int Synchronous_receivables=1;
    int Synchronous_receipt=2;

    public void synchronousReceivables(String accountBook,LedgerSynchronous ledgerSynchronous) throws WMSServiceException;
    public void synchronousReceipt(String accountBook,LedgerSynchronous ledgerSynchronous) throws WMSServiceException;
}
