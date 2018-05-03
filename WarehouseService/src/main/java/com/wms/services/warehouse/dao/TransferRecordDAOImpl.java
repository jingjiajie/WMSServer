package com.wms.services.warehouse.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.TransferRecord;
import com.wms.utilities.model.TransferRecordView;
import com.wms.utilities.model.Warehouse;
import com.wms.utilities.model.WarehouseView;

public class TransferRecordDAOImpl
        extends BaseDAOImpl<TransferRecord, TransferRecordView>
implements TransferRecordDAO{
    public TransferRecordDAOImpl() {
        super(TransferRecord.class, TransferRecordView.class, TransferRecord::getId);
    }
}
