package com.wms.services.warehouse.dao;

import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.model.DeliveryOrder;
import com.wms.utilities.model.DeliveryOrderView;

public interface DeliveryOrderDAO {
    int[] add(String database, DeliveryOrder deliveryOrders[]) throws WMSDAOException;

    void update(String database, DeliveryOrder deliveryOrders[]) throws WMSDAOException;

    void remove(String database, int ids[]) throws WMSDAOException;

    DeliveryOrderView[] find(String database, Condition cond) throws WMSDAOException;
}
