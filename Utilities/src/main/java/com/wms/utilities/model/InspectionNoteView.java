package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class InspectionNoteView {
    private int id;
    private int warehouseEntryId;
    private int warehouseId;
    private String no;
    private int state;
    private String description;
    private Timestamp inspectionTime;
    private int createPersonId;
    private Timestamp createTime;
    private Integer lastUpdatePersonId;
    private Timestamp lastUpdateTime;
    private String warehouseEntryNo;
    private String supplierNo;
    private String supplierName;
    private String warehouseName;
    private String createPersonName;
    private String lastUpdatePersonName;
    private String inboundDeliveryOrderNo;
    private String purchaseOrderNo;
    private String sapNo;
    private Integer version;

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
    @Column(name = "WarehouseEntryID")
    public int getWarehouseEntryId() {
        return warehouseEntryId;
    }

    public void setWarehouseEntryId(int warehouseEntryId) {
        this.warehouseEntryId = warehouseEntryId;
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
    @Column(name = "State")
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Basic
    @Column(name = "Description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "InspectionTime")
    public Timestamp getInspectionTime() {
        return inspectionTime;
    }

    public void setInspectionTime(Timestamp inspectionTime) {
        this.inspectionTime = inspectionTime;
    }

    @Basic
    @Column(name = "CreatePersonID")
    public int getCreatePersonId() {
        return createPersonId;
    }

    public void setCreatePersonId(int createPersonId) {
        this.createPersonId = createPersonId;
    }

    @Basic
    @Column(name = "CreateTime")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "LastUpdatePersonID")
    public Integer getLastUpdatePersonId() {
        return lastUpdatePersonId;
    }

    public void setLastUpdatePersonId(Integer lastUpdatePersonId) {
        this.lastUpdatePersonId = lastUpdatePersonId;
    }

    @Basic
    @Column(name = "LastUpdateTime")
    public Timestamp getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Timestamp lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Basic
    @Column(name = "WarehouseEntryNo")
    public String getWarehouseEntryNo() {
        return warehouseEntryNo;
    }

    public void setWarehouseEntryNo(String warehouseEntryNo) {
        this.warehouseEntryNo = warehouseEntryNo;
    }

    @Basic
    @Column(name = "SupplierNo")
    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    @Basic
    @Column(name = "SupplierName")
    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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
    @Column(name = "CreatePersonName")
    public String getCreatePersonName() {
        return createPersonName;
    }

    public void setCreatePersonName(String createPersonName) {
        this.createPersonName = createPersonName;
    }

    @Basic
    @Column(name = "LastUpdatePersonName")
    public String getLastUpdatePersonName() {
        return lastUpdatePersonName;
    }

    public void setLastUpdatePersonName(String lastUpdatePersonName) {
        this.lastUpdatePersonName = lastUpdatePersonName;
    }

    @Basic
    @Column(name = "InboundDeliveryOrderNo")
    public String getInboundDeliveryOrderNo() {
        return inboundDeliveryOrderNo;
    }

    public void setInboundDeliveryOrderNo(String inboundDeliveryOrderNo) {
        this.inboundDeliveryOrderNo = inboundDeliveryOrderNo;
    }

    @Basic
    @Column(name = "PurchaseOrderNo")
    public String getPurchaseOrderNo() {
        return purchaseOrderNo;
    }

    public void setPurchaseOrderNo(String purchaseOrderNo) {
        this.purchaseOrderNo = purchaseOrderNo;
    }

    @Basic
    @Column(name = "SAPNo")
    public String getSapNo() {
        return sapNo;
    }

    public void setSapNo(String sapNo) {
        this.sapNo = sapNo;
    }

    @Basic
    @Column(name = "Version")
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InspectionNoteView that = (InspectionNoteView) o;

        if (id != that.id) return false;
        if (warehouseEntryId != that.warehouseEntryId) return false;
        if (warehouseId != that.warehouseId) return false;
        if (state != that.state) return false;
        if (createPersonId != that.createPersonId) return false;
        if (no != null ? !no.equals(that.no) : that.no != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (inspectionTime != null ? !inspectionTime.equals(that.inspectionTime) : that.inspectionTime != null)
            return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (lastUpdatePersonId != null ? !lastUpdatePersonId.equals(that.lastUpdatePersonId) : that.lastUpdatePersonId != null)
            return false;
        if (lastUpdateTime != null ? !lastUpdateTime.equals(that.lastUpdateTime) : that.lastUpdateTime != null)
            return false;
        if (warehouseEntryNo != null ? !warehouseEntryNo.equals(that.warehouseEntryNo) : that.warehouseEntryNo != null)
            return false;
        if (supplierNo != null ? !supplierNo.equals(that.supplierNo) : that.supplierNo != null) return false;
        if (supplierName != null ? !supplierName.equals(that.supplierName) : that.supplierName != null) return false;
        if (warehouseName != null ? !warehouseName.equals(that.warehouseName) : that.warehouseName != null)
            return false;
        if (createPersonName != null ? !createPersonName.equals(that.createPersonName) : that.createPersonName != null)
            return false;
        if (lastUpdatePersonName != null ? !lastUpdatePersonName.equals(that.lastUpdatePersonName) : that.lastUpdatePersonName != null)
            return false;
        if (inboundDeliveryOrderNo != null ? !inboundDeliveryOrderNo.equals(that.inboundDeliveryOrderNo) : that.inboundDeliveryOrderNo != null)
            return false;
        if (purchaseOrderNo != null ? !purchaseOrderNo.equals(that.purchaseOrderNo) : that.purchaseOrderNo != null)
            return false;
        if (sapNo != null ? !sapNo.equals(that.sapNo) : that.sapNo != null) return false;
        if (version != null ? !version.equals(that.version) : that.version != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + warehouseEntryId;
        result = 31 * result + warehouseId;
        result = 31 * result + (no != null ? no.hashCode() : 0);
        result = 31 * result + state;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (inspectionTime != null ? inspectionTime.hashCode() : 0);
        result = 31 * result + createPersonId;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (lastUpdatePersonId != null ? lastUpdatePersonId.hashCode() : 0);
        result = 31 * result + (lastUpdateTime != null ? lastUpdateTime.hashCode() : 0);
        result = 31 * result + (warehouseEntryNo != null ? warehouseEntryNo.hashCode() : 0);
        result = 31 * result + (supplierNo != null ? supplierNo.hashCode() : 0);
        result = 31 * result + (supplierName != null ? supplierName.hashCode() : 0);
        result = 31 * result + (warehouseName != null ? warehouseName.hashCode() : 0);
        result = 31 * result + (createPersonName != null ? createPersonName.hashCode() : 0);
        result = 31 * result + (lastUpdatePersonName != null ? lastUpdatePersonName.hashCode() : 0);
        result = 31 * result + (inboundDeliveryOrderNo != null ? inboundDeliveryOrderNo.hashCode() : 0);
        result = 31 * result + (purchaseOrderNo != null ? purchaseOrderNo.hashCode() : 0);
        result = 31 * result + (sapNo != null ? sapNo.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }
}
