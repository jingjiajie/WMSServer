package com.wms.services.salary.service;

import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.SalaryTypePerson;
import com.wms.utilities.model.SalaryTypePersonView;
import com.wms.utilities.service.BaseService;

public interface SalaryTypePersonService extends BaseService<SalaryTypePerson,SalaryTypePersonView> {
    SalaryTypePerson[] findTable(String accountBook, Condition cond) throws WMSServiceException;
}
