package com.wms.services.warehouse.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import org.springframework.stereotype.Repository;
import com.wms.utilities.model.StockTakingOrderItemView;
import com.wms.utilities.model.StockTakingOrderItem;
@Repository
public class StockTakingOrderItemDAOImpl
        extends BaseDAOImpl<StockTakingOrderItem, StockTakingOrderItemView>
        implements StockTakingOrderItemDAO
{
    public StockTakingOrderItemDAOImpl(){ super(StockTakingOrderItem.class,StockTakingOrderItemView.class,StockTakingOrderItem::getId); }
}

