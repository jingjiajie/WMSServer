package com.wms.services.ledger.service;


import com.wms.services.ledger.dao.PersonDAO;
import com.wms.services.ledger.model.Person;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class PersonServiceImpl implements PersonService {
    @Autowired
    PersonDAO personDAO;

    @Transactional
    public void add(String accountBook, Person[] person) {
        personDAO.add(accountBook,person);
    }

    public void update(String accountBook, Person[] person) {

    }

    public void remove(String accountBook, int[] ids) {

    }

    @Transactional
    public Person[] find(String accountBook, Condition[] conds) {
        try {
            return this.personDAO.find(accountBook, conds);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }
}
