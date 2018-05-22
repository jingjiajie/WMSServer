package com.wms.services.warehouse.dao;

import com.wms.utilities.dao.BaseDAO;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.model.StorageLocation;
import com.wms.utilities.model.StorageLocationView;

public interface StorageLocationDAO extends BaseDAO<StorageLocation,StorageLocationView>{
}
