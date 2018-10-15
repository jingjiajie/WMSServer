package com.wms.utilities.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Invoice {
    private int id;
    private String no;
    private Integer supplierId;
    private Timestamp time;
    private int state;
    private String trackingNumber;
    private Timestamp sendingTime;

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
    @Column(name = "No")
    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
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
    @Column(name = "Time")
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
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
    @Column(name = "TrackingNumber")
    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    @Basic
    @Column(name = "SendingTime")
    public Timestamp getSendingTime() {
        return sendingTime;
    }

    public void setSendingTime(Timestamp sendingTime) {
        this.sendingTime = sendingTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Invoice invoice = (Invoice) o;

        if (id != invoice.id) return false;
        if (state != invoice.state) return false;
        if (no != null ? !no.equals(invoice.no) : invoice.no != null) return false;
        if (supplierId != null ? !supplierId.equals(invoice.supplierId) : invoice.supplierId != null) return false;
        if (time != null ? !time.equals(invoice.time) : invoice.time != null) return false;
        if (trackingNumber != null ? !trackingNumber.equals(invoice.trackingNumber) : invoice.trackingNumber != null)
            return false;
        if (sendingTime != null ? !sendingTime.equals(invoice.sendingTime) : invoice.sendingTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (no != null ? no.hashCode() : 0);
        result = 31 * result + (supplierId != null ? supplierId.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + state;
        result = 31 * result + (trackingNumber != null ? trackingNumber.hashCode() : 0);
        result = 31 * result + (sendingTime != null ? sendingTime.hashCode() : 0);
        return result;
    }
}
