package com.wms.utilities.dao;

import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.model.CommonData;

public interface CommonDataDAO {
    int[] add(String database,CommonData materials[]) throws WMSDAOException;
    void update(String database,CommonData materials[]) throws WMSDAOException;
    void remove(String database,int ids[]) throws WMSDAOException;
    CommonData[] find(String database, Condition cond) throws WMSDAOException;
}
