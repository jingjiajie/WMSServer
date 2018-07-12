package com.wms.services.ledger.service;

import com.wms.utilities.model.TaxItem;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.TaxItemView;
import com.wms.utilities.service.BaseService;

import java.util.Map;

public interface TaxItemService extends BaseService<TaxItem,TaxItemView> {
    int Type_QUOTA=0;
    int Type_PROPORTION=1;
}