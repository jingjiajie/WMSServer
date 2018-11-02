package com.wms.services.warehouse.datastructures;

public class DeliveryByTransferOrder {

    public Integer getPersonId() {
        return personId;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public int getTransferOrderId() {
        return transferOrderId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public void setTransferOrderId(int transferOrderId) {
        this.transferOrderId = transferOrderId;
    }

    private Integer personId;
    private int warehouseId;
    private int transferOrderId;
}
