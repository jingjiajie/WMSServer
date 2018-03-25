package com.wms.services.ledger.dao;

import com.wms.services.ledger.model.Person;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class PersonDAOImpl implements PersonDAO {
    @Autowired
    private SessionFactory sessionFactory;

    public void add(String database, Person[] persons) {
        Session session = this.sessionFactory.getCurrentSession();
        session.createNativeQuery("USE "+database+";").executeUpdate();
        for (Person p : persons) {
            session.save(p);
        }
    }

    public void update(String database, Person[] person) {

    }

    public void remove(String database, int[] ids) {

    }

    public Person[] find(String database,Condition[] conds) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + database + ";").executeUpdate();
        }catch (Exception ex){
            throw new DatabaseNotFoundException(database);
        }

        Query query = Condition.createQuery("Person",conds,session);

        List<Person> listPerson = query.list();
        Person[] arrPerson = new Person[listPerson.size()];
        listPerson.toArray(arrPerson);
        return arrPerson;
    }
}
