package com.wms.services.settlement.datastructures;

import java.math.BigDecimal;

public class ValidateTray {
    public BigDecimal getLength() {
        return length;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    BigDecimal length;
    BigDecimal width;

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    int warehouseId;
}
