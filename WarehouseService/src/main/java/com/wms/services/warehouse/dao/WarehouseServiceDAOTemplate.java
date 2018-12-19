package com.wms.services.warehouse.dao;

import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.lang.reflect.Array;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class WarehouseServiceDAOTemplate<TTable, TView> {
    private SessionFactory sessionFactory;
    private String tableName;
    private String viewName;
    private Function<TTable, Integer> methodGetPK;

    public WarehouseServiceDAOTemplate(SessionFactory sessionFactory, String tableName, String viewName, Function<TTable, Integer> methodGetPK) {
        this.methodGetPK = methodGetPK;
        this.tableName = tableName;
        this.viewName = viewName;
        this.sessionFactory = sessionFactory;
    }

    public int[] add(String database, TTable[] objs) {
        if (objs.length == 0) {
            return new int[0];
        }

        Session session = sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + database + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException(database);
        }
        try {
            for (TTable obj : objs) {
                session.save(obj);
            }
            int ids[] = Stream.of(objs).mapToInt(this.methodGetPK::apply).toArray();
            return ids;
        } catch (Throwable ex) {
            throw new WMSDAOException(ex.getMessage());
        }
    }

    public void update(String database, TTable objs[]) throws WMSDAOException {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + database + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException(database);
        }

        try {
            for (TTable obj : objs) {
                session.update(obj);
            }
        } catch (Throwable ex) {
            throw new WMSDAOException(ex.getMessage());
        }
    }

    public void remove(String database, int ids[]) throws WMSDAOException {
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
            session.createQuery(String.format("delete from " + this.tableName + " where ID in(%s)", idStr.toString())).executeUpdate();
        } catch (Throwable ex) {
            throw new WMSDAOException(ex.getMessage());
        }
    }

    public List<TView> find(String database, Condition cond) throws WMSDAOException {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + database + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException(database);
        }
        try {
            Query query = cond.createQuery(this.viewName, session);
            List<TView> resultList = query.list();
            return resultList;
        } catch (Throwable ex) {
            throw new WMSDAOException(ex.getMessage());
        }
    }

    public TView[] find(String database,Condition cond,Class<TView> viewClass) throws WMSDAOException{
        List<TView> resultList = this.find(database, cond);
        TView[] resultArray = (TView[]) Array.newInstance(viewClass,resultList.size());
        resultList.toArray(resultArray);
        return resultArray;
    }
}