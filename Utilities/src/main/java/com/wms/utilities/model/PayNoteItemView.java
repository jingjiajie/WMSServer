package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class PayNoteItemView {
    private int id;
    private int payNoteId;
    private int personId;
    private BigDecimal preTaxAmount;
    private BigDecimal taxAmount;
    private BigDecimal afterTaxAmount;
    private int state;
    private BigDecimal paidAmount;
    private String vocherNo;
    private String comment;
    private String personName;

    @Basic
    @Column(name = "ID")
    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "PayNoteID")
    public int getPayNoteId() {
        return payNoteId;
    }

    public void setPayNoteId(int payNoteId) {
        this.payNoteId = payNoteId;
    }

    @Basic
    @Column(name = "PersonID")
    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    @Basic
    @Column(name = "PreTaxAmount")
    public BigDecimal getPreTaxAmount() {
        return preTaxAmount;
    }

    public void setPreTaxAmount(BigDecimal preTaxAmount) {
        this.preTaxAmount = preTaxAmount;
    }

    @Basic
    @Column(name = "TaxAmount")
    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    @Basic
    @Column(name = "AfterTaxAmount")
    public BigDecimal getAfterTaxAmount() {
        return afterTaxAmount;
    }

    public void setAfterTaxAmount(BigDecimal afterTaxAmount) {
        this.afterTaxAmount = afterTaxAmount;
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
    @Column(name = "PaidAmount")
    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    @Basic
    @Column(name = "VocherNo")
    public String getVocherNo() {
        return vocherNo;
    }

    public void setVocherNo(String vocherNo) {
        this.vocherNo = vocherNo;
    }

    @Basic
    @Column(name = "Comment")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Basic
    @Column(name = "PersonName")
    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PayNoteItemView that = (PayNoteItemView) o;

        if (id != that.id) return false;
        if (payNoteId != that.payNoteId) return false;
        if (personId != that.personId) return false;
        if (state != that.state) return false;
        if (preTaxAmount != null ? !preTaxAmount.equals(that.preTaxAmount) : that.preTaxAmount != null) return false;
        if (taxAmount != null ? !taxAmount.equals(that.taxAmount) : that.taxAmount != null) return false;
        if (afterTaxAmount != null ? !afterTaxAmount.equals(that.afterTaxAmount) : that.afterTaxAmount != null)
            return false;
        if (paidAmount != null ? !paidAmount.equals(that.paidAmount) : that.paidAmount != null) return false;
        if (vocherNo != null ? !vocherNo.equals(that.vocherNo) : that.vocherNo != null) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        if (personName != null ? !personName.equals(that.personName) : that.personName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + payNoteId;
        result = 31 * result + personId;
        result = 31 * result + (preTaxAmount != null ? preTaxAmount.hashCode() : 0);
        result = 31 * result + (taxAmount != null ? taxAmount.hashCode() : 0);
        result = 31 * result + (afterTaxAmount != null ? afterTaxAmount.hashCode() : 0);
        result = 31 * result + state;
        result = 31 * result + (paidAmount != null ? paidAmount.hashCode() : 0);
        result = 31 * result + (vocherNo != null ? vocherNo.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (personName != null ? personName.hashCode() : 0);
        return result;
    }
}
