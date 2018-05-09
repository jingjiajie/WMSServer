package com.wms.services.warehouse.service;

import com.wms.services.warehouse.datastructures.StockRecordFind;
import com.wms.services.warehouse.datastructures.TransferStock;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import com.wms.utilities.service.BaseService;

public interface StockRecordService
        extends   BaseService<StockRecord,StockRecordView>
{
    public void RealTransformStock(String accountBook,TransferStock transferStock);
    public void addAmount(String accountBook,TransferStock transferStock );
    public StockRecordView[] find(String accountBook, StockRecordFind stockRecordFind);
    public void modifyAvailableAmount(String accountBook,TransferStock transferStock);
}
