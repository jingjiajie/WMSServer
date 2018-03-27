package com.wms.services.ledger.dao;

import com.wms.services.ledger.model.AccountTitle;
import com.wms.services.ledger.model.Person;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Repository
public class AccountTitleDAOImpl implements AccountTitleDAO {
    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public  int[] add(String database, AccountTitle[] accountTitles) throws  WMSDAOException{
        if(accountTitles.length == 0){
            return new int[0];
        }
        Session session = sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + database + ";").executeUpdate();
        }catch (Throwable ex){
            throw new DatabaseNotFoundException(database);
        }

        try {
            for (AccountTitle accountTitle : accountTitles) {
                session.save(accountTitle);
            }
            int ids[] = Stream.of(accountTitles).mapToInt((p) -> p.getId()).toArray();
            return ids;
        }catch (Throwable ex) {
            throw new WMSDAOException(ex.getMessage());
        }
    }
}
