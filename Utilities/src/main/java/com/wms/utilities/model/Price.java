package com.wms.utilities.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Price {
    private int id;
    private int supplyId;
    private BigDecimal areaUnitPrice;
    private BigDecimal logisticsThreshold1;
    private BigDecimal logisticsUnitPrice1;
    private BigDecimal logisticsThreshold2;
    private BigDecimal logisticsUnitPrice2;
    private BigDecimal logisticsThreshold3;
    private BigDecimal logisticsUnitPrice3;

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
    @Column(name = "SupplyID")
    public int getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(int supplyId) {
        this.supplyId = supplyId;
    }

    @Basic
    @Column(name = "AreaUnitPrice")
    public BigDecimal getAreaUnitPrice() {
        return areaUnitPrice;
    }

    public void setAreaUnitPrice(BigDecimal areaUnitPrice) {
        this.areaUnitPrice = areaUnitPrice;
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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Price price = (Price) object;

        if (id != price.id) return false;
        if (supplyId != price.supplyId) return false;
        if (areaUnitPrice != null ? !areaUnitPrice.equals(price.areaUnitPrice) : price.areaUnitPrice != null)
            return false;
        if (logisticsThreshold1 != null ? !logisticsThreshold1.equals(price.logisticsThreshold1) : price.logisticsThreshold1 != null)
            return false;
        if (logisticsUnitPrice1 != null ? !logisticsUnitPrice1.equals(price.logisticsUnitPrice1) : price.logisticsUnitPrice1 != null)
            return false;
        if (logisticsThreshold2 != null ? !logisticsThreshold2.equals(price.logisticsThreshold2) : price.logisticsThreshold2 != null)
            return false;
        if (logisticsUnitPrice2 != null ? !logisticsUnitPrice2.equals(price.logisticsUnitPrice2) : price.logisticsUnitPrice2 != null)
            return false;
        if (logisticsThreshold3 != null ? !logisticsThreshold3.equals(price.logisticsThreshold3) : price.logisticsThreshold3 != null)
            return false;
        if (logisticsUnitPrice3 != null ? !logisticsUnitPrice3.equals(price.logisticsUnitPrice3) : price.logisticsUnitPrice3 != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + supplyId;
        result = 31 * result + (areaUnitPrice != null ? areaUnitPrice.hashCode() : 0);
        result = 31 * result + (logisticsThreshold1 != null ? logisticsThreshold1.hashCode() : 0);
        result = 31 * result + (logisticsUnitPrice1 != null ? logisticsUnitPrice1.hashCode() : 0);
        result = 31 * result + (logisticsThreshold2 != null ? logisticsThreshold2.hashCode() : 0);
        result = 31 * result + (logisticsUnitPrice2 != null ? logisticsUnitPrice2.hashCode() : 0);
        result = 31 * result + (logisticsThreshold3 != null ? logisticsThreshold3.hashCode() : 0);
        result = 31 * result + (logisticsUnitPrice3 != null ? logisticsUnitPrice3.hashCode() : 0);
        return result;
    }
}
