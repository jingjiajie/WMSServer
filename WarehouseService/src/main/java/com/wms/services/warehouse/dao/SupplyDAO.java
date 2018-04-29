package com.wms.services.warehouse.dao;

import com.wms.utilities.model.Supply;
import com.wms.utilities.model.SupplyView;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;

public interface SupplyDAO {
    int[] add(String database,Supply supplys[]) throws WMSDAOException;

    void update(String database,Supply supplys[]) throws WMSDAOException;

    void remove(String database,int ids[]) throws WMSDAOException;

    SupplyView[] find(String database, Condition cond) throws WMSDAOException;
}
