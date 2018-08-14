package com.wms.services.salary.service;

import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.SalaryItem;
import com.wms.utilities.model.SalaryItemView;
import com.wms.utilities.model.SalaryPeriod;
import com.wms.utilities.service.BaseService;

public interface SalaryItemService extends BaseService<SalaryItem,SalaryItemView> {
    SalaryItem[] findTable(String accountBook, Condition cond) throws WMSServiceException;
}
