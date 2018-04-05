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
    public int[] add(String database,Supplier[] suppliers) throws WMSDAOException{
        if(suppliers.length == 0){
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
            for (Supplier supplier : suppliers) {
                session.save(supplier);
            }
            int ids[] = Stream.of(suppliers).mapToInt((p) -> p.getId()).toArray();
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
                session.update(supplier);
            }
        }catch (Throwable ex){
            throw new WMSDAOException(ex.getMessage());
        }
    }

    public void remove(String database, int ids[]) throws WMSDAOException{
        if(ids.length == 0){
            return;
        }
        Session session = sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + database + ";").executeUpdate();
        }catch (Throwable ex){
            throw new DatabaseNotFoundException(database);
        }
        try {
            StringBuffer idStr = new StringBuffer();
            for (int id : ids) {
                idStr.append(String.format("%d,", id));
            }
            idStr.setLength(idStr.length() - 1);
            session.createQuery(String.format("delete from Supplier where ID in(%s)", idStr.toString())).executeUpdate();
        }catch (Throwable ex){
            throw new WMSDAOException(ex.getMessage());
        }
    }

    public Supplier[] find(String database,Condition cond) throws WMSDAOException{
        Session session = sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + database + ";").executeUpdate();
        }catch (Throwable ex){
            throw new DatabaseNotFoundException(database);
        }
        try {
            Query query = cond.createQuery("Supplier", session);
            List<Supplier> listSupplier = query.list();
            Supplier[] arrSupplier = new Supplier[listSupplier.size()];
            listSupplier.toArray(arrSupplier);
            return arrSupplier;
        }catch (Throwable ex){
            throw new WMSDAOException(ex.getMessage());
        }
    }
    /* public List<Supplier> findInside(String database,String sql ) throws WMSDAOException{
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










