package com.wms.services.warehouse.dao;

import com.wms.services.warehouse.controller.PersonController;
import com.wms.services.warehouse.model.WarehouseEntry;
import com.wms.services.warehouse.model.WarehouseEntryView;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

@Repository
public class WarehouseEntryDAOImpl implements WarehouseEntryDAO {
    @Autowired
    SessionFactory sessionFactory;

    private WarehouseServiceDAOTemplate<WarehouseEntry,WarehouseEntryView> getDAOTemplate(){
        return new WarehouseServiceDAOTemplate<>
                (this.sessionFactory,"WarehouseEntry","WarehouseEntryView", WarehouseEntry::getId);
    }

    @Override
    public int[] add(String database, WarehouseEntry[] warehouseEntries) throws WMSDAOException {
        return this.getDAOTemplate().add(database, warehouseEntries);
    }

    @Override
    public void update(String database, WarehouseEntry[] warehouseEntries) throws WMSDAOException {
        this.getDAOTemplate().update(database, warehouseEntries);
    }

    @Override
    public void remove(String database, int[] ids) throws WMSDAOException {
        this.getDAOTemplate().remove(database, ids);
    }

    @Override
    public WarehouseEntryView[] find(String database, Condition cond) throws WMSDAOException {
        List<WarehouseEntryView> resultList = this.getDAOTemplate().find(database, cond);
        WarehouseEntryView[] resultArray = new WarehouseEntryView[resultList.size()];
        resultList.toArray(resultArray);
        return resultArray;
    }
}
