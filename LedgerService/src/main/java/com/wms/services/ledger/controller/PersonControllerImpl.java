package com.wms.services.ledger.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.utilities.model.Person;
import com.wms.services.ledger.service.PersonService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.PersonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/person")
public class PersonControllerImpl implements PersonController {
    @Autowired
    PersonService personService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<int[]> add(@PathVariable("accountBook") String accountBook,
                                     @RequestBody Person[] persons) {
        int ids[] = personService.add(accountBook, persons);
        return new ResponseEntity<>(ids, HttpStatus.OK);
    }

    @RequestMapping(value = "/{strIDs}", method = RequestMethod.DELETE)
    @ResponseBody
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs, new TypeToken<int[]>() {
        }.getType());
        personService.remove(accountBook, ids);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseBody
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody Person[] persons) {
        personService.update(accountBook, persons);
    }

    @RequestMapping(value = "/{condStr}", method = RequestMethod.GET)
    public ResponseEntity<PersonView[]> find(@PathVariable("accountBook") String accountBook,
                                         @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
        PersonView[] persons = personService.find(accountBook, cond);
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }
}
