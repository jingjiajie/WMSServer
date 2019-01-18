package com.wms.services.settlement.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.DeliveryAmountDetails;
import com.wms.utilities.model.DeliveryAmountDetailsView;

public class DeliveryAmountDetailsDAOImpl
        extends BaseDAOImpl<DeliveryAmountDetails, DeliveryAmountDetailsView>
        implements DeliveryAmountDetailsDAO {
    public DeliveryAmountDetailsDAOImpl() {
        super(DeliveryAmountDetails.class, DeliveryAmountDetailsView.class, DeliveryAmountDetails::getId); }
}
