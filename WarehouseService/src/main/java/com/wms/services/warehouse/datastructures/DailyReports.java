package com.wms.services.warehouse.datastructures;

import java.math.BigDecimal;
import java.sql.Timestamp;

//0入库 1出库
public class DailyReports {
    public String getSupplierName() {
        return supplierName;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public String getMaterialName() {
        return materialName;
    }

    public BigDecimal getAmountDiff() {
        return amountDiff;
    }

    public int getType() {
        return type;
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

    public void setAmountDiff(BigDecimal amountDiff) {
        this.amountDiff = amountDiff;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(int supplyId) {
        this.supplyId = supplyId;
    }

    public String getMaterialProductLine() {
        return materialProductLine;
    }

    public void setMaterialProductLine(String materialProductLine) {
        this.materialProductLine = materialProductLine;
    }

    public String getDestination() {
        return destination;
    }

    public String getEntryNo() {
        return entryNo;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setEntryNo(String entryNo) {
        this.entryNo = entryNo;
    }

    public BigDecimal getReturnAmountQualified() {
        return returnAmountQualified;
    }

    public void setReturnAmountQualified(BigDecimal returnAmountQualified) {
        this.returnAmountQualified = returnAmountQualified;
    }

    public BigDecimal getReturnAmountUnqualified() {
        return returnAmountUnqualified;
    }

    public void setReturnAmountUnqualified(BigDecimal returnAmountUnqualified) {
        this.returnAmountUnqualified = returnAmountUnqualified;
    }

    public BigDecimal getReturnToSupplierQualified() {
        return returnToSupplierQualified;
    }

    public BigDecimal getReturnToSupplierUnqualified() {
        return returnToSupplierUnqualified;
    }

    public void setReturnToSupplierQualified(BigDecimal returnToSupplierQualified) {
        this.returnToSupplierQualified = returnToSupplierQualified;
    }

    public void setReturnToSupplierUnqualified(BigDecimal returnToSupplierUnqualified) {
        this.returnToSupplierUnqualified = returnToSupplierUnqualified;
    }

    public BigDecimal getRealStockQualified() {
        return realStockQualified;
    }

    public BigDecimal getRealStockUnqualified() {
        return realStockUnqualified;
    }

    public BigDecimal getRealStockWaitingForInspection() {
        return realStockWaitingForInspection;
    }

    public void setRealStockQualified(BigDecimal realStockQualified) {
        this.realStockQualified = realStockQualified;
    }

    public void setRealStockUnqualified(BigDecimal realStockUnqualified) {
        this.realStockUnqualified = realStockUnqualified;
    }

    public void setRealStockWaitingForInspection(BigDecimal realStockWaitingForInspection) {
        this.realStockWaitingForInspection = realStockWaitingForInspection;
    }

    public void setEntryAmountQua(BigDecimal entryAmountQua) {
        this.entryAmountQua = entryAmountQua;
    }

    public void setEntryAmountUnq(BigDecimal entryAmountUnq) {
        this.entryAmountUnq = entryAmountUnq;
    }

    public BigDecimal getEntryAmountWait() {
        return entryAmountWait;
    }

    public BigDecimal getEntryAmountQua() {
        return entryAmountQua;
    }

    public BigDecimal getEntryAmountUnq() {
        return entryAmountUnq;
    }

    public void setEntryAmountWait(BigDecimal entryAmountWait) {
        this.entryAmountWait = entryAmountWait;
    }

    private int supplyId;
    private String supplierName;
    private String materialNo;
    private String materialName;
    private String materialProductLine;
    //正品出库数量
    private BigDecimal amountDiff;
    private BigDecimal realStockQualified;
    private BigDecimal realStockUnqualified;
    private BigDecimal realStockWaitingForInspection;
    private Timestamp timestamp;
    private int state;
    private int type;
    private String destination;
    private String entryNo;
    //动力退中都的合格品和不合格品
    private BigDecimal returnAmountQualified=BigDecimal.ZERO;
    private BigDecimal returnAmountUnqualified=BigDecimal.ZERO;
    //中都退厂家
    private BigDecimal returnToSupplierQualified=BigDecimal.ZERO;
    private BigDecimal returnToSupplierUnqualified=BigDecimal.ZERO;
    private BigDecimal entryAmountQua=BigDecimal.ZERO;
    private BigDecimal entryAmountWait=BigDecimal.ZERO;
    private BigDecimal entryAmountUnq=BigDecimal.ZERO;


    public static final int AMOUNT_DIFF_ENTRY_STATE=1;
    public static final int AMOUNT_DIFF_DELIVERY_STATE=2;
    public static final int TOTAl=0;
    //动力退中都状态
    public static final int RETURN=3;
    //中都退厂家
    public static final int RETURN_TO_SUPPLIER=4;
    public static final int AMOUNT_END=5;
}
