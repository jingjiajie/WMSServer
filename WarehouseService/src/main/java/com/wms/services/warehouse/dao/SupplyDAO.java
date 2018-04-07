package com.wms.services.warehouse.dao;

import com.wms.services.warehouse.model.Supply;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;

public interface SupplyDAO {
    int[] add(String database,Supply supplys[]) throws WMSDAOException;
    void update(String database,Supply supplys[]) throws WMSDAOException;
    void remove(String database,int ids[]) throws WMSDAOException;
    Supply[] find(String database,Condition cond) throws WMSDAOException;
    //  List<Supplier> findInside(String database, String sql) throws WMSDAOException;
}
