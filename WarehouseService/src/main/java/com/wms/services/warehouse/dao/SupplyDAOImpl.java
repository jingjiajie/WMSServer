package com.wms.services.warehouse.dao;
import com.wms.services.warehouse.model.Supply;
import com.wms.services.warehouse.model.SupplyView;
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
@Transactional
public class SupplyDAOImpl implements SupplyDAO {
    @Autowired
    SessionFactory sessionFactory;

    private WarehouseServiceDAOTemplate<Supply, SupplyView> getDAOTemplate() {
        return new WarehouseServiceDAOTemplate<>
                (this.sessionFactory, "Supply", "SupplyView", Supply::getId);
    }

    @Override
    public int[] add(String database,Supply[] supplies) throws WMSDAOException{
        return this.getDAOTemplate().add(database, supplies);
    }

    @Override
    public void update(String database, Supply supplies[]) throws WMSDAOException{
        this.getDAOTemplate().update(database, supplies);
    }

    @Override
    public void remove(String database, int ids[]) throws WMSDAOException{
        this.getDAOTemplate().remove(database, ids);
    }

    @Override
    public SupplyView[] find(String database,Condition cond) throws WMSDAOException{
        return this.getDAOTemplate().find(database, cond, SupplyView.class);
    }


}
