package com.wms.services.warehouse.dao;


import com.wms.utilities.model.StockTakingOrder;
import com.wms.utilities.model.StockTakingOrderView;
import org.springframework.stereotype.Repository;
import com.wms.utilities.dao.BaseDAOImpl;

@Repository
public class StockTakingOrderDAOImpl
        extends BaseDAOImpl<StockTakingOrder, StockTakingOrderView>
    implements StockTakingOrderDAO
{public StockTakingOrderDAOImpl(){
    super(StockTakingOrder.class,StockTakingOrderView.class,StockTakingOrder::getId);
}
}
