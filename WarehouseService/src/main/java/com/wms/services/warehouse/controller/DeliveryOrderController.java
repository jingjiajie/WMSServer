package com.wms.services.warehouse.controller;

import com.wms.services.warehouse.datastructures.DeliveryByPakage;
import com.wms.services.warehouse.datastructures.DeliveryOrderAndItems;
import com.wms.services.warehouse.datastructures.TransferArgs;
import com.wms.services.warehouse.datastructures.TransferAuto;
import com.wms.utilities.controller.BaseController;
import com.wms.utilities.model.DeliveryOrder;
import com.wms.utilities.model.DeliveryOrderView;

import java.util.List;

public interface DeliveryOrderController
    extends BaseController<DeliveryOrder,DeliveryOrderView>{

    void transferPakage(String accountBook, TransferArgs transferArgs);
    void transferAuto(String accountBook, TransferAuto transferAuto);

    void deliveryByPakage(String accountBook, DeliveryByPakage deliveryByPakage);
    void deliveryFinish(String accountBook,List<Integer> ids);
    void decreaseInAccounting(String accountBook,List<Integer> ids);
    List<DeliveryOrderAndItems> getPreviewData(String accountBook, String strIDs);

}
