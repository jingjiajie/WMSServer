package com.wms.services.warehouse.service;

import com.wms.services.warehouse.datastructures.StockTakingOrderAndItems;
import com.wms.services.warehouse.datastructures.WarehouseEntryAndItems;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.StockTakingOrder;
import com.wms.utilities.model.StockTakingOrderView;
import com.wms.utilities.service.BaseService;

import java.util.List;

public interface StockTakingOrderService
        extends BaseService<StockTakingOrder, StockTakingOrderView> {
    List<StockTakingOrderAndItems> getPreviewData(String accountBook, List<Integer> stockTakingOrderIds) throws WMSServiceException;
}
