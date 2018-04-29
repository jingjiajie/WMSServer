package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class StorageLocation {
    private int id;
    private int storageAreaId;
    private String no;
    private String name;
    private int enabled;

    @Id
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "StorageAreaID", nullable = false)
    public int getStorageAreaId() {
        return storageAreaId;
    }

    public void setStorageAreaId(int storageAreaId) {
        this.storageAreaId = storageAreaId;
    }

    @Basic
    @Column(name = "No", nullable = false, length = 64)
    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    @Basic
    @Column(name = "Name", nullable = true, length = 64)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "Enabled", nullable = false)
    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StorageLocation that = (StorageLocation) o;
        return id == that.id &&
                storageAreaId == that.storageAreaId &&
                enabled == that.enabled &&
                Objects.equals(no, that.no) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, storageAreaId, no, name, enabled);
    }
}
