package com.wms.services.salary.controller;

import com.wms.services.salary.datestructures.AddPersonSalary;
import com.wms.services.salary.datestructures.AddPersonSalaryRequest;
import com.wms.utilities.controller.BaseController;
import com.wms.utilities.model.PersonSalary;
import com.wms.utilities.model.PersonSalaryView;
import com.wms.utilities.model.SalaryTypePersonView;

public interface PersonSalaryController extends BaseController<PersonSalary,PersonSalaryView> {
    void addPersonSalary(String accountBook, AddPersonSalary addPersonSalary);
    void removeNo();
    void refreshFormula(String accountBook, AddPersonSalaryRequest addPersonSalaryRequest);
    void refreshValuation(String accountBook, AddPersonSalaryRequest addPersonSalaryRequest);
    void refreshPersonSalary(String accountBook, AddPersonSalaryRequest addPersonSalaryRequest);
    void refreshFormulaAndValuation(String accountBook, AddPersonSalaryRequest addPersonSalaryRequest);
    void addLastPeriod(String accountBook, AddPersonSalaryRequest addPersonSalaryRequest);
    PersonSalaryView[] findSum( String accountBook,String condStr);
    SalaryTypePersonView judgeSalaryTypePerson(String accountBook, AddPersonSalaryRequest addPersonSalaryRequest);
}
