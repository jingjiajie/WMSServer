package com.wms.utilities.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class SalaryItem {
    private int id;
    private Integer salaryTypeId;
    private String name;
    private BigDecimal defaultAmount;
    private int type;
    private int warehouseId;
    private String formula;
    private int giveOut;
    private int priority;
    private String identifier;

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
    @Column(name = "SalaryTypeID")
    public Integer getSalaryTypeId() {
        return salaryTypeId;
    }

    public void setSalaryTypeId(Integer salaryTypeId) {
        this.salaryTypeId = salaryTypeId;
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
    @Column(name = "DefaultAmount")
    public BigDecimal getDefaultAmount() {
        return defaultAmount;
    }

    public void setDefaultAmount(BigDecimal defaultAmount) {
        this.defaultAmount = defaultAmount;
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
    @Column(name = "WarehouseID")
    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    @Basic
    @Column(name = "Formula")
    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    @Basic
    @Column(name = "GiveOut")
    public int getGiveOut() {
        return giveOut;
    }

    public void setGiveOut(int giveOut) {
        this.giveOut = giveOut;
    }

    @Basic
    @Column(name = "Priority")
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Basic
    @Column(name = "Identifier")
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        SalaryItem that = (SalaryItem) object;

        if (id != that.id) return false;
        if (type != that.type) return false;
        if (warehouseId != that.warehouseId) return false;
        if (giveOut != that.giveOut) return false;
        if (priority != that.priority) return false;
        if (salaryTypeId != null ? !salaryTypeId.equals(that.salaryTypeId) : that.salaryTypeId != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (defaultAmount != null ? !defaultAmount.equals(that.defaultAmount) : that.defaultAmount != null)
            return false;
        if (formula != null ? !formula.equals(that.formula) : that.formula != null) return false;
        if (identifier != null ? !identifier.equals(that.identifier) : that.identifier != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (salaryTypeId != null ? salaryTypeId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (defaultAmount != null ? defaultAmount.hashCode() : 0);
        result = 31 * result + type;
        result = 31 * result + warehouseId;
        result = 31 * result + (formula != null ? formula.hashCode() : 0);
        result = 31 * result + giveOut;
        result = 31 * result + priority;
        result = 31 * result + (identifier != null ? identifier.hashCode() : 0);
        return result;
    }
}
