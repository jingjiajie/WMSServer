package com.wms.services.warehouse.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.WarehouseEntry;
import com.wms.utilities.model.WarehouseEntryView;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class WarehouseEntryDAOImpl
        extends BaseDAOImpl<WarehouseEntry, WarehouseEntryView>
        implements WarehouseEntryDAO {

    public WarehouseEntryDAOImpl(){
        super(WarehouseEntry.class,WarehouseEntryView.class,WarehouseEntry::getId);
    }
}
