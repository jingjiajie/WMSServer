package com.wms.services.warehouse.datastructures;

public class TransferFinishArgs {
    public boolean isAllFinish() {
        return allFinish;
    }

    public int getTransferOrderId() {
        return transferOrderId;
    }

    public boolean isQualified() {
        return qualified;
    }

    public int getTargetStorageLocationId() {
        return targetStorageLocationId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setAllFinish(boolean allFinish) {
        this.allFinish = allFinish;
    }

    public void setTransferOrderId(int transferOrderId) {
        this.transferOrderId = transferOrderId;
    }

    public void setQualified(boolean qualified) {
        this.qualified = qualified;
    }

    public void setTargetStorageLocationId(int targetStorageLocationId) {
        this.targetStorageLocationId = targetStorageLocationId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    private boolean allFinish = false;//是否全部完成移库
    private int transferOrderId = -1;//移库单ID
    private boolean qualified = true;
    private int targetStorageLocationId = -1;//（整单完成）目标库区ID
    private int personId = -1;//（整单完成）作业人员ID，若留空则不改变原作业人

    public TransferFinishItem[] getTransferFinishItems() {
        return transferFinishItems;
    }

    public void setTransferFinishItems(TransferFinishItem[] transferFinishItems) {
        this.transferFinishItems = transferFinishItems;
    }

    private TransferFinishItem[] transferFinishItems = new TransferFinishItem[]{};
}
