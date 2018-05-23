package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
@Entity
public class StorageLocationView {
    private int id;
    private int storageAreaId;
    private String no;
    private String name;
    private int enabled;
    private int warehouseId;
    private String storageAreaNo;
    private String storageAreaName;

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
    @Column(name = "StorageAreaID")
    public int getStorageAreaId() {
        return storageAreaId;
    }

    public void setStorageAreaId(int storageAreaId) {
        this.storageAreaId = storageAreaId;
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
    @Column(name = "Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    @Column(name = "WarehouseID")
    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    @Basic
    @Column(name = "StorageAreaNo")
    public String getStorageAreaNo() {
        return storageAreaNo;
    }

    public void setStorageAreaNo(String storageAreaNo) {
        this.storageAreaNo = storageAreaNo;
    }

    @Basic
    @Column(name = "StorageAreaName")
    public String getStorageAreaName() {
        return storageAreaName;
    }

    public void setStorageAreaName(String storageAreaName) {
        this.storageAreaName = storageAreaName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StorageLocationView that = (StorageLocationView) o;

        if (id != that.id) return false;
        if (storageAreaId != that.storageAreaId) return false;
        if (enabled != that.enabled) return false;
        if (warehouseId != that.warehouseId) return false;
        if (no != null ? !no.equals(that.no) : that.no != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (storageAreaNo != null ? !storageAreaNo.equals(that.storageAreaNo) : that.storageAreaNo != null)
            return false;
        if (storageAreaName != null ? !storageAreaName.equals(that.storageAreaName) : that.storageAreaName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + storageAreaId;
        result = 31 * result + (no != null ? no.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + enabled;
        result = 31 * result + warehouseId;
        result = 31 * result + (storageAreaNo != null ? storageAreaNo.hashCode() : 0);
        result = 31 * result + (storageAreaName != null ? storageAreaName.hashCode() : 0);
        return result;
    }
}
