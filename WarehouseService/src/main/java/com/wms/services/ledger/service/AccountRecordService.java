package com.wms.services.ledger.service;

import com.wms.services.ledger.datestructures.*;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import com.wms.utilities.service.BaseService;

import java.util.List;

public interface AccountRecordService extends BaseService<AccountRecord,AccountRecordView> {
    public void RealTransferAccount(String accountBook, TransferAccount transferAccount)throws WMSServiceException;
    public void AddAccountRecord(String accountBook, AccountRecord[] accountRecords)throws WMSServiceException;
    public List<FindLinkAccountTitle> FindParentAccountTitle(String accountBook, AccountTitle[] accountTitles)throws WMSServiceException;
    public List<FindLinkAccountTitle> FindSonAccountTitle(String accountBook, AccountTitle[] accountTitles)throws WMSServiceException;
    public int[] simpleAdd(String accountBook, AccountRecord[] accountRecords) throws WMSServiceException;
    public void writeOff(String accountBook,List<Integer> ids) throws WMSServiceException;
    public List<AccrualCheck> accrualCheck(String accountBook, AccrualCheck accrualCheck) throws WMSServiceException;
    public List<AccrualCheck> deficitCheck(String accountBook,AccrualCheck accrualCheck) throws WMSServiceException;
    public List<AccrualCheck> showBalance(String accountBook,AccrualCheck accrualCheck) throws WMSServiceException;
    public List<TreeViewData> buildAccountTitleTreeView(String accountBook) throws WMSServiceException;

    public FindBalance findNewBalance(String accountBook, AccountTitle accountTitle);
    public FindBalance findNewBalance(String accountBook,AccountTitle accountTitle,Integer curWarehouseId);
    //汇总单生成
    public List<SummaryAccountRecord>  summaryAllTitle(String accountBook,AccrualCheck accrualCheck);
    }
