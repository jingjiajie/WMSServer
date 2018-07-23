package com.wms.services.ledger.service;

import com.wms.services.ledger.datestructures.FindLinkAccountTitle;
import com.wms.services.ledger.datestructures.TransferAccount;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.AccountRecord;
import com.wms.utilities.model.AccountRecordView;
import com.wms.utilities.model.*;
import com.wms.utilities.service.BaseService;

import java.util.List;

public interface AccountRecordService extends BaseService<AccountRecord,AccountRecordView> {
    public void RealTransformAccount(String accountBook, TransferAccount transferAccount)throws WMSServiceException;
    public void AddAccountRecord(String accountBook, AccountRecord[] accountRecords)throws WMSServiceException;
    public List<FindLinkAccountTitle> FindParentAccountTitle(String accountBook, AccountTitle[] accountTitles)throws WMSServiceException;
    public List<FindLinkAccountTitle> FindSonAccountTitle(String accountBook, AccountTitle[] accountTitles)throws WMSServiceException;
}
