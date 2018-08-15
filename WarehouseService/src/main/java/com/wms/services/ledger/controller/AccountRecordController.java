package com.wms.services.ledger.controller;

import com.wms.services.ledger.datestructures.AccrualCheck;
import com.wms.utilities.controller.BaseController;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.AccountRecord;
import com.wms.utilities.model.AccountRecordView;

import java.util.List;

public interface AccountRecordController
        extends BaseController<AccountRecord,AccountRecordView> {
    public void writeOff(String accountBook,List<Integer> ids) throws WMSServiceException;
    public List<AccrualCheck> accrualCheck(String accountBook, AccrualCheck accrualCheck) throws WMSServiceException;
    public List<AccountRecordView> deficitCheck(String accountBook,AccrualCheck accrualCheck) throws WMSServiceException;


}
