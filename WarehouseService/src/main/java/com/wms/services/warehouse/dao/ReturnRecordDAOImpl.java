package com.wms.services.warehouse.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import org.springframework.stereotype.Repository;

@Repository
public class ReturnRecordDAOImpl
        extends BaseDAOImpl<ReturnRecord,ReturnRecordView>
        implements ReturnRecordDAO{
    public ReturnRecordDAOImpl(){
        super(ReturnRecord.class,ReturnRecordView.class,ReturnRecord::getId);
    }
}
