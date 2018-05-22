package com.wms.services.warehouse.dao;

import com.wms.utilities.dao.BaseDAO;
import com.wms.utilities.model.Warehouse;
import com.wms.utilities.model.WarehouseView;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;

public interface WarehouseDAO  extends BaseDAO<Warehouse,WarehouseView>{
}
