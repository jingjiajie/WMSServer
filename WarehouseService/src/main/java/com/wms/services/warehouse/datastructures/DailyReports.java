package com.wms.services.warehouse.datastructures;

import java.math.BigDecimal;
import java.sql.Timestamp;

//0入库 1出库
public class DailyReports {
    public String getSupplierName() {
        return supplierName;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public String getMaterialName() {
        return materialName;
    }

    public BigDecimal getAmountDiff() {
        return amountDiff;
    }

    public BigDecimal getRealStock() {
        return realStock;
    }

    public int getType() {
        return type;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public void setAmountDiff(BigDecimal amountDiff) {
        this.amountDiff = amountDiff;
    }

    public void setRealStock(BigDecimal realStock) {
        this.realStock = realStock;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    String supplierName;
    String materialNo;
    String materialName;
    BigDecimal amountDiff;
    BigDecimal realStock;
    Timestamp timestamp;
    int state;
    int type;

    public static final int AMOUNT_DIFF_ENTRY_STATE=0;
    public static final int AMOUNT_DIFF_DELIVERY_STATE=1;
    public static final int AMOUNT_PRIME=2;
    public static final int AMOUNT_END=3;
}
