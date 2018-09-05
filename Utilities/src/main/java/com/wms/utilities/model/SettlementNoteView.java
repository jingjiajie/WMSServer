package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class SettlementNoteView {
    private int id;
    private String no;
    private int summaryNoteId;
    private Integer state;
    private Integer accountTitleIncomeId;
    private Integer accountTitleReceivableId;
    private Integer accountTitlePropertyId;
    private Timestamp createTime;
    private Integer createPersonId;
    private String accountTitleIncomeNo;
    private String accountTitleIncomeName;
    private String accountTitleReceivableNo;
    private String accountTitleReceivableName;
    private String accountTitlePropertyNo;
    private String accountTitlePropertyName;
    private String createPersonName;

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
    @Column(name = "No")
    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    @Basic
    @Column(name = "SummaryNoteID")
    public int getSummaryNoteId() {
        return summaryNoteId;
    }

    public void setSummaryNoteId(int summaryNoteId) {
        this.summaryNoteId = summaryNoteId;
    }

    @Basic
    @Column(name = "State")
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Basic
    @Column(name = "AccountTitleIncomeID")
    public Integer getAccountTitleIncomeId() {
        return accountTitleIncomeId;
    }

    public void setAccountTitleIncomeId(Integer accountTitleIncomeId) {
        this.accountTitleIncomeId = accountTitleIncomeId;
    }

    @Basic
    @Column(name = "AccountTitleReceivableID")
    public Integer getAccountTitleReceivableId() {
        return accountTitleReceivableId;
    }

    public void setAccountTitleReceivableId(Integer accountTitleReceivableId) {
        this.accountTitleReceivableId = accountTitleReceivableId;
    }

    @Basic
    @Column(name = "AccountTitlePropertyID")
    public Integer getAccountTitlePropertyId() {
        return accountTitlePropertyId;
    }

    public void setAccountTitlePropertyId(Integer accountTitlePropertyId) {
        this.accountTitlePropertyId = accountTitlePropertyId;
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
    @Column(name = "CreatePersonID")
    public Integer getCreatePersonId() {
        return createPersonId;
    }

    public void setCreatePersonId(Integer createPersonId) {
        this.createPersonId = createPersonId;
    }

    @Basic
    @Column(name = "AccountTitleIncomeNo")
    public String getAccountTitleIncomeNo() {
        return accountTitleIncomeNo;
    }

    public void setAccountTitleIncomeNo(String accountTitleIncomeNo) {
        this.accountTitleIncomeNo = accountTitleIncomeNo;
    }

    @Basic
    @Column(name = "AccountTitleIncomeName")
    public String getAccountTitleIncomeName() {
        return accountTitleIncomeName;
    }

    public void setAccountTitleIncomeName(String accountTitleIncomeName) {
        this.accountTitleIncomeName = accountTitleIncomeName;
    }

    @Basic
    @Column(name = "AccountTitleReceivableNo")
    public String getAccountTitleReceivableNo() {
        return accountTitleReceivableNo;
    }

    public void setAccountTitleReceivableNo(String accountTitleReceivableNo) {
        this.accountTitleReceivableNo = accountTitleReceivableNo;
    }

    @Basic
    @Column(name = "AccountTitleReceivableName")
    public String getAccountTitleReceivableName() {
        return accountTitleReceivableName;
    }

    public void setAccountTitleReceivableName(String accountTitleReceivableName) {
        this.accountTitleReceivableName = accountTitleReceivableName;
    }

    @Basic
    @Column(name = "AccountTitlePropertyNo")
    public String getAccountTitlePropertyNo() {
        return accountTitlePropertyNo;
    }

    public void setAccountTitlePropertyNo(String accountTitlePropertyNo) {
        this.accountTitlePropertyNo = accountTitlePropertyNo;
    }

    @Basic
    @Column(name = "AccountTitlePropertyName")
    public String getAccountTitlePropertyName() {
        return accountTitlePropertyName;
    }

    public void setAccountTitlePropertyName(String accountTitlePropertyName) {
        this.accountTitlePropertyName = accountTitlePropertyName;
    }

    @Basic
    @Column(name = "CreatePersonName")
    public String getCreatePersonName() {
        return createPersonName;
    }

    public void setCreatePersonName(String createPersonName) {
        this.createPersonName = createPersonName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SettlementNoteView that = (SettlementNoteView) o;

        if (id != that.id) return false;
        if (summaryNoteId != that.summaryNoteId) return false;
        if (no != null ? !no.equals(that.no) : that.no != null) return false;
        if (state != null ? !state.equals(that.state) : that.state != null) return false;
        if (accountTitleIncomeId != null ? !accountTitleIncomeId.equals(that.accountTitleIncomeId) : that.accountTitleIncomeId != null)
            return false;
        if (accountTitleReceivableId != null ? !accountTitleReceivableId.equals(that.accountTitleReceivableId) : that.accountTitleReceivableId != null)
            return false;
        if (accountTitlePropertyId != null ? !accountTitlePropertyId.equals(that.accountTitlePropertyId) : that.accountTitlePropertyId != null)
            return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (createPersonId != null ? !createPersonId.equals(that.createPersonId) : that.createPersonId != null)
            return false;
        if (accountTitleIncomeNo != null ? !accountTitleIncomeNo.equals(that.accountTitleIncomeNo) : that.accountTitleIncomeNo != null)
            return false;
        if (accountTitleIncomeName != null ? !accountTitleIncomeName.equals(that.accountTitleIncomeName) : that.accountTitleIncomeName != null)
            return false;
        if (accountTitleReceivableNo != null ? !accountTitleReceivableNo.equals(that.accountTitleReceivableNo) : that.accountTitleReceivableNo != null)
            return false;
        if (accountTitleReceivableName != null ? !accountTitleReceivableName.equals(that.accountTitleReceivableName) : that.accountTitleReceivableName != null)
            return false;
        if (accountTitlePropertyNo != null ? !accountTitlePropertyNo.equals(that.accountTitlePropertyNo) : that.accountTitlePropertyNo != null)
            return false;
        if (accountTitlePropertyName != null ? !accountTitlePropertyName.equals(that.accountTitlePropertyName) : that.accountTitlePropertyName != null)
            return false;
        if (createPersonName != null ? !createPersonName.equals(that.createPersonName) : that.createPersonName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (no != null ? no.hashCode() : 0);
        result = 31 * result + summaryNoteId;
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (accountTitleIncomeId != null ? accountTitleIncomeId.hashCode() : 0);
        result = 31 * result + (accountTitleReceivableId != null ? accountTitleReceivableId.hashCode() : 0);
        result = 31 * result + (accountTitlePropertyId != null ? accountTitlePropertyId.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (createPersonId != null ? createPersonId.hashCode() : 0);
        result = 31 * result + (accountTitleIncomeNo != null ? accountTitleIncomeNo.hashCode() : 0);
        result = 31 * result + (accountTitleIncomeName != null ? accountTitleIncomeName.hashCode() : 0);
        result = 31 * result + (accountTitleReceivableNo != null ? accountTitleReceivableNo.hashCode() : 0);
        result = 31 * result + (accountTitleReceivableName != null ? accountTitleReceivableName.hashCode() : 0);
        result = 31 * result + (accountTitlePropertyNo != null ? accountTitlePropertyNo.hashCode() : 0);
        result = 31 * result + (accountTitlePropertyName != null ? accountTitlePropertyName.hashCode() : 0);
        result = 31 * result + (createPersonName != null ? createPersonName.hashCode() : 0);
        return result;
    }
}
