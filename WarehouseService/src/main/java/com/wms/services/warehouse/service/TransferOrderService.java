package com.wms.services.warehouse.service;

import com.wms.services.warehouse.datastructures.TransferOrderAndItems;
import com.wms.services.warehouse.datastructures.TransferFinishArgs;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.TransferOrder;
import com.wms.utilities.model.TransferOrderView;
import com.wms.utilities.service.BaseService;

import java.util.List;

public interface TransferOrderService
    extends BaseService<TransferOrder,TransferOrderView>{

    void transferFinish(String accountBook, TransferFinishArgs transferFinishArgs) throws WMSServiceException;
    void transferSome(String accountBook, List<Integer> ids) throws WMSServiceException;
    List<TransferOrderAndItems> getPreviewData(String accountBook, List<Integer> transferOrderIDs) throws WMSServiceException;
}
