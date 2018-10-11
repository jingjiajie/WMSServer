package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class SupplierView {
    private int id;
    private int warehouseId;
    private String no;
    private String fullName;
    private String name;
    private String enterpriseCode;
    private String contractNo;
    private Timestamp contractStartTime;
    private Timestamp contractEndTime;
    private BigDecimal invoiceDelayMonth;
    private BigDecimal balanceDelayMonth;
    private BigDecimal netArea;
    private BigDecimal fixedStorageCost;
    private BigDecimal contractStorageArea;
    private String taxpayerNumber;
    private String address;
    private String tel;
    private String bankName;
    private String bankAccount;
    private String bankNo;
    private String zipCode;
    private String recipientName;
    private String contractState;
    private int isHistory;
    private Integer newestSupplierId;
    private int createPersonId;
    private Timestamp createTime;
    private Integer lastUpdatePersonId;
    private Timestamp lastUpdateTime;
    private String warehouseName;
    private String createPersonName;
    private String lastUpdatePersonName;
    private int enabled;
    private String serialNo;

    @Id
    @Basic
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
    @Column(name = "No")
    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    @Basic
    @Column(name = "FullName")
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Basic
    @Column(name = "Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "EnterpriseCode")
    public String getEnterpriseCode() {
        return enterpriseCode;
    }

    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode;
    }

    @Basic
    @Column(name = "ContractNo")
    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    @Basic
    @Column(name = "ContractStartTime")
    public Timestamp getContractStartTime() {
        return contractStartTime;
    }

    public void setContractStartTime(Timestamp contractStartTime) {
        this.contractStartTime = contractStartTime;
    }

    @Basic
    @Column(name = "ContractEndTime")
    public Timestamp getContractEndTime() {
        return contractEndTime;
    }

    public void setContractEndTime(Timestamp contractEndTime) {
        this.contractEndTime = contractEndTime;
    }

    @Basic
    @Column(name = "InvoiceDelayMonth")
    public BigDecimal getInvoiceDelayMonth() {
        return invoiceDelayMonth;
    }

    public void setInvoiceDelayMonth(BigDecimal invoiceDelayMonth) {
        this.invoiceDelayMonth = invoiceDelayMonth;
    }

    @Basic
    @Column(name = "BalanceDelayMonth")
    public BigDecimal getBalanceDelayMonth() {
        return balanceDelayMonth;
    }

    public void setBalanceDelayMonth(BigDecimal balanceDelayMonth) {
        this.balanceDelayMonth = balanceDelayMonth;
    }

    @Basic
    @Column(name = "NetArea")
    public BigDecimal getNetArea() {
        return netArea;
    }

    public void setNetArea(BigDecimal netArea) {
        this.netArea = netArea;
    }

    @Basic
    @Column(name = "FixedStorageCost")
    public BigDecimal getFixedStorageCost() {
        return fixedStorageCost;
    }

    public void setFixedStorageCost(BigDecimal fixedStorageCost) {
        this.fixedStorageCost = fixedStorageCost;
    }

    @Basic
    @Column(name = "ContractStorageArea")
    public BigDecimal getContractStorageArea() {
        return contractStorageArea;
    }

    public void setContractStorageArea(BigDecimal contractStorageArea) {
        this.contractStorageArea = contractStorageArea;
    }

    @Basic
    @Column(name = "TaxpayerNumber")
    public String getTaxpayerNumber() {
        return taxpayerNumber;
    }

    public void setTaxpayerNumber(String taxpayerNumber) {
        this.taxpayerNumber = taxpayerNumber;
    }

    @Basic
    @Column(name = "Address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "Tel")
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Basic
    @Column(name = "BankName")
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Basic
    @Column(name = "BankAccount")
    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    @Basic
    @Column(name = "BankNo")
    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    @Basic
    @Column(name = "ZipCode")
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Basic
    @Column(name = "RecipientName")
    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    @Basic
    @Column(name = "ContractState")
    public String getContractState() {
        return contractState;
    }

    public void setContractState(String contractState) {
        this.contractState = contractState;
    }

    @Basic
    @Column(name = "IsHistory")
    public int getIsHistory() {
        return isHistory;
    }

    public void setIsHistory(int isHistory) {
        this.isHistory = isHistory;
    }

    @Basic
    @Column(name = "NewestSupplierID")
    public Integer getNewestSupplierId() {
        return newestSupplierId;
    }

    public void setNewestSupplierId(Integer newestSupplierId) {
        this.newestSupplierId = newestSupplierId;
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
    @Column(name = "Enabled")
    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    @Basic
    @Column(name = "SerialNo")
    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SupplierView that = (SupplierView) o;
        return id == that.id &&
                warehouseId == that.warehouseId &&
                isHistory == that.isHistory &&
                createPersonId == that.createPersonId &&
                enabled == that.enabled &&
                Objects.equals(no, that.no) &&
                Objects.equals(fullName, that.fullName) &&
                Objects.equals(name, that.name) &&
                Objects.equals(enterpriseCode, that.enterpriseCode) &&
                Objects.equals(contractNo, that.contractNo) &&
                Objects.equals(contractStartTime, that.contractStartTime) &&
                Objects.equals(contractEndTime, that.contractEndTime) &&
                Objects.equals(invoiceDelayMonth, that.invoiceDelayMonth) &&
                Objects.equals(balanceDelayMonth, that.balanceDelayMonth) &&
                Objects.equals(netArea, that.netArea) &&
                Objects.equals(fixedStorageCost, that.fixedStorageCost) &&
                Objects.equals(contractStorageArea, that.contractStorageArea) &&
                Objects.equals(taxpayerNumber, that.taxpayerNumber) &&
                Objects.equals(address, that.address) &&
                Objects.equals(tel, that.tel) &&
                Objects.equals(bankName, that.bankName) &&
                Objects.equals(bankAccount, that.bankAccount) &&
                Objects.equals(bankNo, that.bankNo) &&
                Objects.equals(zipCode, that.zipCode) &&
                Objects.equals(recipientName, that.recipientName) &&
                Objects.equals(contractState, that.contractState) &&
                Objects.equals(newestSupplierId, that.newestSupplierId) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(lastUpdatePersonId, that.lastUpdatePersonId) &&
                Objects.equals(lastUpdateTime, that.lastUpdateTime) &&
                Objects.equals(warehouseName, that.warehouseName) &&
                Objects.equals(createPersonName, that.createPersonName) &&
                Objects.equals(lastUpdatePersonName, that.lastUpdatePersonName) &&
                Objects.equals(serialNo, that.serialNo);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, warehouseId, no, fullName, name, enterpriseCode, contractNo, contractStartTime, contractEndTime, invoiceDelayMonth, balanceDelayMonth, netArea, fixedStorageCost, contractStorageArea, taxpayerNumber, address, tel, bankName, bankAccount, bankNo, zipCode, recipientName, contractState, isHistory, newestSupplierId, createPersonId, createTime, lastUpdatePersonId, lastUpdateTime, warehouseName, createPersonName, lastUpdatePersonName, enabled, serialNo);
    }
}
