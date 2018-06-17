package com.wms.services.warehouse.datastructures;

import com.wms.services.warehouse.service.SafetyStockService;

public class TransferAuto {
    private int warehouseId;

    public int getTransferType() {
        return transferType;
    }

    public void setTransferType(int transferType) {
        this.transferType = transferType;
    }

    private int transferType;

    public int getWarehouseId() {
        return warehouseId;
    }


    public Integer getPersonId() {
        return personId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
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
