package com.wms.services.warehouse.service;

import com.wms.services.warehouse.datastructures.StockTakingOrderItemAdd;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.service.BaseService;
import com.wms.utilities.model.StockTakingOrderItemView;
import com.wms.utilities.model.StockTakingOrderItem;
public interface StockTakingOrderItemService
        extends BaseService<StockTakingOrderItem, StockTakingOrderItemView> {
    void addStockTakingOrderItemSingle(String accountBook, StockTakingOrderItemAdd stockTakingOrderItemAdd);
    void addStockTakingOrderItemAll(String accountBook, StockTakingOrderItemAdd stockTakingOrderItemAdd);
    void setRealAmount(String accountBook,StockTakingOrderItem stockTakingOrderItem);
    long findCount(String database,Condition cond) throws WMSServiceException;
}