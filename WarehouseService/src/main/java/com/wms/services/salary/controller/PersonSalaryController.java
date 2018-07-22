package com.wms.services.salary.controller;

import com.wms.services.salary.datestructures.AddAllItem;
import com.wms.services.salary.datestructures.AddPersonSalary;
import com.wms.utilities.controller.BaseController;
import com.wms.utilities.model.PersonSalary;
import com.wms.utilities.model.PersonSalaryView;

public interface PersonSalaryController extends BaseController<PersonSalary,PersonSalaryView> {
    void addPersonSalary(String accountBook, AddPersonSalary addPersonSalary);
}
