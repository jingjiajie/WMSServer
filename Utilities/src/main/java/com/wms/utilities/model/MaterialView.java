package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

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

    @Basic
    @Id
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

        if (id != that.id) return false;
        if (warehouseId != that.warehouseId) return false;
        if (enabled != that.enabled) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (no != null ? !no.equals(that.no) : that.no != null) return false;
        if (warehouseName != null ? !warehouseName.equals(that.warehouseName) : that.warehouseName != null)
            return false;
        if (productLine != null ? !productLine.equals(that.productLine) : that.productLine != null) return false;
        if (serialNo != null ? !serialNo.equals(that.serialNo) : that.serialNo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + warehouseId;
        result = 31 * result + (no != null ? no.hashCode() : 0);
        result = 31 * result + (warehouseName != null ? warehouseName.hashCode() : 0);
        result = 31 * result + (productLine != null ? productLine.hashCode() : 0);
        result = 31 * result + enabled;
        result = 31 * result + (serialNo != null ? serialNo.hashCode() : 0);
        return result;
    }
}
