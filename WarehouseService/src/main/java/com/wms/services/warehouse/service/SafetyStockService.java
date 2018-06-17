package com.wms.services.warehouse.service;

import com.wms.utilities.model.SafetyStock;
import com.wms.utilities.model.SafetyStockView;
import com.wms.utilities.service.BaseService;


public interface SafetyStockService
    extends BaseService<SafetyStock,SafetyStockView>{
    int TYPE_READY = 1;
    int TYPE_OUT = 0;
    int TYPE_OTHER = 2;
}
