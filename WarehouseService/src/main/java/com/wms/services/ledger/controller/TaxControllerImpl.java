package com.wms.services.ledger.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.ledger.service.TaxService;
import com.wms.utilities.model.Tax;
import com.wms.utilities.datastructures.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/tax")
public class TaxControllerImpl implements TaxController {
    @Autowired
    TaxService taxService;

    @RequestMapping(value = "/",method = RequestMethod.POST)
    public ResponseEntity<int[]> add(@PathVariable("accountBook") String accountBook,
                                     @RequestBody Tax[] taxes) {
        int ids[] = taxService.add(accountBook, taxes);
        return new ResponseEntity<int[]>(ids, HttpStatus.OK);
    }

    @RequestMapping(value = "/{strIDs}",method = RequestMethod.DELETE)
    @ResponseBody
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs,new TypeToken<int[]>(){}.getType());
        taxService.remove(accountBook,ids);
    }

    @RequestMapping(value = "/",method = RequestMethod.PUT)
    @ResponseBody
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody Tax[] taxes) {
        taxService.update(accountBook,taxes);
    }

    @RequestMapping("/{condStr}")
    public ResponseEntity<Tax[]> find(@PathVariable("accountBook") String accountBook,
                                         @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
        Tax[] taxes = taxService.find(accountBook,cond);
        return new ResponseEntity<Tax[]>(taxes, HttpStatus.OK);
    }
}