package com.wms.services.warehouse.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.TransferRecord;
import com.wms.utilities.model.TransferRecordView;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class TransferRecordDAOImpl
        extends BaseDAOImpl<TransferRecord, TransferRecordView>
implements TransferRecordDAO{
    public TransferRecordDAOImpl() {
        super(TransferRecord.class, TransferRecordView.class, TransferRecord::getId);
    }
}
