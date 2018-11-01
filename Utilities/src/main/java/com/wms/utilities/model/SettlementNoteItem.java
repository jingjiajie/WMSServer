package com.wms.utilities.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
public class SettlementNoteItem {
    private int id;
    private int settlementNoteId;
    private int supplierId;
    private BigDecimal storageCharge;
    private BigDecimal logisticFee;
    private BigDecimal actualPayment;
    private int state;
    private Timestamp createTime;

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
    @Column(name = "State")
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Basic
    @Column(name = "CreateTime")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        SettlementNoteItem that = (SettlementNoteItem) object;

        if (id != that.id) return false;
        if (settlementNoteId != that.settlementNoteId) return false;
        if (supplierId != that.supplierId) return false;
        if (state != that.state) return false;
        if (storageCharge != null ? !storageCharge.equals(that.storageCharge) : that.storageCharge != null)
            return false;
        if (logisticFee != null ? !logisticFee.equals(that.logisticFee) : that.logisticFee != null) return false;
        if (actualPayment != null ? !actualPayment.equals(that.actualPayment) : that.actualPayment != null)
            return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;

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
        result = 31 * result + state;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        return result;
    }
}
