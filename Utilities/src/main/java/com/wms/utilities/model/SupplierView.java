package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
public class SupplierView {
    private int id;
    private String warehouseName;
    private String createPersonName;
    private String lastUpdatePersonName;
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
    private BigDecimal basicArea;
    private BigDecimal fixedStorageSingleCost;
    private String realArea;
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
    private int enabled;
    private String serialNo;
    private String supplierPassward;

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
    @Column(name = "BasicArea")
    public BigDecimal getBasicArea() {
        return basicArea;
    }

    public void setBasicArea(BigDecimal basicArea) {
        this.basicArea = basicArea;
    }

    @Basic
    @Column(name = "FixedStorageSingleCost")
    public BigDecimal getFixedStorageSingleCost() {
        return fixedStorageSingleCost;
    }

    public void setFixedStorageSingleCost(BigDecimal fixedStorageSingleCost) {
        this.fixedStorageSingleCost = fixedStorageSingleCost;
    }

    @Basic
    @Column(name = "RealArea")
    public String getRealArea() {
        return realArea;
    }

    public void setRealArea(String realArea) {
        this.realArea = realArea;
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

    @Basic
    @Column(name = "SupplierPassward")
    public String getSupplierPassward() {
        return supplierPassward;
    }

    public void setSupplierPassward(String supplierPassward) {
        this.supplierPassward = supplierPassward;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        SupplierView that = (SupplierView) object;

        if (id != that.id) return false;
        if (warehouseId != that.warehouseId) return false;
        if (isHistory != that.isHistory) return false;
        if (createPersonId != that.createPersonId) return false;
        if (enabled != that.enabled) return false;
        if (warehouseName != null ? !warehouseName.equals(that.warehouseName) : that.warehouseName != null)
            return false;
        if (createPersonName != null ? !createPersonName.equals(that.createPersonName) : that.createPersonName != null)
            return false;
        if (lastUpdatePersonName != null ? !lastUpdatePersonName.equals(that.lastUpdatePersonName) : that.lastUpdatePersonName != null)
            return false;
        if (no != null ? !no.equals(that.no) : that.no != null) return false;
        if (fullName != null ? !fullName.equals(that.fullName) : that.fullName != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (enterpriseCode != null ? !enterpriseCode.equals(that.enterpriseCode) : that.enterpriseCode != null)
            return false;
        if (contractNo != null ? !contractNo.equals(that.contractNo) : that.contractNo != null) return false;
        if (contractStartTime != null ? !contractStartTime.equals(that.contractStartTime) : that.contractStartTime != null)
            return false;
        if (contractEndTime != null ? !contractEndTime.equals(that.contractEndTime) : that.contractEndTime != null)
            return false;
        if (invoiceDelayMonth != null ? !invoiceDelayMonth.equals(that.invoiceDelayMonth) : that.invoiceDelayMonth != null)
            return false;
        if (balanceDelayMonth != null ? !balanceDelayMonth.equals(that.balanceDelayMonth) : that.balanceDelayMonth != null)
            return false;
        if (basicArea != null ? !basicArea.equals(that.basicArea) : that.basicArea != null) return false;
        if (fixedStorageSingleCost != null ? !fixedStorageSingleCost.equals(that.fixedStorageSingleCost) : that.fixedStorageSingleCost != null)
            return false;
        if (realArea != null ? !realArea.equals(that.realArea) : that.realArea != null) return false;
        if (taxpayerNumber != null ? !taxpayerNumber.equals(that.taxpayerNumber) : that.taxpayerNumber != null)
            return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (tel != null ? !tel.equals(that.tel) : that.tel != null) return false;
        if (bankName != null ? !bankName.equals(that.bankName) : that.bankName != null) return false;
        if (bankAccount != null ? !bankAccount.equals(that.bankAccount) : that.bankAccount != null) return false;
        if (bankNo != null ? !bankNo.equals(that.bankNo) : that.bankNo != null) return false;
        if (zipCode != null ? !zipCode.equals(that.zipCode) : that.zipCode != null) return false;
        if (recipientName != null ? !recipientName.equals(that.recipientName) : that.recipientName != null)
            return false;
        if (contractState != null ? !contractState.equals(that.contractState) : that.contractState != null)
            return false;
        if (newestSupplierId != null ? !newestSupplierId.equals(that.newestSupplierId) : that.newestSupplierId != null)
            return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (lastUpdatePersonId != null ? !lastUpdatePersonId.equals(that.lastUpdatePersonId) : that.lastUpdatePersonId != null)
            return false;
        if (lastUpdateTime != null ? !lastUpdateTime.equals(that.lastUpdateTime) : that.lastUpdateTime != null)
            return false;
        if (serialNo != null ? !serialNo.equals(that.serialNo) : that.serialNo != null) return false;
        if (supplierPassward != null ? !supplierPassward.equals(that.supplierPassward) : that.supplierPassward != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (warehouseName != null ? warehouseName.hashCode() : 0);
        result = 31 * result + (createPersonName != null ? createPersonName.hashCode() : 0);
        result = 31 * result + (lastUpdatePersonName != null ? lastUpdatePersonName.hashCode() : 0);
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
        result = 31 * result + (basicArea != null ? basicArea.hashCode() : 0);
        result = 31 * result + (fixedStorageSingleCost != null ? fixedStorageSingleCost.hashCode() : 0);
        result = 31 * result + (realArea != null ? realArea.hashCode() : 0);
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
        result = 31 * result + (serialNo != null ? serialNo.hashCode() : 0);
        result = 31 * result + (supplierPassward != null ? supplierPassward.hashCode() : 0);
        return result;
    }
}
