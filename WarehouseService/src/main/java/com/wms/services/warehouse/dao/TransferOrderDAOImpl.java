package com.wms.services.warehouse.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import org.springframework.stereotype.Repository;
import com.wms.utilities.model.TransferOrder;
import com.wms.utilities.model.TransferOrderView;
@Repository
public class TransferOrderDAOImpl
    extends BaseDAOImpl<TransferOrder,TransferOrderView>
    implements TransferOrderDAO{

    public TransferOrderDAOImpl(){
        super(TransferOrder.class,TransferOrderView.class,TransferOrder::getId);
    }
}
