package com.wms.services.warehouse.dao;

import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.model.PackageItem;
import com.wms.utilities.model.PackageItemView;

public interface PackageItemDAO {
    int[] add(String database, PackageItem items[]) throws WMSDAOException;
    void update(String database, PackageItem items[]) throws WMSDAOException;
    void remove(String database, int ids[]) throws WMSDAOException;
    PackageItemView[] find(String database, Condition cond) throws WMSDAOException;
}
