package com.wms.services.ledger.service;

import com.wms.services.ledger.datestructures.TransferAccount;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.AccountRecord;
import com.wms.utilities.model.AccountRecordView;
import com.wms.utilities.service.BaseService;

public interface AccountRecordService extends BaseService<AccountRecord,AccountRecordView> {
    public void RealTransformAccount(String accountBook, TransferAccount transferAccount)throws WMSServiceException;
    public void AddAccountRecord(String accountBook, AccountRecord[] accountRecords)throws WMSServiceException;
}
