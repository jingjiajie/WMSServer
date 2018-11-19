package com.wms.services.warehouse.datastructures;

public class StorageLocationLess {
    int id;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNo() {
        return no;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    String no;
    int warehouseId;
}
