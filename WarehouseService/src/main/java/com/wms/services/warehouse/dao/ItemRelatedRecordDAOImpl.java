package com.wms.services.warehouse.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.ItemRelatedRecord;
import org.springframework.stereotype.Repository;

@Repository
public class ItemRelatedRecordDAOImpl
        extends BaseDAOImpl<ItemRelatedRecord, ItemRelatedRecord>
        implements ItemRelatedRecordDAO {

    public ItemRelatedRecordDAOImpl()

    {
        super(ItemRelatedRecord.class, ItemRelatedRecord.class, ItemRelatedRecord::getId);
    }
}
