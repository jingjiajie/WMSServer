package com.wms.services.warehouse.service;

import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.DeliveryOrderItem;
import com.wms.utilities.model.DeliveryOrderItemView;
import com.wms.utilities.service.BaseService;

import java.util.List;

public interface DeliveryOrderItemService
extends BaseService<DeliveryOrderItem,DeliveryOrderItemView>{

    void loadingSome(String accountBook, List<Integer> ids) throws WMSServiceException;
    void loadingALL(String accountBook, List<Integer> ids) throws WMSServiceException;
    public void updateSleep();

    public void remove2(String accountBook, int[] ids);
    public void update2(String accountBook, DeliveryOrderItem[] deliveryOrderItems);
    public int[] add2(String accountBook, DeliveryOrderItem[] deliveryOrderItems);
}
