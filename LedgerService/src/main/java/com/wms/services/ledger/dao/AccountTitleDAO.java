package com.wms.services.ledger.dao;

import com.wms.services.ledger.model.AccountTitle;
import com.wms.services.ledger.model.Person;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;

import java.util.Map;

public interface AccountTitleDAO {
    int[] add(String database, AccountTitle accountTitles[]) throws WMSDAOException;
}
