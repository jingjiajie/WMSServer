package com.wms.utilities.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "WarehouseID", nullable = false)
    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    @Basic
    @Column(name = "SupplierID", nullable = false)
    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
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
    @Column(name = "Description", nullable = true, length = 64)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "State", nullable = false)
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Basic
    @Column(name = "DeliverOrderNoSRM", nullable = true, length = 64)
    public String getDeliverOrderNoSrm() {
        return deliverOrderNoSrm;
    }

    public void setDeliverOrderNoSrm(String deliverOrderNoSrm) {
        this.deliverOrderNoSrm = deliverOrderNoSrm;
    }

    @Basic
    @Column(name = "InboundDeliveryOrderNo", nullable = true, length = 64)
    public String getInboundDeliveryOrderNo() {
        return inboundDeliveryOrderNo;
    }

    public void setInboundDeliveryOrderNo(String inboundDeliveryOrderNo) {
        this.inboundDeliveryOrderNo = inboundDeliveryOrderNo;
    }

    @Basic
    @Column(name = "OutboundDeliveryOrderNo", nullable = true, length = 64)
    public String getOutboundDeliveryOrderNo() {
        return outboundDeliveryOrderNo;
    }

    public void setOutboundDeliveryOrderNo(String outboundDeliveryOrderNo) {
        this.outboundDeliveryOrderNo = outboundDeliveryOrderNo;
    }

    @Basic
    @Column(name = "PurchaseOrderNo", nullable = true, length = 64)
    public String getPurchaseOrderNo() {
        return purchaseOrderNo;
    }

    public void setPurchaseOrderNo(String purchaseOrderNo) {
        this.purchaseOrderNo = purchaseOrderNo;
    }

    @Basic
    @Column(name = "CreatePersonID", nullable = false)
    public int getCreatePersonId() {
        return createPersonId;
    }

    public void setCreatePersonId(int createPersonId) {
        this.createPersonId = createPersonId;
    }

    @Basic
    @Column(name = "CreateTime", nullable = false)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "LastUpdatePersonID", nullable = true)
    public Integer getLastUpdatePersonId() {
        return lastUpdatePersonId;
    }

    public void setLastUpdatePersonId(Integer lastUpdatePersonId) {
        this.lastUpdatePersonId = lastUpdatePersonId;
    }

    @Basic
    @Column(name = "LastUpdateTime", nullable = true)
    public Timestamp getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Timestamp lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WarehouseEntry that = (WarehouseEntry) o;
        return id == that.id &&
                warehouseId == that.warehouseId &&
                supplierId == that.supplierId &&
                createPersonId == that.createPersonId &&
                Objects.equals(no, that.no) &&
                Objects.equals(description, that.description) &&
                Objects.equals(state, that.state) &&
                Objects.equals(deliverOrderNoSrm, that.deliverOrderNoSrm) &&
                Objects.equals(inboundDeliveryOrderNo, that.inboundDeliveryOrderNo) &&
                Objects.equals(outboundDeliveryOrderNo, that.outboundDeliveryOrderNo) &&
                Objects.equals(purchaseOrderNo, that.purchaseOrderNo) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(lastUpdatePersonId, that.lastUpdatePersonId) &&
                Objects.equals(lastUpdateTime, that.lastUpdateTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, warehouseId, supplierId, no, description, state, deliverOrderNoSrm, inboundDeliveryOrderNo, outboundDeliveryOrderNo, purchaseOrderNo, createPersonId, createTime, lastUpdatePersonId, lastUpdateTime);
    }
}
