package com.wms.services.salary.datestructures;

import java.util.List;

public class AddPersonSalaryRequest {
    public int getSalaryPeriodId() {
        return salaryPeriodId;
    }

    public List<Integer> getSalaryTypeIds() {
        return salaryTypeIds;
    }

    public List<Integer> getPersonSalaryIds() {
        return personSalaryIds;
    }

    public void setSalaryPeriodId(int salaryPeriodId) {
        this.salaryPeriodId = salaryPeriodId;
    }

    public void setSalaryTypeIds(List<Integer> salaryTypeIds) {
        this.salaryTypeIds = salaryTypeIds;
    }

    public void setPersonSalaryIds(List<Integer> personSalaryIds) {
        this.personSalaryIds = personSalaryIds;
    }

    private int salaryPeriodId;
    private List<Integer> salaryTypeIds;
    private List<Integer> personSalaryIds;

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    private int warehouseId;
}
