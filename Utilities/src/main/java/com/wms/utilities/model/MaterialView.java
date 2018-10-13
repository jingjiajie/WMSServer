package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class MaterialView {
    private int id;
    private String name;
    private int warehouseId;
    private String no;
    private String warehouseName;
    private String productLine;
    private int enabled;
    private String serialNo;

    @Id
    @Basic
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "WarehouseID")
    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    @Basic
    @Column(name = "No")
    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    @Basic
    @Column(name = "WarehouseName")
    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    @Basic
    @Column(name = "ProductLine")
    public String getProductLine() {
        return productLine;
    }

    public void setProductLine(String productLine) {
        this.productLine = productLine;
    }

    @Basic
    @Column(name = "Enabled")
    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    @Basic
    @Column(name = "SerialNo")
    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaterialView that = (MaterialView) o;
        return id == that.id &&
                warehouseId == that.warehouseId &&
                enabled == that.enabled &&
                Objects.equals(name, that.name) &&
                Objects.equals(no, that.no) &&
                Objects.equals(warehouseName, that.warehouseName) &&
                Objects.equals(productLine, that.productLine) &&
                Objects.equals(serialNo, that.serialNo);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, warehouseId, no, warehouseName, productLine, enabled, serialNo);
    }
}
