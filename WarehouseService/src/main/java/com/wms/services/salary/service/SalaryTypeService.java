package com.wms.services.salary.service;

import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.SalaryPeriod;
import com.wms.utilities.model.SalaryType;
import com.wms.utilities.model.SalaryTypeView;
import com.wms.utilities.service.BaseService;

public interface SalaryTypeService extends BaseService<SalaryType,SalaryTypeView>{
    SalaryType[] findTable(String accountBook, Condition cond) throws WMSServiceException;
}
