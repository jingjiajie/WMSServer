package com.wms.services.ledger.dao;

import com.wms.services.ledger.model.AccountTitle;
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
public class AccountTitleDAOImpl implements AccountTitleDAO {
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Autowired
    private SessionFactory sessionFactory;

    public int[] add(String database, AccountTitle[] accountTitles) throws WMSDAOException {
        if (accountTitles.length == 0) {
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
            for (AccountTitle accountTitle : accountTitles) {
                session.save(accountTitle);
            }
            int ids[] = Stream.of(accountTitles).mapToInt((p) -> p.getId()).toArray();
            return ids;
        } catch (Throwable ex) {
            throw new WMSDAOException(ex.getMessage());
        }
    }

    public void update(String database, AccountTitle accountTitles[]) throws WMSDAOException {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + database + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException(database);
        }

        try {
            for (AccountTitle accountTitle : accountTitles) {
                StringBuffer sbHQLString = new StringBuffer();
                session.merge(accountTitle);
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
            session.createQuery(String.format("delete from AccountTitle where ID in(%s)", idStr.toString())).executeUpdate();
        }catch (Throwable ex){
            throw new WMSDAOException(ex.getMessage());
        }
    }

    public AccountTitle[] find(String database, Condition cond) throws WMSDAOException {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + database + ";").executeUpdate();
        }catch (Throwable ex){
            throw new DatabaseNotFoundException(database);
        }

        try {
            Query query = cond.createQuery("AccountTitle", session);

            List<AccountTitle> listAccountTitle = query.list();
            AccountTitle[] arrAccountTitle = new AccountTitle[listAccountTitle.size()];
            listAccountTitle.toArray(arrAccountTitle);
            return arrAccountTitle;
        }catch (Throwable ex){
            throw new WMSDAOException(ex.getMessage());
        }
    }
}