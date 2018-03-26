package com.wms.services.ledger.controller;

import com.wms.services.ledger.model.Person;
import org.springframework.http.ResponseEntity;

public interface PersonController {
    ResponseEntity<int[]> add(String accountBook,Person[] persons);
    void remove(String accountBook,String strIDs);
    void update(String accountBook,Person persons[]);
    ResponseEntity<Person[]> find(String accountBook,String condStr);
}
