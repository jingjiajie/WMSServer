package com.wms.services.salary.service;

import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.SalaryPeriod;
import com.wms.utilities.model.SalaryPeriodView;
import com.wms.utilities.service.BaseService;

public interface SalaryPeriodService extends BaseService<SalaryPeriod,SalaryPeriodView> {
    SalaryPeriod[] findTable(String accountBook, Condition cond) throws WMSServiceException;
}
