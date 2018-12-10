package com.wms.services.warehouse.service;

import com.wms.services.warehouse.datastructures.*;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import com.wms.utilities.service.BaseService;

import java.sql.Timestamp;

public interface StockRecordService
        extends   BaseService<StockRecord,StockRecordView> {
    void RealTransformStock(String accountBook, TransferStock transferStock);

    void addAmount(String accountBook, TransferStock transferStock,TransferStock transferStockRestore);

    void reduceAmount(String accountBook, TransferStock transferStock,TransferStock transferStockRestore);

    void transferStock(String accountBook, TransferStock transferStock);

    void addAmountToNewestBatchNo(String accountBook,TransferStock transferStock );

    void returnSupply(String accountBook, StockRecord[] stockRecords);

    StockRecord[] find(String accountBook, StockRecordFind stockRecordFind);

    StockRecord[] findTableNewest(String accountBook,StockRecordFind stockRecordFind);

    StockRecordViewAndSumGroupBySupplyId[] findByTime(String accountBook, StockRecordFindByTime[] stockRecordFindByTimes);

    StockRecordViewNewest[] findNewest(String accountBook, Condition condition);

    Object[] findCheckSupply(String accountBook, StockRecordFind stockRecordFind,String ids,int stockTakingOrderId);

    Object[] findCheckSupplyAmountAll(String accountBook, StockRecordFind stockRecordFind,String ids,int stockTakingOrderId);

    Object[] findCheckWarehouse(String accountBook, StockRecordFind stockRecordFind,int stockTakingOrderId);

    Object[] findCheckWarehouseAmountAll(String accountBook, StockRecordFind stockRecordFind,int stockTakingOrderId);

    Object[] findLoadingWarehouse(String accountBook, StockRecordFind stockRecordFind,int stockTakingOrderId);

    Object[] findLoadingSupply (String accountBook, StockRecordFind stockRecordFind,String ids,int stockTakingOrderId);

    Object[] findQualifiedWarehouse(String accountBook, StockRecordFind stockRecordFind,int stockTakingOrderId);

    Object[] findQualifiedSupply(String accountBook, StockRecordFind stockRecordFind,String ids,int stockTakingOrderId);

    Object[] findUnqualifiedWarehouse(String accountBook, StockRecordFind stockRecordFind,int stockTakingOrderId);

    Object[] findUnqualifiedSupply(String accountBook, StockRecordFind stockRecordFind,String ids,int stockTakingOrderId);

    Object[] findBySql(String accountBook, String sql, Object[] o);

    void modifyAvailableAmount(String accountBook, TransferStock transferStock);

    void RealTransferStockUnitFlexible(String accountBook, TransferStock transferStock);

    String batchTransfer(Timestamp timestamp);

    long findCount(String database,Condition cond) throws WMSServiceException;

    void judgeOldestBatch(String accountBook, JudgeOldestBatch judgeOldestBatch);



}
