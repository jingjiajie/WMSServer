package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class PriceView {
    private int id;
    private int supplyId;
    private BigDecimal areaUnitPrice;
    private BigDecimal logisticsThreshold1;
    private BigDecimal logisticsUnitPrice1;
    private BigDecimal logisticsThreshold2;
    private BigDecimal logisticsUnitPrice2;
    private BigDecimal logisticsThreshold3;
    private BigDecimal logisticsUnitPrice3;
    private Integer materialId;
    private String materialNo;
    private String materialName;
    private String materialProductLine;
    private Integer supplierId;
    private String supplierNo;
    private String supplierName;

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

    @Basic
    @Column(name = "MaterialID")
    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
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
    @Column(name = "MaterialName")
    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    @Basic
    @Column(name = "MaterialProductLine")
    public String getMaterialProductLine() {
        return materialProductLine;
    }

    public void setMaterialProductLine(String materialProductLine) {
        this.materialProductLine = materialProductLine;
    }

    @Basic
    @Column(name = "SupplierID")
    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        PriceView priceView = (PriceView) object;

        if (id != priceView.id) return false;
        if (supplyId != priceView.supplyId) return false;
        if (areaUnitPrice != null ? !areaUnitPrice.equals(priceView.areaUnitPrice) : priceView.areaUnitPrice != null)
            return false;
        if (logisticsThreshold1 != null ? !logisticsThreshold1.equals(priceView.logisticsThreshold1) : priceView.logisticsThreshold1 != null)
            return false;
        if (logisticsUnitPrice1 != null ? !logisticsUnitPrice1.equals(priceView.logisticsUnitPrice1) : priceView.logisticsUnitPrice1 != null)
            return false;
        if (logisticsThreshold2 != null ? !logisticsThreshold2.equals(priceView.logisticsThreshold2) : priceView.logisticsThreshold2 != null)
            return false;
        if (logisticsUnitPrice2 != null ? !logisticsUnitPrice2.equals(priceView.logisticsUnitPrice2) : priceView.logisticsUnitPrice2 != null)
            return false;
        if (logisticsThreshold3 != null ? !logisticsThreshold3.equals(priceView.logisticsThreshold3) : priceView.logisticsThreshold3 != null)
            return false;
        if (logisticsUnitPrice3 != null ? !logisticsUnitPrice3.equals(priceView.logisticsUnitPrice3) : priceView.logisticsUnitPrice3 != null)
            return false;
        if (materialId != null ? !materialId.equals(priceView.materialId) : priceView.materialId != null) return false;
        if (materialNo != null ? !materialNo.equals(priceView.materialNo) : priceView.materialNo != null) return false;
        if (materialName != null ? !materialName.equals(priceView.materialName) : priceView.materialName != null)
            return false;
        if (materialProductLine != null ? !materialProductLine.equals(priceView.materialProductLine) : priceView.materialProductLine != null)
            return false;
        if (supplierId != null ? !supplierId.equals(priceView.supplierId) : priceView.supplierId != null) return false;
        if (supplierNo != null ? !supplierNo.equals(priceView.supplierNo) : priceView.supplierNo != null) return false;
        if (supplierName != null ? !supplierName.equals(priceView.supplierName) : priceView.supplierName != null)
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
        result = 31 * result + (materialId != null ? materialId.hashCode() : 0);
        result = 31 * result + (materialNo != null ? materialNo.hashCode() : 0);
        result = 31 * result + (materialName != null ? materialName.hashCode() : 0);
        result = 31 * result + (materialProductLine != null ? materialProductLine.hashCode() : 0);
        result = 31 * result + (supplierId != null ? supplierId.hashCode() : 0);
        result = 31 * result + (supplierNo != null ? supplierNo.hashCode() : 0);
        result = 31 * result + (supplierName != null ? supplierName.hashCode() : 0);
        return result;
    }
}
