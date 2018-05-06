package com.wms.services.warehouse.dao;

import com.wms.utilities.dao.BaseDAO;
import com.wms.utilities.model.TransferRecord;
import com.wms.utilities.model.TransferRecordView;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

public interface TransferRecordDAO
        extends BaseDAO<TransferRecord,TransferRecordView> {
}
