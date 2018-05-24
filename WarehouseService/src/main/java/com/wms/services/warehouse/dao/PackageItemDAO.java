package com.wms.services.warehouse.dao;

import com.wms.utilities.dao.BaseDAO;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.model.PackageItem;
import com.wms.utilities.model.PackageItemView;

public interface PackageItemDAO extends BaseDAO<PackageItem,PackageItemView> {

}
