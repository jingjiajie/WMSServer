package com.wms.services.warehouse.dao;

import com.wms.services.warehouse.model.Supplier;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;

public interface SupplierDAO {
    int[] add(String database,Supplier suppliers[]) throws WMSDAOException;
    void update(String database,Supplier suppliers[]) throws WMSDAOException;
    void remove(String database,int ids[]) throws WMSDAOException;
    Supplier[] find(String database,Condition cond) throws WMSDAOException;
    //  List<Supplier> findInside(String database, String sql) throws WMSDAOException;
}

