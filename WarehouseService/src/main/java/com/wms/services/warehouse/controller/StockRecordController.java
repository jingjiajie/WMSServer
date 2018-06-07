package com.wms.services.warehouse.controller;

import com.wms.services.warehouse.datastructures.StockRecordFind;
import com.wms.services.warehouse.datastructures.TransferStock;
import com.wms.utilities.controller.BaseController;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.StockRecord;
import com.wms.utilities.model.StockRecordView;
import com.wms.utilities.model.StockRecordViewNewest;


public interface StockRecordController
extends BaseController<StockRecord,StockRecordView> {
  void RealTransferStock(String accountbook, TransferStock transferStock);
  void addAmount(String accountbook, TransferStock transferStock);
  void returnSupply(String accountbook, StockRecord[] stockRecords);
  void modifyAvailableAmount(String accountbook, TransferStock transferStock);
  StockRecord[] find(String accountbook, StockRecordFind stockRecordFind);
  StockRecordViewNewest[] findNewest(String accountbook, String condition);
  long findCount(String accountBook,String condStr);
}
