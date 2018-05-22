package com.wms.services.warehouse.controller;

import com.wms.services.warehouse.datastructures.StockRecordFind;
import com.wms.services.warehouse.datastructures.TransferStock;
import com.wms.utilities.controller.BaseController;
import com.wms.utilities.model.StockRecord;
import com.wms.utilities.model.StockRecordView;


public interface StockRecordController
extends BaseController<StockRecord,StockRecordView> {
  void RealTransferStock(String accountbook, TransferStock transferStock);
  void addAmount(String accountbook, TransferStock transferStock);
  void modifyAvailableAmount(String accountbook, TransferStock transferStock);
  StockRecordView[] find(String accountbook, StockRecordFind stockRecordFind);
  long findCount(String accountBook,String condStr);
}
