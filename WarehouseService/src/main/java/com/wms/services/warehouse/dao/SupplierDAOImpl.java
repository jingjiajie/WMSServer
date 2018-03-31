package com.wms.services.warehouse.dao;

import com.wms.services.warehouse.model.Supplier;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.stream.Stream;
@Repository
public class SupplierDAOImpl implements SupplierDAO {
    public SessionFactory getSessionFactory() {
     return sessionFactory;
    }
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    private SessionFactory sessionFactory;
    public int[] add(String database,Supplier[] supplier) throws WMSDAOException{
        if(supplier.length == 0){
            return new int[0];
        }
        if(sessionFactory==null){
            System.out.println("sessionFactory 为空");
        }
        Session session = sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + database + ";").executeUpdate();
        }catch (Throwable ex){
            throw new DatabaseNotFoundException(database);
        }
        try {
            for (Supplier person : supplier) {
                session.save(person);
            }
            int ids[] = Stream.of(supplier).mapToInt((p) -> p.getId()).toArray();
            return ids;
        }catch (Throwable ex){
            throw new WMSDAOException(ex.getMessage());
        }
    }

    public void update(String database, Supplier suppliers[]) throws WMSDAOException{
        Session session = sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + database + ";").executeUpdate();
        }catch (Throwable ex){
            throw new DatabaseNotFoundException(database);
        }

        try {
            for (Supplier supplier : suppliers) {
                StringBuffer sbHQLString = new StringBuffer();
                session.update(supplier);
            }
        }catch (Throwable ex){
            throw new WMSDAOException(ex.getMessage());
        }
    }






}










