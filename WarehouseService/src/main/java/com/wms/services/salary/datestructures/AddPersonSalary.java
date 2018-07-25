package com.wms.services.salary.datestructures;

import java.util.List;

public class AddPersonSalary {
    public int getSalaryPeriodId() {
        return salaryPeriodId;
    }

    public List<Integer> getSalaryTypeId() {
        return salaryTypeId;
    }

    public void setSalaryPeriodId(int salaryPeriodId) {
        this.salaryPeriodId = salaryPeriodId;
    }

    public void setSalaryTypeId(List<Integer> salaryTypeId) {
        this.salaryTypeId = salaryTypeId;
    }

    private int salaryPeriodId;
    private List<Integer> salaryTypeId;

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    private int warehouseId;
}
