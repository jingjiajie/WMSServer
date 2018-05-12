package com.wms.services.ledger.controller;

import com.wms.utilities.model.Person;
import com.wms.utilities.model.PersonView;
import org.springframework.http.ResponseEntity;

public interface PersonController {
    ResponseEntity<int[]> add(String accountBook,Person[] persons);
    void remove(String accountBook,String strIDs);
    void update(String accountBook,Person persons[]);
    ResponseEntity<PersonView[]> find(String accountBook, String condStr);
}
