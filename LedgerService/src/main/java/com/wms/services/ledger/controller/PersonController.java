package com.wms.services.ledger.controller;

import com.wms.services.ledger.model.Person;
import org.springframework.http.ResponseEntity;

public interface PersonController {
    ResponseEntity<Person[]> find(String accountBook,String condStr);
}
