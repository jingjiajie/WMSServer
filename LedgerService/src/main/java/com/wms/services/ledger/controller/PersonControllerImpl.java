package com.wms.services.ledger.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.ledger.model.Person;
import com.wms.services.ledger.service.PersonService;
import com.wms.utilities.datastructures.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PersonControllerImpl implements PersonController {
    @Autowired
    PersonService personService;

    @RequestMapping("/{accountBook}/person/{condStr}")
    public ResponseEntity<Person[]> find(@PathVariable("accountBook") String accountBook,
                                         @PathVariable("condStr") String condStr) {
        Condition[] conds = Condition.fromJson(condStr);
        Person[] persons = personService.find(accountBook,conds);
        return new ResponseEntity<Person[]>(persons, HttpStatus.OK);
    }
}
