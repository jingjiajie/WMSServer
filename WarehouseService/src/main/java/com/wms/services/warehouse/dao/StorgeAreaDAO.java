package com.wms.services.warehouse.dao;

import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.services.warehouse.model.StorageArea;
public interface StorgeAreaDAO {
    int[] add(String database,StorageArea storageAreas[]) throws WMSDAOException;
    void update(String database,StorageArea storageAreas[]) throws WMSDAOException;
    void remove(String database,int ids[]) throws WMSDAOException;
    StorageArea[] find(String database, Condition cond) throws WMSDAOException;
}
