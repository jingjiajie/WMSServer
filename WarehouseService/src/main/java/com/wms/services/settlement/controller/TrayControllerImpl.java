package com.wms.services.settlement.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.settlement.datastructures.ValidateTray;
import com.wms.services.settlement.service.TrayService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.CommonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/tray")
public class TrayControllerImpl implements TrayController{
    @Autowired
    TrayService trayService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public int[] add(@PathVariable("accountBook") String accountBook,
                     @RequestBody CommonData[] commonData) {
        return trayService.add(accountBook, commonData);
    }

    @Override
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody CommonData[] commonData) {
        trayService.update(accountBook, commonData);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strIDs}", method = RequestMethod.DELETE)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs, new TypeToken<int[]>() {
        }.getType());
        trayService.remove(accountBook, ids);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strCond}", method = RequestMethod.GET)
    public CommonData[] find(@PathVariable("accountBook") String accountBook,
                                     @PathVariable("strCond") String condStr) {
        return trayService.find(accountBook, Condition.fromJson(condStr));
    }


    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return this.trayService.findCount(accountBook, Condition.fromJson(condStr));
    }

    @RequestMapping(value = "/validate_tray", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public void validateEntities(@PathVariable("accountBook") String accountBook,
                                      @RequestBody ValidateTray validateTray) {
        trayService.validateEntities(accountBook, validateTray);
    }
}
