package com.wms.services.warehouse.dao;

import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.wms.services.warehouse.model.StorageArea;
import java.util.List;
import java.util.stream.Stream;

@Repository
public class StorgeAreaDAOImpl implements StorgeAreaDAO {
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Autowired
    private SessionFactory sessionFactory;

    public int[] add(String database, StorageArea[] storageAreas) throws WMSDAOException{
        if(storageAreas.length == 0){
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
            for (StorageArea storageArea: storageAreas) {
                session.save(storageArea);
            }
            int ids[] = Stream.of(storageAreas).mapToInt((p) -> p.getId()).toArray();
            return ids;
        }catch (Throwable ex){
            throw new WMSDAOException(ex.getMessage());
        }
    }
    public void update(String database,StorageArea storageAreas[]) throws WMSDAOException{
        Session session = sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + database + ";").executeUpdate();
        }catch (Throwable ex){
            throw new DatabaseNotFoundException(database);
        }
        try {
            for (StorageArea storageArea : storageAreas) {
                session.update(storageArea);
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
            session.createQuery(String.format("delete from StorageArea where ID in(%s)", idStr.toString())).executeUpdate();
        }catch (Throwable ex){
            throw new WMSDAOException(ex.getMessage());
        }
    }
    public StorageArea[] find(String database,Condition cond) throws WMSDAOException{
        Session session = sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + database + ";").executeUpdate();
        }catch (Throwable ex){
            throw new DatabaseNotFoundException(database);
        }
        try {
            Query query = cond.createQuery("StorageArea", session);
            List<StorageArea> listStorageArea = query.list();
            StorageArea[] arrStorageArea = new StorageArea[listStorageArea.size()];
            listStorageArea.toArray(arrStorageArea);
            return arrStorageArea;}
        catch (Throwable ex){
            throw new WMSDAOException(ex.getMessage());
        }
    }

}
