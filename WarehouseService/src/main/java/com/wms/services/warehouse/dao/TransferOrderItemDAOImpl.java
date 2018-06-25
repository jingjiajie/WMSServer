package com.wms.services.warehouse.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.TransferOrderItem;
import com.wms.utilities.model.TransferOrderItemView;
import org.springframework.stereotype.Repository;


@Repository
public class TransferOrderItemDAOImpl
    extends BaseDAOImpl<TransferOrderItem,TransferOrderItemView>
    implements TransferOrderItemDAO{

    public TransferOrderItemDAOImpl(){
        super(TransferOrderItem.class,TransferOrderItemView.class,TransferOrderItem::getId);
    }
}
