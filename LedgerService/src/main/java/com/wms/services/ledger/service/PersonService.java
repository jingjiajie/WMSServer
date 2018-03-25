package com.wms.services.ledger.service;

import com.wms.services.ledger.model.Person;
import com.wms.utilities.datastructures.Condition;

import java.util.Map;

public interface PersonService {
    void add(String accountBook, Person person[]);
    void update(String accountBook,Person person[]);
    void remove(String accountBook,int[] ids);
    Person[] find(String accountBook,Condition[] conds);
}
