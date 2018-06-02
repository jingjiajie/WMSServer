package com.wms.services.ledger.dao;

import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.Tax;

import java.util.Map;

public interface TaxDAO {
    int[] add(String database, Tax taxes[]) throws WMSDAOException;
    void update(String database,Tax taxes[]) throws WMSDAOException;
    void remove(String database, int[] ids) throws WMSDAOException;
    Tax[] find(String database,Condition cond) throws WMSDAOException;
}
