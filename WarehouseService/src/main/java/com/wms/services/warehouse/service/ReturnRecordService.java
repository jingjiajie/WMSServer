package com.wms.services.warehouse.service;

import com.wms.services.warehouse.datastructures.ReturnAmount;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.ReturnRecord;
import com.wms.utilities.model.ReturnRecordView;
import com.wms.utilities.service.BaseService;

import java.sql.Timestamp;

public interface ReturnRecordService
        extends BaseService<ReturnRecord,ReturnRecordView> {
    ReturnAmount findAmount(String database, int supplierId, Timestamp timestampStart, Timestamp timestampEnd);
}
