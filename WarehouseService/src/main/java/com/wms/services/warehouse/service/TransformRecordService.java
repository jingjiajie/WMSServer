package com.wms.services.warehouse.service;

import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.TransferRecord;
import com.wms.utilities.model.TransferRecordView;

public interface TransformRecordService {
    int[] add(String accountBook, TransferRecord transferRecords[]) throws WMSServiceException;
    TransferRecordView[] find(String accountBook, Condition cond) throws WMSServiceException;
}
