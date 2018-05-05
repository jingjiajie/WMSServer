package com.wms.services.warehouse.dao;

import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.model.DeliveryOrderItem;
import com.wms.utilities.model.DeliveryOrderItemView;

public interface DeliveryOrderItemDAO {
    int[] add(String database, DeliveryOrderItem deliveryOrderItems[]) throws WMSDAOException;

    void update(String database, DeliveryOrderItem deliveryOrderItems[]) throws WMSDAOException;

    void remove(String database, int ids[]) throws WMSDAOException;

    DeliveryOrderItemView[] find(String database, Condition cond) throws WMSDAOException;
}
