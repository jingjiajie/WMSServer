package com.wms.services.ledger.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.Person;
import com.wms.utilities.model.PersonView;
import org.springframework.stereotype.Repository;

@Repository
public class PersonDAOImpl
        extends BaseDAOImpl<Person,PersonView>
        implements PersonDAO {

    public PersonDAOImpl(){
        super(Person.class,PersonView.class,Person::getId);
    }
}