package com.wms.services.salary.datestructures;

import java.util.List;

public class AddPersonSalary {
    public int getSalaryPeriodId() {
        return salaryPeriodId;
    }

    public int getSalaryTypeId() {
        return salaryTypeId;
    }

    public void setSalaryPeriodId(int salaryPeriodId) {
        this.salaryPeriodId = salaryPeriodId;
    }

    public void setSalaryTypeId(int salaryTypeId) {
        this.salaryTypeId = salaryTypeId;
    }

    private int salaryPeriodId;
    private int salaryTypeId;

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    private int warehouseId;

    public List<Integer> getPersonSalaryIds() {
        return personSalaryIds;
    }

    public void setPersonSalaryIds(List<Integer> personSalaryIds) {
        this.personSalaryIds = personSalaryIds;
    }

    private List<Integer> personSalaryIds;
}
