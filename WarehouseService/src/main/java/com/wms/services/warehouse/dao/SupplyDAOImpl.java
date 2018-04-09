package com.wms.services.warehouse.dao;

import com.wms.services.warehouse.model.Supply;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;
@Repository
public class SupplyDAOImpl implements SupplyDAO {
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Autowired
    private SessionFactory sessionFactory;
    public int[] add(String database,Supply[] supplies) throws WMSDAOException {
        if(supplies.length == 0){
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
            for (Supply supply : supplies) {
                session.save(supply);
            }
            int ids[] = Stream.of(supplies).mapToInt((p) -> p.getId()).toArray();
            return ids;
        }catch (Throwable ex){
            throw new WMSDAOException(ex.getMessage());
        }
    }

    public void update(String database, Supply supplies[]) throws WMSDAOException{
        Session session = sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + database + ";").executeUpdate();
        }catch (Throwable ex){
            throw new DatabaseNotFoundException(database);
        }

        try {
            for (Supply supply : supplies) {
                StringBuffer sbHQLString = new StringBuffer();
                session.update(supply);
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
            session.createQuery(String.format("delete from Supply where ID in(%s)", idStr.toString())).executeUpdate();
        }catch (Throwable ex){
            throw new WMSDAOException(ex.getMessage());
        }
    }
    public Supply[] find(String database,Condition cond) throws WMSDAOException{
        Session session = sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + database + ";").executeUpdate();
        }catch (Throwable ex){
            throw new DatabaseNotFoundException(database);
        }
        try {
            Query query = cond.createQuery("Supply", session);
            List<Supply> listSupply = query.list();
            Supply[] arrSupply = new Supply[listSupply.size()];
            listSupply.toArray(arrSupply);
            return arrSupply;
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
