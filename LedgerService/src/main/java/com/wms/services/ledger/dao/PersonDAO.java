package com.wms.services.ledger.dao;

import com.wms.services.ledger.model.Person;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;

import java.util.Map;

public interface PersonDAO {
    void add(String database,Person person[]) throws WMSDAOException;
    void update(String database,Person person[]) throws WMSDAOException;
    void remove(String database,int[] ids) throws WMSDAOException;
    Person[] find(String database,Condition[] conds) throws WMSDAOException;
}
