package com.wms.services.warehouse.controller;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.warehouse.model.Supply;
import com.wms.services.warehouse.service.SupplyService;
import com.wms.utilities.datastructures.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/person")
public class SupplyControllerImpl implements SupplyController {
    @Autowired
    SupplyService supplyService;
    @RequestMapping(value="/",method = RequestMethod.POST)
    public ResponseEntity<int[]> add(@PathVariable("accountBook") String accountBook,
                                     @RequestBody Supply[] supplies) {
        int ids[] = supplyService.add(accountBook, supplies);
        return new ResponseEntity<int[]>(ids, HttpStatus.OK);
    }

    @RequestMapping(value = "/",method = RequestMethod.PUT)
    @ResponseBody
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody Supply[] supplies) {
        supplyService.update(accountBook,supplies);
    }
    @RequestMapping(value = "/{strIDs}",method = RequestMethod.DELETE)
    @ResponseBody
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs,new TypeToken<int[]>(){}.getType());
        supplyService.remove(accountBook,ids);
    }
    @RequestMapping("/{condStr}")
    public ResponseEntity<Supply[]> find(@PathVariable("accountBook") String accountBook,
                                           @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
        Supply[] supplies = supplyService.find(accountBook, cond);
        return new ResponseEntity<Supply[]>(supplies, HttpStatus.OK);
    }

}
