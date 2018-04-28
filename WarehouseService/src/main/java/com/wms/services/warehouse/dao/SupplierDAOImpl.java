package com.wms.services.warehouse.dao;
import com.wms.services.warehouse.model.Supplier;
import com.wms.services.warehouse.model.SupplierView;
import com.wms.services.warehouse.model.WarehouseEntry;
import com.wms.services.warehouse.model.WarehouseEntryView;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Repository
@Transactional
public class SupplierDAOImpl implements SupplierDAO {
    @Autowired
    SessionFactory sessionFactory;

    private WarehouseServiceDAOTemplate<Supplier, SupplierView> getDAOTemplate() {
        return new WarehouseServiceDAOTemplate<>
                (this.sessionFactory, "Supplier", "SupplierView", Supplier::getId);
    }

    @Override
    public int[] add(String database,Supplier[] suppliers) throws WMSDAOException{
        return this.getDAOTemplate().add(database, suppliers);
    }

    @Override
    public void update(String database, Supplier suppliers[]) throws WMSDAOException{
        this.getDAOTemplate().update(database, suppliers);
    }

    public void remove(String database, int ids[]) throws WMSDAOException{
        this.getDAOTemplate().remove(database, ids);
    }

    @Override
    public SupplierView[] find(String database,Condition cond) throws WMSDAOException{
        return this.getDAOTemplate().find(database, cond, SupplierView.class);
    }
/*
    public List<Supplier> findInside(String database,String sql ) throws WMSDAOException{
        Session session = sessionFactory.getCurrentSession();
        String entityName="Supplier";
        StringBuffer hqlString = new StringBuffer("from "+entityName+" ");
        Map<String,Object> queryParams = new HashMap<String, Object>();
        String SQL=sql ;
         try {
            session.createNativeQuery("USE " + database + ";").executeUpdate();
        }catch (Throwable ex){
            throw new DatabaseNotFoundException(database);
        }
        Query query = session.createQuery(SQL);
        for(Map.Entry<String,Object> entry : queryParams.entrySet()){
            query.setParameter(entry.getKey(),entry.getValue());
        }
        List<Supplier> listSupplier = query.list();
        return listSupplier;
    }
*/
}










