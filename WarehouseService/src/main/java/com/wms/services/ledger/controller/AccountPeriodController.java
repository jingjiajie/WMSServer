package com.wms.services.ledger.controller;

import com.wms.services.ledger.datestructures.CarryOver;
import com.wms.utilities.controller.BaseController;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.AccountPeriod;
import com.wms.utilities.model.AccountPeriodView;

public interface AccountPeriodController
        extends BaseController<AccountPeriod,AccountPeriodView> {
    public void carryOver(String accountBook,CarryOver carryOver) throws WMSServiceException;
}
