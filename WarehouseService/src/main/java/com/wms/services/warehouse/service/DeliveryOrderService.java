package com.wms.services.warehouse.service;

import com.wms.services.warehouse.datastructures.*;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.DeliveryOrder;
import com.wms.utilities.model.DeliveryOrderView;
import com.wms.utilities.model.TransferOrderItem;
import com.wms.utilities.model.TransferOrderItemView;
import com.wms.utilities.service.BaseService;

import java.util.List;

public interface DeliveryOrderService
 extends BaseService<DeliveryOrder,DeliveryOrderView>{

    int STATE_IN_LOADING = 0;
    int STATE_PARTIAL_LOADING = 1;
    int STATE_ALL_LOADING = 2;
    int STATE_IN_DELIVER = 3;
    int STATE_DELIVER_FINNISH = 4;
    public void transferPakage(String accountBook, TransferArgs transferArgs);
    public List<TransferOrderItemView> transferAuto(String accountBook, TransferAuto TransferAuto);
    public void deliveryFinish(String accountBook,DeliveryFinish deliveryFinish);
    public void decreaseInAccounting(String accountBook,List<Integer> ids);
    public void deliveryByPakage(String accountBook,DeliveryByPakage deliveryByPakage);
    List<DeliveryOrderAndItems> getPreviewData(String accountBook, List<Integer> deliveryOrderIDs) throws WMSServiceException;


}
