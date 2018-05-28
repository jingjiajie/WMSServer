package com.wms.services.warehouse.datastructures;

public class TransferAuto {
    private int warehouseId;
    private int supplyId;

    public int getWarehouseId() {
        return warehouseId;
    }

    public int getSupplyId() {
        return supplyId;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public void setSupplyId(int supplyId) {
        this.supplyId = supplyId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    private Integer personId;

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    private boolean autoCommit =true;
}
