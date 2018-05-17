package com.wms.services.warehouse.dao;

import com.wms.utilities.dao.BaseDAO;
import com.wms.utilities.model.Material;
import com.wms.utilities.model.WarehouseEntryItem;
import com.wms.utilities.model.WarehouseEntryItemView;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;

public interface WarehouseEntryItemDAO
        extends BaseDAO<WarehouseEntryItem,WarehouseEntryItemView>{
}
