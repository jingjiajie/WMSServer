package com.wms.services.warehouse.datastructures;

import java.sql.Timestamp;

public class StockTakingOrderItemAddSingle {
    public int getStockTakingOrderId() {
        return stockTakingOrderId;
    }

    public Integer getPersonId() {
        return personId;
    }

    public Timestamp getCheckTime() {
        return checkTime;
    }

    public void setStockTakingOrderId(int stockTakingOrderId) {
        this.stockTakingOrderId = stockTakingOrderId;
    }


    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public void setCheckTime(Timestamp checkTime) {
        this.checkTime = checkTime;
    }

    private int stockTakingOrderId;

    public Integer[] getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(Integer[] supplyId) {
        this.supplyId = supplyId;
    }

    private Integer[] supplyId;

    private Integer personId;

    private Timestamp checkTime=new Timestamp(System.currentTimeMillis());

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    private int mode=0;

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    private int warehouseId;

    public String getAddMode() {
        return addMode;
    }

    public void setAddMode(String addMode) {
        this.addMode = addMode;
    }

    private String addMode="single";
}
