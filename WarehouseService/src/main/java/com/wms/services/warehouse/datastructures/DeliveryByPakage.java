package com.wms.services.warehouse.datastructures;

public class DeliveryByPakage {
    public Integer getPersonId() {
        return personId;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    private Integer personId;
    private int warehouseId;
    private int packageId;
}
