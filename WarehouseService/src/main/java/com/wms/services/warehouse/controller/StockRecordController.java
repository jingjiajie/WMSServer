package com.wms.services.warehouse.controller;

import com.wms.services.warehouse.datastructures.*;
import com.wms.utilities.controller.BaseController;
import com.wms.utilities.model.StockRecord;
import com.wms.utilities.model.StockRecordView;
import com.wms.utilities.model.StockRecordViewNewest;


public interface StockRecordController
extends BaseController<StockRecord,StockRecordView> {
  void RealTransferStock(String accountbook, TransferStock transferStock);
  void addAmount(String accountbook, TransferStock transferStock);
  void returnSupply(String accountbook, StockRecord[] stockRecords);
  void throwException();
  void modifyAvailableAmount(String accountbook, TransferStock transferStock);
  StockRecord[] find(String accountbook, StockRecordFind stockRecordFind);
  StockRecordViewNewest[] findNewest(String accountbook, String condition);
  StockRecordViewNewest[] findNewestWithoutZero(String accountbook, String condition);
  StockRecordViewAndSumGroupBySupplyId[] findByTime(String accountbook, StockRecordFindByTime[] stockRecordFindByTimes);
  long findCount(String accountBook,String condStr);
  long findCountWithoutZero(String accountBook,String condStr);
  void remove(String accountBook,String strIDs);
  void judgeOldestBatch(String accountBook, JudgeOldestBatch judgeOldestBatch);
  void validateRandomCode(String accountBook, RandomCode randomCode);
}
