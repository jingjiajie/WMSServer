package com.wms.services.warehouse.service;

import java.util.Map;

public interface RefreshGlobalDateService {
    Map<String,Object[]> refreshGlobalDate(String accountBook, int warehouseId);
}
