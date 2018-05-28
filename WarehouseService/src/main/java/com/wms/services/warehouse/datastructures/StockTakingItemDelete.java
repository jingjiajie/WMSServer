package com.wms.services.warehouse.datastructures;

public class StockTakingItemDelete {
    public int[] getDeleteIds() {
        return deleteIds;
    }

    public int getPersonId() {
        return personId;
    }

    public void setDeleteIds(int[] deleteIds) {
        this.deleteIds = deleteIds;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    int[] deleteIds=null;
    int personId;

}
