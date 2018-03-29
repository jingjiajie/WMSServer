package com.wms.services.ledger.dao;

import com.wms.services.ledger.model.Person;
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
public class PersonDAOImpl implements PersonDAO {
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Autowired
    private SessionFactory sessionFactory;

    public int[] add(String database, Person[] persons) throws WMSDAOException{
        if(persons.length == 0){
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
            for (Person person : persons) {
                session.save(person);
            }
            int ids[] = Stream.of(persons).mapToInt((p) -> p.getId()).toArray();
            return ids;
        }catch (Throwable ex){
            throw new WMSDAOException(ex.getMessage());
        }
    }

    public void update(String database, Person persons[]) throws WMSDAOException{
        Session session = sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + database + ";").executeUpdate();
        }catch (Throwable ex){
            throw new DatabaseNotFoundException(database);
        }

        try {
            for (Person person : persons) {
                StringBuffer sbHQLString = new StringBuffer();
                session.update(person);
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
            session.createQuery(String.format("delete from Person where ID in(%s)", idStr.toString())).executeUpdate();
        }catch (Throwable ex){
            throw new WMSDAOException(ex.getMessage());
        }
    }

    public Person[] find(String database,Condition cond) throws WMSDAOException{
        Session session = sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + database + ";").executeUpdate();
        }catch (Throwable ex){
            throw new DatabaseNotFoundException(database);
        }

        try {
            Query query = cond.createQuery("Person", session);

            List<Person> listPerson = query.list();
            Person[] arrPerson = new Person[listPerson.size()];
            listPerson.toArray(arrPerson);
            return arrPerson;
        }catch (Throwable ex){
            throw new WMSDAOException(ex.getMessage());
        }
    }
}