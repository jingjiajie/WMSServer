package com.wms.utilities.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class StorageLocation {
    private int id;
    private int storageAreaId;
    private String no;
    private String name;
    private int enabled;
    private BigDecimal reservedArea;
    private BigDecimal piles;
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal lengthPadding;
    private BigDecimal widthPadding;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(name = "ReservedArea")
    public BigDecimal getReservedArea() {
        return reservedArea;
    }

    public void setReservedArea(BigDecimal reservedArea) {
        this.reservedArea = reservedArea;
    }

    @Basic
    @Column(name = "Piles")
    public BigDecimal getPiles() {
        return piles;
    }

    public void setPiles(BigDecimal piles) {
        this.piles = piles;
    }

    @Basic
    @Column(name = "Length")
    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    @Basic
    @Column(name = "Width")
    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    @Basic
    @Column(name = "LengthPadding")
    public BigDecimal getLengthPadding() {
        return lengthPadding;
    }

    public void setLengthPadding(BigDecimal lengthPadding) {
        this.lengthPadding = lengthPadding;
    }

    @Basic
    @Column(name = "WidthPadding")
    public BigDecimal getWidthPadding() {
        return widthPadding;
    }

    public void setWidthPadding(BigDecimal widthPadding) {
        this.widthPadding = widthPadding;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        StorageLocation that = (StorageLocation) object;

        if (id != that.id) return false;
        if (storageAreaId != that.storageAreaId) return false;
        if (enabled != that.enabled) return false;
        if (no != null ? !no.equals(that.no) : that.no != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (reservedArea != null ? !reservedArea.equals(that.reservedArea) : that.reservedArea != null) return false;
        if (piles != null ? !piles.equals(that.piles) : that.piles != null) return false;
        if (length != null ? !length.equals(that.length) : that.length != null) return false;
        if (width != null ? !width.equals(that.width) : that.width != null) return false;
        if (lengthPadding != null ? !lengthPadding.equals(that.lengthPadding) : that.lengthPadding != null)
            return false;
        if (widthPadding != null ? !widthPadding.equals(that.widthPadding) : that.widthPadding != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + storageAreaId;
        result = 31 * result + (no != null ? no.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + enabled;
        result = 31 * result + (reservedArea != null ? reservedArea.hashCode() : 0);
        result = 31 * result + (piles != null ? piles.hashCode() : 0);
        result = 31 * result + (length != null ? length.hashCode() : 0);
        result = 31 * result + (width != null ? width.hashCode() : 0);
        result = 31 * result + (lengthPadding != null ? lengthPadding.hashCode() : 0);
        result = 31 * result + (widthPadding != null ? widthPadding.hashCode() : 0);
        return result;
    }
}
