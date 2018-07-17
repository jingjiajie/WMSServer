package com.wms.services.ledger.datestructures;

public class CarryOver {
    public int getWarehouseId() {
        return warehouseId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    int warehouseId;
    int personId;
}
