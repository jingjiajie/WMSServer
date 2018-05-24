package com.wms.services.warehouse.controller;

import com.wms.services.warehouse.datastructures.TransferFinishArgs;
import com.wms.utilities.controller.BaseController;
import com.wms.utilities.model.TransferOrder;
import com.wms.utilities.model.TransferOrderView;

public interface TransferOrderController {
    void remove(String accountBook,String strIDs);
    void update(String accountBook,TransferOrder transferOrders[]);
    TransferOrderView[] find(String accountBook, String condStr);
    void transferFinish(String accountBook, TransferFinishArgs transferFinishArgs);
    long findCount(String accountBook,String condStr);
}
