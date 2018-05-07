package com.wms.services.warehouse.dao;

import com.wms.utilities.model.SafetyStock;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.model.SafetyStockView;

public interface SafetyStockDAO {
    int[] add(String database,SafetyStock safetyStocks[]) throws WMSDAOException;
    void update(String database,SafetyStock safetyStocks[]) throws WMSDAOException;
    void remove(String database,int ids[]) throws WMSDAOException;
    SafetyStockView[] find(String database, Condition cond) throws WMSDAOException;
}
