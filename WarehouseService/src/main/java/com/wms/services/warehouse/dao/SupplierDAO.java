package com.wms.services.warehouse.dao;

import com.wms.utilities.model.Supplier;
import com.wms.utilities.model.SupplierView;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;

public interface SupplierDAO {
    int[] add(String database, Supplier suppliers[]) throws WMSDAOException;

    void update(String database, Supplier suppliers[]) throws WMSDAOException;

    void remove(String database, int ids[]) throws WMSDAOException;

    SupplierView[] find(String database, Condition cond) throws WMSDAOException;



}
