package com.wms.services.warehouse.controller;

import com.wms.services.warehouse.datastructures.TransferArgs;
import com.wms.services.warehouse.datastructures.TransferAuto;
import com.wms.utilities.controller.BaseController;
import com.wms.utilities.model.DeliveryOrder;
import com.wms.utilities.model.DeliveryOrderView;

public interface DeliveryOrderController
    extends BaseController<DeliveryOrder,DeliveryOrderView>{

    void transferPakage(String accountBook, TransferArgs transferArgs);
    void transferAuto(String accountBook, TransferAuto transferAuto);

}
