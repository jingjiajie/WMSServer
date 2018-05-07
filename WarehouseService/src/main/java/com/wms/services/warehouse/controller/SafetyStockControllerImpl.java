package com.wms.services.warehouse.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.warehouse.service.SafetyStockService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.SafetyStock;
import com.wms.utilities.model.SafetyStockView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/safety_stock")
public class SafetyStockControllerImpl implements SafetyStockController{
    @Autowired
    SafetyStockService safetyStockService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public int[] add(@PathVariable("accountBook") String accountBook,
                     @RequestBody SafetyStock[] safetyStocks) {
        return safetyStockService.add(accountBook, safetyStocks);
    }

    @Override
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody SafetyStock[] safetyStocks) {
        safetyStockService.update(accountBook, safetyStocks);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strIDs}", method = RequestMethod.DELETE)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs, new TypeToken<int[]>() {
        }.getType());
        safetyStockService.remove(accountBook, ids);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strCond}", method = RequestMethod.GET)
    public SafetyStockView[] find(@PathVariable("accountBook") String accountBook,
                                     @PathVariable("strCond") String condStr) {
        return safetyStockService.find(accountBook, Condition.fromJson(condStr));
    }


}
