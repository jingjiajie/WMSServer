package com.wms.services.warehouse.service;

import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.ItemRelatedRecord;
import com.wms.utilities.model.Warehouse;
import com.wms.utilities.service.BaseService;

public interface ItemRelatedRecordService
extends BaseService<ItemRelatedRecord,ItemRelatedRecord>{
    ItemRelatedRecord[] findTable(String accountBook, Condition cond) throws WMSServiceException;
}

