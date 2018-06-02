package com.wms.services.ledger.dao;

import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.model.TaxItem;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public class TaxItemDAOImpl implements TaxItemDAO {
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Autowired
    private SessionFactory sessionFactory;

    public int[] add(String database, TaxItem[] taxItems) throws WMSDAOException{
        if(taxItems.length == 0){
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
            for (TaxItem taxItem : taxItems) {
                session.save(taxItem);
            }
            int ids[] = Stream.of(taxItems).mapToInt((p) -> p.getId()).toArray();
            return ids;
        }catch (Throwable ex){
            throw new WMSDAOException(ex.getMessage());
        }
    }

    public void update(String database, TaxItem taxItems[]) throws WMSDAOException{
        Session session = sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + database + ";").executeUpdate();
        }catch (Throwable ex){
            throw new DatabaseNotFoundException(database);
        }

        try {
            for (TaxItem taxItem : taxItems) {
                StringBuffer sbHQLString = new StringBuffer();
                session.merge(taxItem);
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
            session.createQuery(String.format("delete from TaxItem where ID in(%s)", idStr.toString())).executeUpdate();
        }catch (Throwable ex){
            throw new WMSDAOException(ex.getMessage());
        }
    }

    public TaxItem[] find(String database,Condition cond) throws WMSDAOException{
        Session session = sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + database + ";").executeUpdate();
        }catch (Throwable ex){
            throw new DatabaseNotFoundException(database);
        }

        try {
            Query query = cond.createQuery("TaxItem", session);

            List<TaxItem> listTaxItem = query.list();
            TaxItem[] arrTaxItem = new TaxItem[listTaxItem.size()];
            listTaxItem.toArray(arrTaxItem);
            return arrTaxItem;
        }catch (Throwable ex){
            throw new WMSDAOException(ex.getMessage());
        }
    }

}
