package com.wms.services.warehouse.dao;

import com.wms.services.warehouse.model.Material;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;

public interface MaterialDAO {
    int[] add(String database,Material materials[]) throws WMSDAOException;
    void update(String database,Material materials[]) throws WMSDAOException;
    void remove(String database,int ids[]) throws WMSDAOException;
    Material[] find(String database,Condition cond) throws WMSDAOException;
}
