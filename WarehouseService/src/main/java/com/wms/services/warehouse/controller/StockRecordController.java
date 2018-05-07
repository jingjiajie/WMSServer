package com.wms.services.warehouse.controller;

import com.wms.services.warehouse.datastructures.TransferStock;
import com.wms.utilities.controller.BaseController;
import com.wms.utilities.model.StockRecord;
import com.wms.utilities.model.StockRecordView;

public interface StockRecordController
extends BaseController<StockRecord,StockRecordView> {
  void transferStock(String accountbook, TransferStock transferStock);
  void modifyAmount(String accountbook, TransferStock transferStock);
}
