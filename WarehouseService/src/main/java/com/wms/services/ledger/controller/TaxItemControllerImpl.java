package com.wms.services.ledger.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.ledger.service.TaxItemService;
import com.wms.utilities.model.TaxItem;
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

    @RequestMapping(value="/",method = RequestMethod.POST)
    public ResponseEntity<int[]> add(@PathVariable("accountBook") String accountBook,
                                     @RequestBody TaxItem[] taxItems) {
        int ids[] = taxItemService.add(accountBook, taxItems);
        return new ResponseEntity<int[]>(ids, HttpStatus.OK);
    }

    @RequestMapping(value = "/{strIDs}",method = RequestMethod.DELETE)
    @ResponseBody
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs,new TypeToken<int[]>(){}.getType());
        taxItemService.remove(accountBook,ids);
    }

    @RequestMapping(value = "/",method = RequestMethod.PUT)
    @ResponseBody
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody TaxItem[] taxItems) {
        taxItemService.update(accountBook,taxItems);
    }

    @RequestMapping("/{condStr}")
    public ResponseEntity<TaxItem[]> find(@PathVariable("accountBook") String accountBook,
                                         @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
        TaxItem[] taxItems = taxItemService.find(accountBook,cond);
        return new ResponseEntity<TaxItem[]>(taxItems, HttpStatus.OK);
    }
}
