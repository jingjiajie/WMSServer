package com.wms.services.warehouse.controller;

import java.util.Map;

public interface RefreshGlobalDateController {
    Map<String,Object[]> refreshGlobalDate(String accountBook, int warehouseId);
}
