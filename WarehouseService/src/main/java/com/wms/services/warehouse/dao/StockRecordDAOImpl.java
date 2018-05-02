package com.wms.services.warehouse.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.InspectionNote;
import com.wms.utilities.model.InspectionNoteView;
import com.wms.utilities.model.StockRecord;
import com.wms.utilities.model.StockRecordView;
import org.springframework.stereotype.Repository;

@Repository
public class StockRecordDAOImpl
        extends BaseDAOImpl<StockRecord,StockRecordView>
        implements StockRecordDAO{
    public StockRecordDAOImpl(){
        super(StockRecord.class,StockRecordView.class,StockRecord::getId);
    }

}
