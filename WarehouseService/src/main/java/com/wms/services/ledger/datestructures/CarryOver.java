package com.wms.services.ledger.datestructures;

import java.sql.Timestamp;

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

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    Timestamp startTime;
}
