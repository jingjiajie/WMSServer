package com.wms.services.ledger.dao;

import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.model.Tax;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public class TaxDAOImpl implements TaxDAO {
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Autowired
    private SessionFactory sessionFactory;

    public int[] add(String database, Tax[] taxes) throws WMSDAOException {
        if (taxes.length == 0) {
            return new int[0];
        }
        if (sessionFactory == null) {
            System.out.println("sessionFactory 为空");
        }
        Session session = sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + database + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException(database);
        }

        try {
            for (Tax tax : taxes) {
                session.save(tax);
            }
            int ids[] = Stream.of(taxes).mapToInt((p) -> p.getId()).toArray();
            return ids;
        } catch (Throwable ex) {
            throw new WMSDAOException(ex.getMessage());
        }
    }

    public void update(String database, Tax taxes[]) throws WMSDAOException {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + database + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException(database);
        }

        try {
            for (Tax tax : taxes) {
                StringBuffer sbHQLString = new StringBuffer();
                session.merge(tax);
            }
        } catch (Throwable ex) {
            throw new WMSDAOException(ex.getMessage());
        }
    }

    public void remove(String database, int[] ids) throws WMSDAOException {
        if (ids.length == 0) {
            return;
        }
        Session session = sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + database + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException(database);
        }
        try {
            StringBuffer idStr = new StringBuffer();
            for (int id : ids) {
                idStr.append(String.format("%d,", id));
            }
            idStr.setLength(idStr.length() - 1);
            session.createQuery(String.format("delete from Tax where ID in(%s)", idStr.toString())).executeUpdate();
        }catch (Throwable ex){
            throw new WMSDAOException(ex.getMessage());
        }
    }

    public Tax[] find(String database, Condition cond) throws WMSDAOException {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + database + ";").executeUpdate();
        }catch (Throwable ex){
            throw new DatabaseNotFoundException(database);
        }

        try {
            Query query = cond.createQuery("Tax", session);

            List<Tax> listTax = query.list();
            Tax[] arrTax = new Tax[listTax.size()];
            listTax.toArray(arrTax);
            return arrTax;
        }catch (Throwable ex){
            throw new WMSDAOException(ex.getMessage());
        }
    }
}