package com.wms.services.warehouse.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.*;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class WarehouseEntryItemDAOImpl
        extends BaseDAOImpl<WarehouseEntryItem, WarehouseEntryItemView>
        implements WarehouseEntryItemDAO {

    public WarehouseEntryItemDAOImpl() {
        super(WarehouseEntryItem.class, WarehouseEntryItemView.class, WarehouseEntryItem::getId);
    }
}
