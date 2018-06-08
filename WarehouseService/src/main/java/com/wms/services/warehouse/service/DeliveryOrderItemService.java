package com.wms.services.warehouse.service;

import com.wms.services.warehouse.datastructures.DeliveryOrderAndItems;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.DeliveryOrderItemView;
import com.wms.utilities.service.BaseService;
import com.wms.utilities.model.DeliveryOrderItem;

import java.util.List;

public interface DeliveryOrderItemService
extends BaseService<DeliveryOrderItem,DeliveryOrderItemView>{

    void loadingSome(String accountBook, List<Integer> ids) throws WMSServiceException;
    void loadingALL(String accountBook, List<Integer> ids) throws WMSServiceException;
}
