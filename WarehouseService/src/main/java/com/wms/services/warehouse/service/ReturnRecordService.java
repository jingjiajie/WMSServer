package com.wms.services.warehouse.service;

import com.wms.services.warehouse.datastructures.ReturnAmount;
import com.wms.utilities.model.ReturnRecord;
import com.wms.utilities.model.ReturnRecordView;
import com.wms.utilities.service.BaseService;

import java.sql.Timestamp;

public interface ReturnRecordService
        extends BaseService<ReturnRecord,ReturnRecordView> {
    ReturnAmount findAmount(String database, int supplyId, Timestamp timestampStart, Timestamp timestampEnd);
}
