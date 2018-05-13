package com.wms.utilities.dao;

import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;

public interface BaseDAO<TTable,TView> {
    int[] add(String database,TTable objs[]) throws WMSDAOException;
    void update(String database,TTable objs[]) throws WMSDAOException;
    void remove(String database,int ids[]) throws WMSDAOException;
    TView[] find(String database, Condition cond) throws WMSDAOException;
    long findCount(String database,Condition cond) throws WMSDAOException;
}
