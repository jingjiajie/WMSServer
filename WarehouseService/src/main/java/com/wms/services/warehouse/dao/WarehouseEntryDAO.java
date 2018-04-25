package com.wms.services.warehouse.dao;

import com.wms.services.warehouse.model.WarehouseEntry;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;

public interface WarehouseEntryDAO {
    int[] add(String database, WarehouseEntry warehouseEntries[]) throws WMSDAOException;

    void update(String database, WarehouseEntry warehouseEntries[]) throws WMSDAOException;

    void remove(String database, int ids[]) throws WMSDAOException;

    WarehouseEntry[] find(String database, Condition cond) throws WMSDAOException;
}
