package com.wms.services.ledger.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.ledger.service.TaxItemService;
import com.wms.utilities.model.TaxItem;
import com.wms.utilities.model.TaxItemView;
import com.wms.utilities.datastructures.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/taxitem")
public class TaxItemControllerImpl implements TaxItemController {
    @Autowired
    TaxItemService taxItemService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public int[] add(@PathVariable("accountBook") String accountBook,
                     @RequestBody TaxItem[] taxItems) {
        return taxItemService.add(accountBook, taxItems);
    }

    @Override
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody TaxItem[] taxItems) {
        taxItemService.update(accountBook, taxItems);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strIDs}", method = RequestMethod.DELETE)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs, new TypeToken<int[]>() {
        }.getType());
        taxItemService.remove(accountBook, ids);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strCond}", method = RequestMethod.GET)
    public TaxItemView[] find(@PathVariable("accountBook") String accountBook,
                                    @PathVariable("strCond") String condStr) {
        return taxItemService.find(accountBook, Condition.fromJson(condStr));
    }

    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return this.taxItemService.findCount(accountBook, Condition.fromJson(condStr));
    }
}
