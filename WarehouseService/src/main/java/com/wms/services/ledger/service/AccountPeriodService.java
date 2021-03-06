package com.wms.services.ledger.service;

import com.wms.services.ledger.datestructures.CarryOver;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.service.BaseService;
import com.wms.utilities.model.AccountPeriod;
import com.wms.utilities.model.AccountPeriodView;


public interface AccountPeriodService extends BaseService<AccountPeriod,AccountPeriodView> {
    int ended_ture=1;
    int ended_false=0;
    public void carryOver(String accountBook,CarryOver carryOver) throws WMSServiceException;
}
