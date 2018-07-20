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

    int ENABLED_ON=1;
    int ENABLED_OFF =0;

    int TYPE_ASSET=0;
    int TYPE_DEBT =1;
    int TYPE_JOINT=2;
    int TYPE_EQUITY=3;
    int TYPE_Profit_and_loss=4;

}
