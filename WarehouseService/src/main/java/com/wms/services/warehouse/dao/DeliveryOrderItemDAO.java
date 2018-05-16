package com.wms.services.warehouse.dao;

import com.wms.utilities.dao.BaseDAO;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.model.DeliveryOrderItem;
import com.wms.utilities.model.DeliveryOrderItemView;

public interface DeliveryOrderItemDAO extends BaseDAO<DeliveryOrderItem,DeliveryOrderItemView>{
}
