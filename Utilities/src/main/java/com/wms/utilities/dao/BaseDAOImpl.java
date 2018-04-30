package com.wms.utilities.dao;

import com.wms.utilities.model.WarehouseEntryView;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Array;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class BaseDAOImpl<TTable, TView> {
    @Autowired
    private SessionFactory sessionFactory;

    private String tableName;
    private String viewName;
    private Class<TTable> classTable;
    private Class<TView> classView;
    private Function<TTable, Integer> methodGetPK;

    public BaseDAOImpl(Class<TTable> classTable, Class<TView> classView, Function<TTable, Integer> methodGetPK) {
        this.methodGetPK = methodGetPK;
        this.tableName = classTable.getName();
        this.viewName = classView.getName();
        this.classTable = classTable;
        this.classView = classView;
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

    public TView[] find(String database, Condition cond) throws WMSDAOException {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + database + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException(database);
        }
        try {
            Query query = cond.createQuery(this.viewName, session);
            List<TView> resultList = query.list();
            TView[] resultArray = (TView[]) Array.newInstance(this.classView,resultList.size());
            resultList.toArray(resultArray);
            return resultArray;
        } catch (Throwable ex) {
            throw new WMSDAOException(ex.getMessage());
        }
    }
}