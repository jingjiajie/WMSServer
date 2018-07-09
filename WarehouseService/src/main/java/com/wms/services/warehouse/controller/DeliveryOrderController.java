package com.wms.services.warehouse.controller;

import com.wms.services.warehouse.datastructures.*;
import com.wms.utilities.controller.BaseController;
import com.wms.utilities.model.DeliveryOrder;
import com.wms.utilities.model.DeliveryOrderView;
import com.wms.utilities.model.TransferOrderItem;
import com.wms.utilities.model.TransferOrderItemView;

import java.util.List;

public interface DeliveryOrderController
    extends BaseController<DeliveryOrder,DeliveryOrderView>{

    void transferPakage(String accountBook, TransferArgs transferArgs);
    List<TransferOrderItemView> transferAuto(String accountBook, TransferAuto transferAuto);

    void deliveryByPakage(String accountBook, DeliveryByPakage deliveryByPakage);
    void deliveryFinish(String accountBook,DeliveryFinish deliveryFinish);
    void decreaseInAccounting(String accountBook,List<Integer> ids);
    List<DeliveryOrderAndItems> getPreviewData(String accountBook, String strIDs);

}
