package com.wms.services.warehouse.controller;

import com.wms.services.warehouse.datastructures.*;
import com.wms.utilities.controller.BaseController;
import com.wms.utilities.model.*;

import java.util.List;

public interface DeliveryOrderController
    extends BaseController<DeliveryOrder,DeliveryOrderView>{

    void transferPakage(String accountBook, TransferArgs transferArgs);
    List<TransferOrderItemView> transferAuto(String accountBook, TransferAuto transferAuto);

    List<DeliveryOrderItemView> deliveryByPakage(String accountBook, DeliveryByPakage deliveryByPakage);
    void deliveryFinish(String accountBook,DeliveryFinish deliveryFinish);
    void decreaseInAccounting(String accountBook,List<Integer> ids);
    List<DeliveryOrderAndItems> getPreviewData(String accountBook, String strIDs);

}
