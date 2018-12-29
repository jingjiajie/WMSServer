package com.wms.services.salary.service;

import com.wms.services.salary.datestructures.AddPersonSalary;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.PayNoteItem;
import com.wms.utilities.model.PersonSalary;
import com.wms.utilities.model.PersonSalaryView;
import com.wms.utilities.service.BaseService;

import java.util.List;

public interface PersonSalaryService extends BaseService<PersonSalary,PersonSalaryView> {
    void addPersonSalaryBySalaryType(String accountBook, AddPersonSalary addPersonSalary);
    void updateNewestPeriodPersonSalaryDelete(String accountBook, AddPersonSalary addPersonSalary, List<Integer> personRemoveIds);
    void updateNewestPeriodPersonSalary(String accountBook, AddPersonSalary addPersonSalary);
    void addForNewPeriod(String accountBook, AddPersonSalary addPersonSalary);
    PersonSalary[] findTable(String accountBook, Condition cond) throws WMSServiceException;
    void refreshFormula(String accountBook, AddPersonSalary addPersonSalary);
    void refreshPersonSalary(String accountBook, AddPersonSalary addPersonSalary);
    void addLastPeriod(String accountBook, AddPersonSalary addPersonSalary);
    void refreshValuation(String accountBook, AddPersonSalary addPersonSalary);
}
