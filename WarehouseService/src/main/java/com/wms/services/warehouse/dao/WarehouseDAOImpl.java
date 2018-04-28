package com.wms.services.warehouse.dao;

import com.wms.services.warehouse.model.Supplier;
import com.wms.services.warehouse.model.SupplierView;
import com.wms.services.warehouse.model.Warehouse;
import com.wms.services.warehouse.model.WarehouseView;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Repository

public class WarehouseDAOImpl implements WarehouseDAO {
    @Autowired
    SessionFactory sessionFactory;

    private WarehouseServiceDAOTemplate<Warehouse,WarehouseView> getDAOTemplate() {
        return new WarehouseServiceDAOTemplate<>
                (this.sessionFactory, "Warehouse", "WarehouseView", Warehouse::getId);
    }

    @Override
    public int[] add(String database,Warehouse[] warehouses) throws WMSDAOException{
        return this.getDAOTemplate().add(database,warehouses);
    }

    @Override
    public void update(String database, Warehouse warehouses[]) throws WMSDAOException{
        this.getDAOTemplate().update(database, warehouses);
    }

    @Override
    public void remove(String database, int ids[]) throws WMSDAOException{
        this.getDAOTemplate().remove(database, ids);
    }

    @Override
    public WarehouseView[] find(String database,Condition cond) throws WMSDAOException{
        return this.getDAOTemplate().find(database, cond, WarehouseView.class);
    }
}
