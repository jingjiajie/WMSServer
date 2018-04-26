package com.wms.services.warehouse.dao;

import com.wms.services.warehouse.model.Warehouse;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;

public interface WarehouseDAO {
    int[] add(String database, Warehouse warehouses[]) throws WMSDAOException;

    void update(String database, Warehouse warehouses[]) throws WMSDAOException;

    void remove(String database, int ids[]) throws WMSDAOException;

    Warehouse[] find(String database, Condition cond) throws WMSDAOException;
}