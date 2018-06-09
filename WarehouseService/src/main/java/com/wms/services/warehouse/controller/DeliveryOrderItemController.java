package com.wms.services.warehouse.controller;

import com.wms.utilities.controller.BaseController;
import com.wms.utilities.model.DeliveryOrderItem;
import com.wms.utilities.model.DeliveryOrderItemView;

import java.util.List;

public interface DeliveryOrderItemController
 extends  BaseController<DeliveryOrderItem,DeliveryOrderItemView>{

    void loadingSome(String accountBook,List<Integer> ids);
    void loadingALL(String accountBook,List<Integer> ids);
}
