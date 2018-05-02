package com.wms.services.warehouse.datastructures;

import java.math.BigDecimal;

public class InspectFinishItem {
    private int inspectionNoteItemId = -1;

    private boolean qualified = true;
    private BigDecimal returnAmount;
    private String returnUnit;
    private BigDecimal returnUnitAmount;
    private int returnStorageLocationId = -1;
    private int personId = -1;

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getInspectionNoteItemId() {
        return inspectionNoteItemId;
    }

    public void setInspectionNoteItemId(int inspectionNoteItemId) {
        this.inspectionNoteItemId = inspectionNoteItemId;
    }

    public boolean isQualified() {
        return qualified;
    }

    public void setQualified(boolean qualified) {
        this.qualified = qualified;
    }

    public BigDecimal getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(BigDecimal returnAmount) {
        this.returnAmount = returnAmount;
    }

    public String getReturnUnit() {
        return returnUnit;
    }

    public void setReturnUnit(String returnUnit) {
        this.returnUnit = returnUnit;
    }

    public BigDecimal getReturnUnitAmount() {
        return returnUnitAmount;
    }

    public void setReturnUnitAmount(BigDecimal returnUnitAmount) {
        this.returnUnitAmount = returnUnitAmount;
    }

    public int getReturnStorageLocationId() {
        return returnStorageLocationId;
    }

    public void setReturnStorageLocationId(int returnStorageLocationId) {
        this.returnStorageLocationId = returnStorageLocationId;
    }

}
