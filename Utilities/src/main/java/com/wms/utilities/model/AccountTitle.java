package com.wms.utilities.model;

import javax.persistence.*;

@Entity
public class AccountTitle {
    private int id;
    private String name;
    private String no;
    private int type;
    private int direction;
    private int enabled;
    private String accountTitleDdpendent;

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
    @Column(name = "Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    @Column(name = "Type")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Basic
    @Column(name = "Direction")
    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
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
    @Column(name = "AccountTitleDdpendent")
    public String getAccountTitleDdpendent() {
        return accountTitleDdpendent;
    }

    public void setAccountTitleDdpendent(String accountTitleDdpendent) {
        this.accountTitleDdpendent = accountTitleDdpendent;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        AccountTitle that = (AccountTitle) object;

        if (id != that.id) return false;
        if (type != that.type) return false;
        if (direction != that.direction) return false;
        if (enabled != that.enabled) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (no != null ? !no.equals(that.no) : that.no != null) return false;
        if (accountTitleDdpendent != null ? !accountTitleDdpendent.equals(that.accountTitleDdpendent) : that.accountTitleDdpendent != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (no != null ? no.hashCode() : 0);
        result = 31 * result + type;
        result = 31 * result + direction;
        result = 31 * result + enabled;
        result = 31 * result + (accountTitleDdpendent != null ? accountTitleDdpendent.hashCode() : 0);
        return result;
    }
}
