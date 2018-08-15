package com.wms.services.salary.service;

import com.wms.services.salary.datestructures.AddPersonSalary;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.PayNoteItem;
import com.wms.utilities.model.PersonSalary;
import com.wms.utilities.model.PersonSalaryView;
import com.wms.utilities.service.BaseService;

public interface PersonSalaryService extends BaseService<PersonSalary,PersonSalaryView> {
    void addPersonSalaryBySalaryType(String accountBook, AddPersonSalary addPersonSalary);
    void updateNewestPeriodPersonSalary(String accountBook, AddPersonSalary addPersonSalary);
    void addForNewPeriod(String accountBook, AddPersonSalary addPersonSalary);
    PersonSalary[] findTable(String accountBook, Condition cond) throws WMSServiceException;
}
