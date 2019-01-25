package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
public class PriceDetails {
    private int id;
    private int supplyId;
    private String supplierName;
    private BigDecimal deliveryAmount;
    private int summaryNoteItemId;
    private BigDecimal logisticsUnitPrice1;
    private BigDecimal logisticsThreshold2;
    private BigDecimal logisticsThreshold1;
    private BigDecimal logisticsUnitPrice2;
    private BigDecimal logisticsThreshold3;
    private BigDecimal logisticsUnitPrice3;
    private String materialName;
    private String materialNo;
    private Timestamp startTime;
    private Timestamp endTime;
    private BigDecimal singlePrice;
    private BigDecimal sumPrice;

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
    @Column(name = "SupplyID")
    public int getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(int supplyId) {
        this.supplyId = supplyId;
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
    @Column(name = "DeliveryAmount")
    public BigDecimal getDeliveryAmount() {
        return deliveryAmount;
    }

    public void setDeliveryAmount(BigDecimal deliveryAmount) {
        this.deliveryAmount = deliveryAmount;
    }

    @Basic
    @Column(name = "SummaryNoteItemID")
    public int getSummaryNoteItemId() {
        return summaryNoteItemId;
    }

    public void setSummaryNoteItemId(int summaryNoteItemId) {
        this.summaryNoteItemId = summaryNoteItemId;
    }

    @Basic
    @Column(name = "LogisticsUnitPrice1")
    public BigDecimal getLogisticsUnitPrice1() {
        return logisticsUnitPrice1;
    }

    public void setLogisticsUnitPrice1(BigDecimal logisticsUnitPrice1) {
        this.logisticsUnitPrice1 = logisticsUnitPrice1;
    }

    @Basic
    @Column(name = "LogisticsThreshold2")
    public BigDecimal getLogisticsThreshold2() {
        return logisticsThreshold2;
    }

    public void setLogisticsThreshold2(BigDecimal logisticsThreshold2) {
        this.logisticsThreshold2 = logisticsThreshold2;
    }

    @Basic
    @Column(name = "LogisticsThreshold1")
    public BigDecimal getLogisticsThreshold1() {
        return logisticsThreshold1;
    }

    public void setLogisticsThreshold1(BigDecimal logisticsThreshold1) {
        this.logisticsThreshold1 = logisticsThreshold1;
    }

    @Basic
    @Column(name = "LogisticsUnitPrice2")
    public BigDecimal getLogisticsUnitPrice2() {
        return logisticsUnitPrice2;
    }

    public void setLogisticsUnitPrice2(BigDecimal logisticsUnitPrice2) {
        this.logisticsUnitPrice2 = logisticsUnitPrice2;
    }

    @Basic
    @Column(name = "LogisticsThreshold3")
    public BigDecimal getLogisticsThreshold3() {
        return logisticsThreshold3;
    }

    public void setLogisticsThreshold3(BigDecimal logisticsThreshold3) {
        this.logisticsThreshold3 = logisticsThreshold3;
    }

    @Basic
    @Column(name = "LogisticsUnitPrice3")
    public BigDecimal getLogisticsUnitPrice3() {
        return logisticsUnitPrice3;
    }

    public void setLogisticsUnitPrice3(BigDecimal logisticsUnitPrice3) {
        this.logisticsUnitPrice3 = logisticsUnitPrice3;
    }

    @Basic
    @Column(name = "MaterialName")
    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    @Basic
    @Column(name = "MaterialNo")
    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    @Basic
    @Column(name = "StartTime")
    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    @Basic
    @Column(name = "EndTime")
    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @Basic
    @Column(name = "SinglePrice")
    public BigDecimal getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(BigDecimal singlePrice) {
        this.singlePrice = singlePrice;
    }

    @Basic
    @Column(name = "SumPrice")
    public BigDecimal getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(BigDecimal sumPrice) {
        this.sumPrice = sumPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PriceDetails that = (PriceDetails) o;

        if (id != that.id) return false;
        if (supplyId != that.supplyId) return false;
        if (summaryNoteItemId != that.summaryNoteItemId) return false;
        if (supplierName != null ? !supplierName.equals(that.supplierName) : that.supplierName != null) return false;
        if (deliveryAmount != null ? !deliveryAmount.equals(that.deliveryAmount) : that.deliveryAmount != null)
            return false;
        if (logisticsUnitPrice1 != null ? !logisticsUnitPrice1.equals(that.logisticsUnitPrice1) : that.logisticsUnitPrice1 != null)
            return false;
        if (logisticsThreshold2 != null ? !logisticsThreshold2.equals(that.logisticsThreshold2) : that.logisticsThreshold2 != null)
            return false;
        if (logisticsThreshold1 != null ? !logisticsThreshold1.equals(that.logisticsThreshold1) : that.logisticsThreshold1 != null)
            return false;
        if (logisticsUnitPrice2 != null ? !logisticsUnitPrice2.equals(that.logisticsUnitPrice2) : that.logisticsUnitPrice2 != null)
            return false;
        if (logisticsThreshold3 != null ? !logisticsThreshold3.equals(that.logisticsThreshold3) : that.logisticsThreshold3 != null)
            return false;
        if (logisticsUnitPrice3 != null ? !logisticsUnitPrice3.equals(that.logisticsUnitPrice3) : that.logisticsUnitPrice3 != null)
            return false;
        if (materialName != null ? !materialName.equals(that.materialName) : that.materialName != null) return false;
        if (materialNo != null ? !materialNo.equals(that.materialNo) : that.materialNo != null) return false;
        if (startTime != null ? !startTime.equals(that.startTime) : that.startTime != null) return false;
        if (endTime != null ? !endTime.equals(that.endTime) : that.endTime != null) return false;
        if (singlePrice != null ? !singlePrice.equals(that.singlePrice) : that.singlePrice != null) return false;
        if (sumPrice != null ? !sumPrice.equals(that.sumPrice) : that.sumPrice != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + supplyId;
        result = 31 * result + (supplierName != null ? supplierName.hashCode() : 0);
        result = 31 * result + (deliveryAmount != null ? deliveryAmount.hashCode() : 0);
        result = 31 * result + summaryNoteItemId;
        result = 31 * result + (logisticsUnitPrice1 != null ? logisticsUnitPrice1.hashCode() : 0);
        result = 31 * result + (logisticsThreshold2 != null ? logisticsThreshold2.hashCode() : 0);
        result = 31 * result + (logisticsThreshold1 != null ? logisticsThreshold1.hashCode() : 0);
        result = 31 * result + (logisticsUnitPrice2 != null ? logisticsUnitPrice2.hashCode() : 0);
        result = 31 * result + (logisticsThreshold3 != null ? logisticsThreshold3.hashCode() : 0);
        result = 31 * result + (logisticsUnitPrice3 != null ? logisticsUnitPrice3.hashCode() : 0);
        result = 31 * result + (materialName != null ? materialName.hashCode() : 0);
        result = 31 * result + (materialNo != null ? materialNo.hashCode() : 0);
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + (singlePrice != null ? singlePrice.hashCode() : 0);
        result = 31 * result + (sumPrice != null ? sumPrice.hashCode() : 0);
        return result;
    }
}
