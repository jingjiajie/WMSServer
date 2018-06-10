package com.wms.services.warehouse.service;

import com.wms.services.warehouse.datastructures.DeliveryOrderAndItems;
import com.wms.services.warehouse.datastructures.TransferArgs;
import com.wms.services.warehouse.datastructures.TransferAuto;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.service.BaseService;
import com.wms.utilities.model.DeliveryOrder;
import com.wms.utilities.model.DeliveryOrderView;

import java.util.List;

public interface DeliveryOrderService
 extends BaseService<DeliveryOrder,DeliveryOrderView>{

    int STATE_IN_LOADING = 0;
    int STATE_PARTIAL_LOADING = 1;
    int STATE_ALL_LOADING = 2;
    int STATE_IN_DELIVER = 3;
    int STATE_DELIVER_FINNISH = 4;
    public void transferPakage(String accountBook, TransferArgs transferArgs);
    public void transferAuto(String accountBook, TransferAuto TransferAuto);
    public void deliveryFinish(String accountBook,List<Integer> ids);
    public void decreaseInAccounting(String accountBook,List<Integer> ids);
    List<DeliveryOrderAndItems> getPreviewData(String accountBook, List<Integer> deliveryOrderIDs) throws WMSServiceException;


}
