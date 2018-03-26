package com.wms.services.ledger.service;

import com.wms.services.ledger.model.Person;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;

import java.util.Map;

public interface PersonService {
    int[] add(String accountBook, Person persons[]) throws WMSServiceException;
    void update(String accountBook,Person persons[]) throws WMSServiceException;
    void remove(String accountBook,int ids[]) throws WMSServiceException;
    Person[] find(String accountBook,Condition cond) throws WMSServiceException;
}
