package com.wms.services.ledger.service;

import com.sun.corba.se.spi.presentation.rmi.IDLNameTranslator;
import com.wms.utilities.model.AccountTitle;
import com.wms.utilities.model.AccountTitleView;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.service.BaseService;

public interface AccountTitleService extends BaseService<AccountTitle,AccountTitleView>{
    int Debit=0;
    int Credit =1;
}
