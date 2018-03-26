package com.wms.services.ledger.dao;

import com.wms.services.ledger.model.Person;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;

import java.util.Map;

public interface PersonDAO {
    int[] add(String database,Person persons[]) throws WMSDAOException;
    void update(String database,Person persons[]) throws WMSDAOException;
    void remove(String database,int ids[]) throws WMSDAOException;
    Person[] find(String database,Condition cond) throws WMSDAOException;
}
