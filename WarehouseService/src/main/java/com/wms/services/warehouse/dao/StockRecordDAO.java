package com.wms.services.warehouse.dao;

import com.wms.utilities.dao.BaseDAO;
import com.wms.utilities.model.StockRecord;
import com.wms.utilities.model.StockRecordView;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRecordDAO
        extends BaseDAO<StockRecord,StockRecordView>{}


