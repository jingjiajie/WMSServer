package com.wms.services.warehouse.dao;

import com.wms.services.warehouse.model.*;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class WarehouseEntryItemDAOImpl implements WarehouseEntryItemDAO {
    @Autowired
    SessionFactory sessionFactory;

    private WarehouseServiceDAOTemplate<WarehouseEntryItem, WarehouseEntryItemView> getDAOTemplate() {
        return new WarehouseServiceDAOTemplate<>
                (this.sessionFactory, "WarehouseEntryItem", "WarehouseEntryItemView", WarehouseEntryItem::getId);
    }

    @Override
    public int[] add(String database, WarehouseEntryItem[] items) throws WMSDAOException {
        return this.getDAOTemplate().add(database, items);
    }

    @Override
    public void update(String database, WarehouseEntryItem[] items) throws WMSDAOException {
        this.getDAOTemplate().update(database, items);
    }

    @Override
    public void remove(String database, int[] ids) throws WMSDAOException {
        this.getDAOTemplate().remove(database, ids);
    }

    @Override
    public WarehouseEntryItemView[] find(String database, Condition cond) throws WMSDAOException {
        return this.getDAOTemplate().find(database, cond, WarehouseEntryItemView.class);
    }
}
