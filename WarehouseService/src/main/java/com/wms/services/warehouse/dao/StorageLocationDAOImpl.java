package com.wms.services.warehouse.dao;

import com.wms.services.warehouse.model.Supplier;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.wms.services.warehouse.model.StorageLocationView;
import java.util.List;
import java.util.stream.Stream;
import com.wms.services.warehouse.model.StorageLocation;
@Repository
public class StorageLocationDAOImpl implements StorageLocationDAO {
    @Autowired
    SessionFactory sessionFactory;
    private WarehouseServiceDAOTemplate<StorageLocation, StorageLocationView> getDAOTemplate() {
        return new WarehouseServiceDAOTemplate<>
                (this.sessionFactory, "StorageLocation", "StorageLocationView", StorageLocation::getId);
    }

    public int[] add(String database,StorageLocation[] storageLocations) throws WMSDAOException {
        return this.getDAOTemplate().add(database, storageLocations);
    }

    public void update(String database, StorageLocation storageLocations[]) throws WMSDAOException{
        this.getDAOTemplate().update(database, storageLocations);
    }

    public void remove(String database, int ids[]) throws WMSDAOException{
        this.getDAOTemplate().remove(database, ids);
    }
    public StorageLocationView[] find(String database,Condition cond) throws WMSDAOException{
        return this.getDAOTemplate().find(database, cond,StorageLocationView.class);
    }



}
