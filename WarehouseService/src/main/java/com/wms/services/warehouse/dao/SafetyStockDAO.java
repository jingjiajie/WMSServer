package com.wms.services.warehouse.dao;

import com.wms.utilities.dao.BaseDAO;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.model.SafetyStock;
import com.wms.utilities.model.SafetyStockView;


public interface SafetyStockDAO extends BaseDAO<SafetyStock,SafetyStockView>{
}
