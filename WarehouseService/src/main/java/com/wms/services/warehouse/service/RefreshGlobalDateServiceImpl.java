package com.wms.services.warehouse.service;

import com.wms.utilities.datastructures.Condition;

public class RefreshGlobalDateServiceImpl implements RefreshGlobalDateService{
    public void refreshGlobalDate(String accountBook, int warehouseId)
    {
        Condition condWarehouse = new Condition().addCondition("warehouseId",warehouseId);
        Condition condSalaryPeriod = new Condition().addCondition("warehouseId", warehouseId);
        Condition condAccountTitle = new Condition();
    }
}
