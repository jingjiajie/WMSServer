package com.wms.services.warehouse.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.warehouse.service.DestinationService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/destination")
public class DestinationControllerImpl implements DestinationController {
    @Autowired
    DestinationService  destinationService;

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value="/",method = RequestMethod.POST)
    public int[] add(@PathVariable("accountBook") String accountBook,
                                     @RequestBody Destination[] destinations) {
        int ids[] = destinationService.add(accountBook, destinations);
        return  ids;
    }

    @Override
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody Destination[] destinations) {
        destinationService.update(accountBook, destinations);
    }

    @Override
    @RequestMapping(value = "/{strIDs}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs, new TypeToken<int[]>() {
        }.getType());
        this.destinationService.remove(accountBook, ids);
    }


    @Override
    @RequestMapping(value = "/{condStr}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public DestinationView[] find(@PathVariable("accountBook") String accountBook,
                                  @PathVariable("condStr") String condStr) {
        return this.destinationService.find(accountBook, Condition.fromJson(condStr));
    }


    @Override
    @RequestMapping(value = "/count/{condStr}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr) {
        return this.destinationService.findCount(accountBook, Condition.fromJson(condStr));
    }
}
