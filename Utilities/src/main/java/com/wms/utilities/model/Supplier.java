package com.wms.utilities.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
public class Supplier {
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
    private int enabled=1;

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
    @Column(name = "No", nullable = true, length = 64)
    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    @Basic
    @Column(name = "FullName", nullable = true, length = 64)
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Basic
    @Column(name = "Name", nullable = true, length = 64)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "EnterpriseCode", nullable = true, length = 64)
    public String getEnterpriseCode() {
        return enterpriseCode;
    }

    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode;
    }

    @Basic
    @Column(name = "ContractNo", nullable = true, length = 64)
    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    @Basic
    @Column(name = "ContractStartTime", nullable = true)
    public Timestamp getContractStartTime() {
        return contractStartTime;
    }

    public void setContractStartTime(Timestamp contractStartTime) {
        this.contractStartTime = contractStartTime;
    }

    @Basic
    @Column(name = "ContractEndTime", nullable = true)
    public Timestamp getContractEndTime() {
        return contractEndTime;
    }

    public void setContractEndTime(Timestamp contractEndTime) {
        this.contractEndTime = contractEndTime;
    }

    @Basic
    @Column(name = "InvoiceDelayMonth", nullable = true, precision = 3)
    public BigDecimal getInvoiceDelayMonth() {
        return invoiceDelayMonth;
    }

    public void setInvoiceDelayMonth(BigDecimal invoiceDelayMonth) {
        this.invoiceDelayMonth = invoiceDelayMonth;
    }

    @Basic
    @Column(name = "BalanceDelayMonth", nullable = true, precision = 3)
    public BigDecimal getBalanceDelayMonth() {
        return balanceDelayMonth;
    }

    public void setBalanceDelayMonth(BigDecimal balanceDelayMonth) {
        this.balanceDelayMonth = balanceDelayMonth;
    }

    @Basic
    @Column(name = "NetArea", nullable = true, precision = 3)
    public BigDecimal getNetArea() {
        return netArea;
    }

    public void setNetArea(BigDecimal netArea) {
        this.netArea = netArea;
    }

    @Basic
    @Column(name = "FixedStorageCost", nullable = true, precision = 3)
    public BigDecimal getFixedStorageCost() {
        return fixedStorageCost;
    }

    public void setFixedStorageCost(BigDecimal fixedStorageCost) {
        this.fixedStorageCost = fixedStorageCost;
    }

    @Basic
    @Column(name = "ContractStorageArea", nullable = true, precision = 3)
    public BigDecimal getContractStorageArea() {
        return contractStorageArea;
    }

    public void setContractStorageArea(BigDecimal contractStorageArea) {
        this.contractStorageArea = contractStorageArea;
    }

    @Basic
    @Column(name = "TaxpayerNumber", nullable = true, length = 64)
    public String getTaxpayerNumber() {
        return taxpayerNumber;
    }

    public void setTaxpayerNumber(String taxpayerNumber) {
        this.taxpayerNumber = taxpayerNumber;
    }

    @Basic
    @Column(name = "Address", nullable = true, length = 64)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "Tel", nullable = true, length = 64)
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Basic
    @Column(name = "BankName", nullable = true, length = 64)
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Basic
    @Column(name = "BankAccount", nullable = true, length = 64)
    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    @Basic
    @Column(name = "BankNo", nullable = true, length = 64)
    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    @Basic
    @Column(name = "ZipCode", nullable = true, length = 64)
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Basic
    @Column(name = "RecipientName", nullable = true, length = 64)
    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    @Basic
    @Column(name = "ContractState", nullable = true, length = 64)
    public String getContractState() {
        return contractState;
    }

    public void setContractState(String contractState) {
        this.contractState = contractState;
    }

    @Basic
    @Column(name = "IsHistory", nullable = false)
    public int getIsHistory() {
        return isHistory;
    }

    public void setIsHistory(int isHistory) {
        this.isHistory = isHistory;
    }

    @Basic
    @Column(name = "NewestSupplierID", nullable = true)
    public Integer getNewestSupplierId() {
        return newestSupplierId;
    }

    public void setNewestSupplierId(Integer newestSupplierId) {
        this.newestSupplierId = newestSupplierId;
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

    @Basic
    @Column(name = "Enabled", nullable = false)
    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Supplier supplier = (Supplier) o;

        if (id != supplier.id) return false;
        if (warehouseId != supplier.warehouseId) return false;
        if (isHistory != supplier.isHistory) return false;
        if (createPersonId != supplier.createPersonId) return false;
        if (enabled != supplier.enabled) return false;
        if (no != null ? !no.equals(supplier.no) : supplier.no != null) return false;
        if (fullName != null ? !fullName.equals(supplier.fullName) : supplier.fullName != null) return false;
        if (name != null ? !name.equals(supplier.name) : supplier.name != null) return false;
        if (enterpriseCode != null ? !enterpriseCode.equals(supplier.enterpriseCode) : supplier.enterpriseCode != null)
            return false;
        if (contractNo != null ? !contractNo.equals(supplier.contractNo) : supplier.contractNo != null) return false;
        if (contractStartTime != null ? !contractStartTime.equals(supplier.contractStartTime) : supplier.contractStartTime != null)
            return false;
        if (contractEndTime != null ? !contractEndTime.equals(supplier.contractEndTime) : supplier.contractEndTime != null)
            return false;
        if (invoiceDelayMonth != null ? !invoiceDelayMonth.equals(supplier.invoiceDelayMonth) : supplier.invoiceDelayMonth != null)
            return false;
        if (balanceDelayMonth != null ? !balanceDelayMonth.equals(supplier.balanceDelayMonth) : supplier.balanceDelayMonth != null)
            return false;
        if (netArea != null ? !netArea.equals(supplier.netArea) : supplier.netArea != null) return false;
        if (fixedStorageCost != null ? !fixedStorageCost.equals(supplier.fixedStorageCost) : supplier.fixedStorageCost != null)
            return false;
        if (contractStorageArea != null ? !contractStorageArea.equals(supplier.contractStorageArea) : supplier.contractStorageArea != null)
            return false;
        if (taxpayerNumber != null ? !taxpayerNumber.equals(supplier.taxpayerNumber) : supplier.taxpayerNumber != null)
            return false;
        if (address != null ? !address.equals(supplier.address) : supplier.address != null) return false;
        if (tel != null ? !tel.equals(supplier.tel) : supplier.tel != null) return false;
        if (bankName != null ? !bankName.equals(supplier.bankName) : supplier.bankName != null) return false;
        if (bankAccount != null ? !bankAccount.equals(supplier.bankAccount) : supplier.bankAccount != null)
            return false;
        if (bankNo != null ? !bankNo.equals(supplier.bankNo) : supplier.bankNo != null) return false;
        if (zipCode != null ? !zipCode.equals(supplier.zipCode) : supplier.zipCode != null) return false;
        if (recipientName != null ? !recipientName.equals(supplier.recipientName) : supplier.recipientName != null)
            return false;
        if (contractState != null ? !contractState.equals(supplier.contractState) : supplier.contractState != null)
            return false;
        if (newestSupplierId != null ? !newestSupplierId.equals(supplier.newestSupplierId) : supplier.newestSupplierId != null)
            return false;
        if (createTime != null ? !createTime.equals(supplier.createTime) : supplier.createTime != null) return false;
        if (lastUpdatePersonId != null ? !lastUpdatePersonId.equals(supplier.lastUpdatePersonId) : supplier.lastUpdatePersonId != null)
            return false;
        if (lastUpdateTime != null ? !lastUpdateTime.equals(supplier.lastUpdateTime) : supplier.lastUpdateTime != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + warehouseId;
        result = 31 * result + (no != null ? no.hashCode() : 0);
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (enterpriseCode != null ? enterpriseCode.hashCode() : 0);
        result = 31 * result + (contractNo != null ? contractNo.hashCode() : 0);
        result = 31 * result + (contractStartTime != null ? contractStartTime.hashCode() : 0);
        result = 31 * result + (contractEndTime != null ? contractEndTime.hashCode() : 0);
        result = 31 * result + (invoiceDelayMonth != null ? invoiceDelayMonth.hashCode() : 0);
        result = 31 * result + (balanceDelayMonth != null ? balanceDelayMonth.hashCode() : 0);
        result = 31 * result + (netArea != null ? netArea.hashCode() : 0);
        result = 31 * result + (fixedStorageCost != null ? fixedStorageCost.hashCode() : 0);
        result = 31 * result + (contractStorageArea != null ? contractStorageArea.hashCode() : 0);
        result = 31 * result + (taxpayerNumber != null ? taxpayerNumber.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (tel != null ? tel.hashCode() : 0);
        result = 31 * result + (bankName != null ? bankName.hashCode() : 0);
        result = 31 * result + (bankAccount != null ? bankAccount.hashCode() : 0);
        result = 31 * result + (bankNo != null ? bankNo.hashCode() : 0);
        result = 31 * result + (zipCode != null ? zipCode.hashCode() : 0);
        result = 31 * result + (recipientName != null ? recipientName.hashCode() : 0);
        result = 31 * result + (contractState != null ? contractState.hashCode() : 0);
        result = 31 * result + isHistory;
        result = 31 * result + (newestSupplierId != null ? newestSupplierId.hashCode() : 0);
        result = 31 * result + createPersonId;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (lastUpdatePersonId != null ? lastUpdatePersonId.hashCode() : 0);
        result = 31 * result + (lastUpdateTime != null ? lastUpdateTime.hashCode() : 0);
        result = 31 * result + enabled;
        return result;
    }
}
