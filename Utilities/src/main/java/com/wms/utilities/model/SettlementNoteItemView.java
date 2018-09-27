package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
public class SettlementNoteItemView {
    private int id;
    private int settlementNoteId;
    private int supplierId;
    private BigDecimal storageCharge;
    private BigDecimal logisticFee;
    private BigDecimal actualPayment;
    private String supplierName;
    private String supplierNo;
    private int state;
    private Timestamp createTime;

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
    @Column(name = "SettlementNoteID")
    public int getSettlementNoteId() {
        return settlementNoteId;
    }

    public void setSettlementNoteId(int settlementNoteId) {
        this.settlementNoteId = settlementNoteId;
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
    @Column(name = "StorageCharge")
    public BigDecimal getStorageCharge() {
        return storageCharge;
    }

    public void setStorageCharge(BigDecimal storageCharge) {
        this.storageCharge = storageCharge;
    }

    @Basic
    @Column(name = "LogisticFee")
    public BigDecimal getLogisticFee() {
        return logisticFee;
    }

    public void setLogisticFee(BigDecimal logisticFee) {
        this.logisticFee = logisticFee;
    }

    @Basic
    @Column(name = "ActualPayment")
    public BigDecimal getActualPayment() {
        return actualPayment;
    }

    public void setActualPayment(BigDecimal actualPayment) {
        this.actualPayment = actualPayment;
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
    @Column(name = "SupplierNo")
    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    @Basic
    @Column(name = "State")
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SettlementNoteItemView that = (SettlementNoteItemView) o;

        if (id != that.id) return false;
        if (settlementNoteId != that.settlementNoteId) return false;
        if (supplierId != that.supplierId) return false;
        if (storageCharge != null ? !storageCharge.equals(that.storageCharge) : that.storageCharge != null)
            return false;
        if (logisticFee != null ? !logisticFee.equals(that.logisticFee) : that.logisticFee != null) return false;
        if (actualPayment != null ? !actualPayment.equals(that.actualPayment) : that.actualPayment != null)
            return false;
        if (supplierName != null ? !supplierName.equals(that.supplierName) : that.supplierName != null) return false;
        if (supplierNo != null ? !supplierNo.equals(that.supplierNo) : that.supplierNo != null) return false;
        if (state != that.state) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + settlementNoteId;
        result = 31 * result + supplierId;
        result = 31 * result + (storageCharge != null ? storageCharge.hashCode() : 0);
        result = 31 * result + (logisticFee != null ? logisticFee.hashCode() : 0);
        result = 31 * result + (actualPayment != null ? actualPayment.hashCode() : 0);
        result = 31 * result + (supplierName != null ? supplierName.hashCode() : 0);
        result = 31 * result + (supplierNo != null ? supplierNo.hashCode() : 0);
        result = 31 * result + state;
        return result;
    }

    @Basic
    @Column(name = "CreateTime")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
