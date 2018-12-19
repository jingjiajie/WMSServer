package com.wms.services.warehouse.datastructures;

public class RandomCode {
    public String getRandomCode() {
        return randomCode;
    }

    public int getEntryOrDeliver() {
        return entryOrDeliver;
    }

    public int getItemId() {
        return ItemId;
    }

    String randomCode;
    int entryOrDeliver ;

    public void setRandomCode(String randomCode) {
        this.randomCode = randomCode;
    }

    public void setEntryOrDeliver(int entryOrDeliver) {
        this.entryOrDeliver = entryOrDeliver;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }

    int ItemId;
}
