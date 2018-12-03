package com.wms.services.settlement.datastructures;

import java.math.BigDecimal;

public class ValidateTray {
    public BigDecimal getLentgth() {
        return lentgth;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setLentgth(BigDecimal lentgth) {
        this.lentgth = lentgth;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    BigDecimal lentgth;
    BigDecimal width;

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    int warehouseId;
}
