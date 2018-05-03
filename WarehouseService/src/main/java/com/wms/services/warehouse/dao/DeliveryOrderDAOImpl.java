package com.wms.services.warehouse.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.DeliveryOrder;
import com.wms.utilities.model.DeliveryOrderView;
import org.springframework.stereotype.Repository;

@Repository
public class DeliveryOrderDAOImpl
    extends BaseDAOImpl<DeliveryOrder,DeliveryOrderView>
    implements DeliveryOrderDAO{

    public DeliveryOrderDAOImpl(){
        super(DeliveryOrder.class,DeliveryOrderView.class,DeliveryOrder::getId);}
}
