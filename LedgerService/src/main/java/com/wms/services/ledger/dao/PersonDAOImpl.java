package com.wms.services.ledger.dao;

import com.wms.services.ledger.model.Person;
import com.wms.utilities.dao.BaseDAO;
import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.model.PersonView;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public class PersonDAOImpl
        extends BaseDAOImpl<Person,PersonView>
        implements PersonDAO {

    public PersonDAOImpl(){
        super(Person.class,PersonView.class,Person::getId);
    }
}