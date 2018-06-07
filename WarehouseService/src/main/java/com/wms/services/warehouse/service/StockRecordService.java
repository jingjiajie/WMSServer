package com.wms.services.warehouse.service;

import com.wms.services.warehouse.datastructures.StockRecordFind;
import com.wms.services.warehouse.datastructures.TransferStock;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import com.wms.utilities.service.BaseService;

import java.sql.Timestamp;

public interface StockRecordService
        extends   BaseService<StockRecord,StockRecordView> {
    void RealTransformStock(String accountBook, TransferStock transferStock);

    void addAmount(String accountBook, TransferStock transferStock);

    void returnSupply(String accountBook, StockRecord[] stockRecords);

    StockRecord[] find(String accountBook, StockRecordFind stockRecordFind);

    StockRecordViewNewest[] findNewest(String accountBook, Condition condition);

    Object[] findCheckSupply(String accountBook, StockRecordFind stockRecordFind,String ids);

    Object[] findCheckSupplyAmountAll(String accountBook, StockRecordFind stockRecordFind,String ids);

    Object[] findCheckWarehouse(String accountBook, StockRecordFind stockRecordFind);

    Object[] findCheckWarehouseAmountAll(String accountBook, StockRecordFind stockRecordFind);

    Object[] findLoadingWarehouse(String accountBook, StockRecordFind stockRecordFind);

    Object[] findLoadingSupply (String accountBook, StockRecordFind stockRecordFind,String ids);

    Object[] findBySql(String accountBook, String sql, Object[] o);

    void modifyAvailableAmount(String accountBook, TransferStock transferStock);

    void RealTransferStockUnitFlexible(String accountBook, TransferStock transferStock);

    String batchTransfer(Timestamp timestamp);

    long findCount(String database,Condition cond) throws WMSServiceException;
}
