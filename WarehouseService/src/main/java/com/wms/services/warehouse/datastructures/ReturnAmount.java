package com.wms.services.warehouse.datastructures;

import java.math.BigDecimal;

public class ReturnAmount {
    public int getSupplyId() {
        return supplyId;
    }


    public void setSupplyId(int supplyId) {
        this.supplyId = supplyId;
    }


    private int supplyId=-1;

    public void setAmountQualified(BigDecimal amountQualified) {
        this.amountQualified = amountQualified;
    }

    public void setAmountUnqualified(BigDecimal amountUnqualified) {
        this.amountUnqualified = amountUnqualified;
    }

    private BigDecimal amountQualified;

    public BigDecimal getAmountQualified() {
        return amountQualified;
    }

    public BigDecimal getAmountUnqualified() {
        return amountUnqualified;
    }

    private BigDecimal amountUnqualified;

    public String getSupplierName() {
        return supplierName;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialProductLine() {
        return materialProductLine;
    }

    public void setMaterialProductLine(String materialProductLine) {
        this.materialProductLine = materialProductLine;
    }

    private String supplierName;
    private String materialNo;
    private String materialName;
    private String materialProductLine;
}
