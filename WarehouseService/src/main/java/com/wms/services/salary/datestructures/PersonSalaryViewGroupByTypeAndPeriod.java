package com.wms.services.salary.datestructures;

import com.wms.utilities.model.PersonSalaryView;

public class PersonSalaryViewGroupByTypeAndPeriod {
    public PersonSalaryView getPersonSalaryView() {
        return personSalaryView;
    }

    public String getGroupCondition() {
        return groupCondition;
    }

    public void setPersonSalaryViews(PersonSalaryView personSalaryViews) {
        this.personSalaryView = personSalaryViews;
    }

    public void setGroupCondition(String groupCondition) {
        this.groupCondition = groupCondition;
    }

    private PersonSalaryView personSalaryView;
    private String groupCondition;


}
