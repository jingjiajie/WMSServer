package com.wms.services.salary.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.PersonSalary;
import com.wms.utilities.model.PersonSalaryView;

public class PersonSalaryDAOImpl extends BaseDAOImpl<PersonSalary,PersonSalaryView> implements PersonSalaryDAO {
    public PersonSalaryDAOImpl(){
        super(PersonSalary.class,PersonSalaryView.class,PersonSalary::getId);}
}
