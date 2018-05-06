package com.wms.services.warehouse.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.StockTakingOrderItem;
import com.wms.utilities.model.StockTakingOrderItemView;
import org.springframework.stereotype.Repository;

@Repository
public class StockTakingOrderItemDAOImpl
        extends BaseDAOImpl<StockTakingOrderItem, StockTakingOrderItemView>
        implements StockTakingOrderItemDAO
{
    public StockTakingOrderItemDAOImpl(){ super(StockTakingOrderItem.class,StockTakingOrderItemView.class,StockTakingOrderItem::getId); }
}

