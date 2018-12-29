package com.wms.services.salary.controller;

import com.wms.services.salary.datestructures.AddPersonSalary;
import com.wms.utilities.controller.BaseController;
import com.wms.utilities.model.PersonSalary;
import com.wms.utilities.model.PersonSalaryView;

public interface PersonSalaryController extends BaseController<PersonSalary,PersonSalaryView> {
    void addPersonSalary(String accountBook, AddPersonSalary addPersonSalary);
    void removeNo();
    void updateNewestPeriodPersonSalary(String accountBook, AddPersonSalary addPersonSalary);
    void refreshFormula(String accountBook, AddPersonSalary addPersonSalary);
}
