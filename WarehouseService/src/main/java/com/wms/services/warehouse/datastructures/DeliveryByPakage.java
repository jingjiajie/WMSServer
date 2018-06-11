package com.wms.services.warehouse.datastructures;

public class DeliveryByPakage {
    public Integer getPersonId() {
        return personId;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public int getPakageId() {
        return pakageId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public void setPakageId(int pakageId) {
        this.pakageId = pakageId;
    }

    private Integer personId;
    private int warehouseId;
    private int pakageId;
}
