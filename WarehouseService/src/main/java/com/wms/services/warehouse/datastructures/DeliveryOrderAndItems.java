package com.wms.services.warehouse.datastructures;


import com.wms.utilities.model.DeliveryOrderItemView;
import com.wms.utilities.model.DeliveryOrderView;

import java.util.List;

public class DeliveryOrderAndItems {
    public DeliveryOrderView getDeliveryOrder() {
        return deliveryOrder;
    }

    public List<DeliveryOrderItemView> getDeliveryOrderItems() {
        return deliveryOrderItems;
    }

    public void setDeliveryOrder(DeliveryOrderView deliveryOrder) {
        this.deliveryOrder = deliveryOrder;
    }

    public void setDeliveryOrderItems(List<DeliveryOrderItemView> deliveryOrderItems) {
        this.deliveryOrderItems = deliveryOrderItems;
    }

    private DeliveryOrderView deliveryOrder;
    private List<DeliveryOrderItemView> deliveryOrderItems;
}
