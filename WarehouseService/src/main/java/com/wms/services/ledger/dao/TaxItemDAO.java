package com.wms.services.ledger.dao;

import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.TaxItem;

import java.util.Map;

public interface TaxItemDAO {
    int[] add(String database, TaxItem taxItems[]) throws WMSDAOException;
    void update(String database,TaxItem taxItems[]) throws WMSDAOException;
    void remove(String database, int[] ids) throws WMSDAOException;
    TaxItem[] find(String database,Condition cond) throws WMSDAOException;
}
