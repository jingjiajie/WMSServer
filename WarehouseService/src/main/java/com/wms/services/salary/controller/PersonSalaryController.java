package com.wms.services.salary.controller;

import com.wms.services.salary.datestructures.AddAllItem;
import com.wms.services.salary.datestructures.AddPersonSalary;
import com.wms.utilities.controller.BaseController;
import com.wms.utilities.model.PersonSalary;
import com.wms.utilities.model.PersonSalaryView;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface PersonSalaryController extends BaseController<PersonSalary,PersonSalaryView> {
    void addPersonSalary(String accountBook, AddPersonSalary addPersonSalary);
    void removeNo();
    void updateNewestPeriodPersonSalary(String accountBook, AddPersonSalary addPersonSalary);
    void refreshFormula(String accountBook, AddPersonSalary addPersonSalary);
    void refreshValuation(String accountBook, AddPersonSalary addPersonSalary);
    void refreshPersonSalary(String accountBook, AddPersonSalary addPersonSalary);
}
