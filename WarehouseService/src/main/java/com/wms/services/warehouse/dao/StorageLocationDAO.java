package com.wms.services.warehouse.dao;

import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.model.StorageLocation;
import com.wms.utilities.model.StorageLocationView;

public interface StorageLocationDAO {
    int[] add(String database,StorageLocation storageLocations[]) throws WMSDAOException;
    void update(String database,StorageLocation storageLocations[]) throws WMSDAOException;
    void remove(String database,int ids[]) throws WMSDAOException;
    StorageLocationView[] find(String database, Condition cond) throws WMSDAOException;
}
