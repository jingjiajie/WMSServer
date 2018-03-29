package com.wms.utilities.datastructures;

public class OrderItem {
    public enum Order{
        ASC,DESC
    }

    private String key;
    private Order order = Order.ASC;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }


}
