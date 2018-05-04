package com.wms.services.warehouse.service;

import com.wms.services.warehouse.datastructures.InspectArgs;
import com.wms.utilities.model.WarehouseEntry;
import com.wms.utilities.model.WarehouseEntryView;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.service.BaseService;

public interface WarehouseEntryService
        extends BaseService<WarehouseEntry, WarehouseEntryView> {

    /**
     * 送检
     *
     * @param accountBook 账套
     * @param inspectArgs 送检参数
     * @see InspectArgs
     */
    public void inspect(String accountBook, InspectArgs inspectArgs);
}
