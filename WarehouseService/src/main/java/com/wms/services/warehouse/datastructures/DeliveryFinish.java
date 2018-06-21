package com.wms.services.warehouse.datastructures;

import java.util.List;

public class DeliveryFinish {
    private List<Integer> DeliveryOrderIds;
    private int personId;
    private String driverName;
    private String liscensePlateNumber;
    private String returnNoteNo;

    public String getReturnNoteNo() {
        return returnNoteNo;
    }

    public void setReturnNoteNo(String returnNoteNo) {
        this.returnNoteNo = returnNoteNo;
    }



    public void setDeliveryOrderIds(List<Integer> deliveryOrderIds) {
        DeliveryOrderIds = deliveryOrderIds;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setLiscensePlateNumber(String liscensePlateNumber) {
        this.liscensePlateNumber = liscensePlateNumber;
    }



    public List<Integer> getDeliveryOrderIds() {
        return DeliveryOrderIds;
    }

    public int getPersonId() {
        return personId;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getLiscensePlateNumber() {
        return liscensePlateNumber;
    }


}
