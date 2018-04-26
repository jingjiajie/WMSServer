package com.wms.services.warehouse.controller;

import com.wms.services.warehouse.model.Person;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("ledger-service")
@RequestMapping("/{accountBook}/person")
public interface PersonController {
    @RequestMapping(value = "/", method = RequestMethod.POST)
    ResponseEntity<int[]> add(@PathVariable("accountBook") String accountBook,
                              @RequestBody Person[] persons);

    @RequestMapping(value = "/{strIDs}", method = RequestMethod.DELETE)
    void remove(@PathVariable("accountBook") String accountBook,
                @PathVariable("strIDs") String strIDs);

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    void update(@PathVariable("accountBook") String accountBook,
                @RequestBody Person persons[]);

    @RequestMapping(value = "/{condStr}", method = RequestMethod.GET)
    Person[] find(@PathVariable("accountBook") String accountBook,
                  @PathVariable("condStr") String condStr);
}
