package com.wms.utilities.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class WarehouseEntry {
    private int id;
    private int warehouseId;
    private int supplierId;
    private String no;
    private String description;
    private int state;
    private String deliverOrderNoSrm;
    private String inboundDeliveryOrderNo;
    private String outboundDeliveryOrderNo;
    private String purchaseOrderNo;
    private int createPersonId;
    private Timestamp createTime;
    private Integer lastUpdatePersonId;
    private Timestamp lastUpdateTime;
    private Integer version;

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
    @Column(name = "WarehouseID")
    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    @Basic
    @Column(name = "SupplierID")
    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
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
    @Column(name = "Description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
    @Column(name = "DeliverOrderNoSRM")
    public String getDeliverOrderNoSrm() {
        return deliverOrderNoSrm;
    }

    public void setDeliverOrderNoSrm(String deliverOrderNoSrm) {
        this.deliverOrderNoSrm = deliverOrderNoSrm;
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
    @Column(name = "OutboundDeliveryOrderNo")
    public String getOutboundDeliveryOrderNo() {
        return outboundDeliveryOrderNo;
    }

    public void setOutboundDeliveryOrderNo(String outboundDeliveryOrderNo) {
        this.outboundDeliveryOrderNo = outboundDeliveryOrderNo;
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

        WarehouseEntry that = (WarehouseEntry) o;

        if (id != that.id) return false;
        if (warehouseId != that.warehouseId) return false;
        if (supplierId != that.supplierId) return false;
        if (state != that.state) return false;
        if (createPersonId != that.createPersonId) return false;
        if (no != null ? !no.equals(that.no) : that.no != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (deliverOrderNoSrm != null ? !deliverOrderNoSrm.equals(that.deliverOrderNoSrm) : that.deliverOrderNoSrm != null)
            return false;
        if (inboundDeliveryOrderNo != null ? !inboundDeliveryOrderNo.equals(that.inboundDeliveryOrderNo) : that.inboundDeliveryOrderNo != null)
            return false;
        if (outboundDeliveryOrderNo != null ? !outboundDeliveryOrderNo.equals(that.outboundDeliveryOrderNo) : that.outboundDeliveryOrderNo != null)
            return false;
        if (purchaseOrderNo != null ? !purchaseOrderNo.equals(that.purchaseOrderNo) : that.purchaseOrderNo != null)
            return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (lastUpdatePersonId != null ? !lastUpdatePersonId.equals(that.lastUpdatePersonId) : that.lastUpdatePersonId != null)
            return false;
        if (lastUpdateTime != null ? !lastUpdateTime.equals(that.lastUpdateTime) : that.lastUpdateTime != null)
            return false;
        if (version != null ? !version.equals(that.version) : that.version != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + warehouseId;
        result = 31 * result + supplierId;
        result = 31 * result + (no != null ? no.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + state;
        result = 31 * result + (deliverOrderNoSrm != null ? deliverOrderNoSrm.hashCode() : 0);
        result = 31 * result + (inboundDeliveryOrderNo != null ? inboundDeliveryOrderNo.hashCode() : 0);
        result = 31 * result + (outboundDeliveryOrderNo != null ? outboundDeliveryOrderNo.hashCode() : 0);
        result = 31 * result + (purchaseOrderNo != null ? purchaseOrderNo.hashCode() : 0);
        result = 31 * result + createPersonId;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (lastUpdatePersonId != null ? lastUpdatePersonId.hashCode() : 0);
        result = 31 * result + (lastUpdateTime != null ? lastUpdateTime.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }
}
