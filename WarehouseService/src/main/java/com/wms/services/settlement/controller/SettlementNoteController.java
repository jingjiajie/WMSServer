package com.wms.services.settlement.controller;

import com.wms.services.settlement.datastructures.LedgerSynchronous;
import com.wms.utilities.controller.BaseController;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.SettlementNote;
import com.wms.utilities.model.SettlementNoteView;

public interface SettlementNoteController extends BaseController<SettlementNote,SettlementNoteView> {
    public void synchronousReceivables(String accountBook,LedgerSynchronous ledgerSynchronous) throws WMSServiceException;
    void synchronousReceipt(String accountBook,LedgerSynchronous ledgerSynchronous);
}
