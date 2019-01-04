package com.wms.services.salary.controller;

import com.wms.services.salary.datestructures.AddAllItem;
import com.wms.services.salary.datestructures.AddPersonSalary;
import com.wms.services.salary.datestructures.AddPersonSalaryRequest;
import com.wms.utilities.controller.BaseController;
import com.wms.utilities.model.PersonSalary;
import com.wms.utilities.model.PersonSalaryView;
import com.wms.utilities.model.PersonSalaryWithSumAmount;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface PersonSalaryController extends BaseController<PersonSalary,PersonSalaryView> {
    void addPersonSalary(String accountBook, AddPersonSalary addPersonSalary);
    void removeNo();
    void updateNewestPeriodPersonSalary(String accountBook, AddPersonSalary addPersonSalary);
    void refreshFormula(String accountBook, AddPersonSalaryRequest addPersonSalaryRequest);
    void refreshValuation(String accountBook, AddPersonSalaryRequest addPersonSalaryRequest);
    void refreshPersonSalary(String accountBook, AddPersonSalaryRequest addPersonSalaryRequest);
    void refreshFormulaAndValuation(String accountBook, AddPersonSalaryRequest addPersonSalaryRequest);
    void addLastPeriod(String accountBook, AddPersonSalaryRequest addPersonSalaryRequest);
    PersonSalaryWithSumAmount[] findSum( String accountBook,String condStr);
}
