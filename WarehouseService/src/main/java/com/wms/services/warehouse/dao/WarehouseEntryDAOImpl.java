package com.wms.services.warehouse.dao;

import com.wms.services.warehouse.model.WarehouseEntry;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WarehouseEntryDAOImpl implements WarehouseEntryDAO {
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    WarehouseServiceDAOTemplateFactory daoTemplateFactory;

    @Override
    public int[] add(String database, WarehouseEntry[] warehouseEntries) throws WMSDAOException {
        WarehouseServiceDAOTemplate<WarehouseEntry> daoTemplate =
                daoTemplateFactory.createWarehouseServiceDAOTemplate("WarehouseEntry", WarehouseEntry::getId);
        return daoTemplate.add(database, warehouseEntries);
    }

    @Override
    public void update(String database, WarehouseEntry[] warehouseEntries) throws WMSDAOException {
        WarehouseServiceDAOTemplate<WarehouseEntry> daoTemplate =
                daoTemplateFactory.createWarehouseServiceDAOTemplate("WarehouseEntry", WarehouseEntry::getId);
        daoTemplate.update(database, warehouseEntries);
    }

    @Override
    public void remove(String database, int[] ids) throws WMSDAOException {
        WarehouseServiceDAOTemplate<WarehouseEntry> daoTemplate =
                daoTemplateFactory.createWarehouseServiceDAOTemplate("WarehouseEntry", WarehouseEntry::getId);
        daoTemplate.remove(database, ids);
    }

    @Override
    public WarehouseEntry[] find(String database, Condition cond) throws WMSDAOException {
        WarehouseServiceDAOTemplate<WarehouseEntry> daoTemplate =
                daoTemplateFactory.createWarehouseServiceDAOTemplate("WarehouseEntry", WarehouseEntry::getId);
        List<WarehouseEntry> resultList = daoTemplate.find(database, cond);
        WarehouseEntry[] resultArray = new WarehouseEntry[resultList.size()];
        resultList.toArray(resultArray);
        return resultArray;
    }
}
