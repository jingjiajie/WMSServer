package com.wms.services.salary.service;

import com.wms.services.salary.datestructures.AddPersonSalary;
import com.wms.utilities.model.PersonSalary;
import com.wms.utilities.model.PersonSalaryView;
import com.wms.utilities.service.BaseService;

public interface PersonSalaryService extends BaseService<PersonSalary,PersonSalaryView> {
    void addPersonSalaryBySalaryType(String accountBook, AddPersonSalary addPersonSalary);
    void updateNewestPeriodPersonSalary(String accountBook, AddPersonSalary addPersonSalary);
}
