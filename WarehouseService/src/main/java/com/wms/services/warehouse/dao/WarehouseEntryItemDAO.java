package com.wms.services.warehouse.dao;

import com.wms.services.warehouse.model.Material;
import com.wms.services.warehouse.model.WarehouseEntryItem;
import com.wms.services.warehouse.model.WarehouseEntryItemView;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;

public interface WarehouseEntryItemDAO {
    int[] add(String database, WarehouseEntryItem items[]) throws WMSDAOException;
    void update(String database, WarehouseEntryItem items[]) throws WMSDAOException;
    void remove(String database, int ids[]) throws WMSDAOException;
    WarehouseEntryItemView[] find(String database, Condition cond) throws WMSDAOException;
}
