package com.wms.services.warehouse.dao;

import com.wms.utilities.dao.BaseDAO;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.model.StockRecord;
import com.wms.utilities.model.StockRecordView;
import com.wms.utilities.model.StockRecordViewNewest;

public interface StockRecordDAO
        extends BaseDAO<StockRecord,StockRecordView>{
    StockRecordViewNewest[] findNewest(String accountBook, Condition condition);
    long findCountNew(String database,Condition cond) throws WMSDAOException;
}


