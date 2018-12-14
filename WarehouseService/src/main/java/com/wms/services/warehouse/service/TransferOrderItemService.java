package com.wms.services.warehouse.service;

import com.wms.utilities.service.BaseService;
import com.wms.utilities.model.*;

public interface TransferOrderItemService
    extends BaseService<TransferOrderItem,TransferOrderItemView>{

    int STATE_IN_TRANSFER = 0;
    int STATE_PARTIAL_FINNISH = 1;
    int STATE_ALL_FINISH = 2;
    public void autoTrans(boolean a);

    public int[] add2(String accountBook, TransferOrderItem[] transferOrderItems);
    public void update2(String accountBook, TransferOrderItem[] transferOrderItems);

}
