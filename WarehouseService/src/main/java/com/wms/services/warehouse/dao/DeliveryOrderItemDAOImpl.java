package com.wms.services.warehouse.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.DeliveryOrderItemView;
import org.springframework.stereotype.Repository;
import com.wms.utilities.model.DeliveryOrderItem;

@Repository
public class DeliveryOrderItemDAOImpl
        extends BaseDAOImpl<DeliveryOrderItem,DeliveryOrderItemView>
        implements DeliveryOrderItemDAO{

    public DeliveryOrderItemDAOImpl(){
        super(DeliveryOrderItem.class,DeliveryOrderItemView.class,DeliveryOrderItem::getId);}
}
