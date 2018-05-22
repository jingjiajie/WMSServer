package com.wms.services.warehouse.dao;

import com.wms.utilities.dao.BaseDAO;
import com.wms.utilities.model.Supplier;
import com.wms.utilities.model.SupplierView;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.model.WarehouseEntry;
import com.wms.utilities.model.WarehouseEntryView;

public interface SupplierDAO extends BaseDAO<Supplier,SupplierView>  {
}
