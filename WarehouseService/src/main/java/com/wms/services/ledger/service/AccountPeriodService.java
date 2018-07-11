package com.wms.services.ledger.service;

import com.wms.utilities.service.BaseService;
import com.wms.utilities.model.AccountPeriod;
import com.wms.utilities.model.AccountPeriodView;


public interface AccountPeriodService extends BaseService<AccountPeriod,AccountPeriodView> {
    int ended_ture=1;
    int ended_false=0;
}
