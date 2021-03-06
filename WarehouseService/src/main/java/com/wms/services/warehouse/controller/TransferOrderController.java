package com.wms.services.warehouse.controller;

import com.wms.services.warehouse.datastructures.DeliveryByTransferOrder;
import com.wms.services.warehouse.datastructures.TransferOrderAndItems;
import com.wms.services.warehouse.datastructures.TransferFinishArgs;
import com.wms.utilities.controller.BaseController;
import com.wms.utilities.model.DeliveryOrderItemView;
import com.wms.utilities.model.TransferOrder;
import com.wms.utilities.model.TransferOrderView;

import java.util.List;

public interface TransferOrderController
    extends BaseController<TransferOrder,TransferOrderView>{
    //void remove(String accountBook,String strIDs);
    //void update(String accountBook,TransferOrder transferOrders[]);
    //TransferOrderView[] find(String accountBook, String condStr);
    void transferFinish(String accountBook, TransferFinishArgs transferFinishArgs);
    void transferSome(String accountBook, List<Integer> ids,int personId);
    //long findCount(String accountBook,String condStr);
    List<TransferOrderAndItems> getPreviewData(String accountBook, String strIDs);
    List<DeliveryOrderItemView> orderToDelivery(String accountBook, DeliveryByTransferOrder deliveryByTransferOrder);
}
