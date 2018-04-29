package com.wms.services.warehouse.dao;

import com.wms.utilities.model.StorageArea;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.model.StorageAreaView;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public class StorageAreaDAOImpl implements StorageAreaDAO {
    @Autowired
    SessionFactory sessionFactory;
    private WarehouseServiceDAOTemplate<StorageArea, StorageAreaView> getDAOTemplate() {
        return new WarehouseServiceDAOTemplate<>
                (this.sessionFactory, "StorageArea", "StorageAreaView", StorageArea::getId);
    }
    @Override
    public int[] add(String database, StorageArea[] storageAreas) throws WMSDAOException{
        return this.getDAOTemplate().add(database, storageAreas);
    }
    @Override
    public void update(String database,StorageArea storageAreas[]) throws WMSDAOException{
        this.getDAOTemplate().update(database, storageAreas);
    }
    @Override
    public void remove(String database, int ids[]) throws WMSDAOException{
        this.getDAOTemplate().remove(database, ids);
    }
    @Override
    public StorageAreaView[] find(String database,Condition cond) throws WMSDAOException{
        return this.getDAOTemplate().find(database, cond, StorageAreaView.class);
    }
}
