package com.wms.utilities.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.*;
import com.wms.utilities.service.CommonDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/{accountBook}/common_data")
public class CommonDataControllerImpl implements CommonDateController {
    @Autowired
    CommonDataService commonDataService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public int[] add(@PathVariable("accountBook") String accountBook,
                     @RequestBody CommonData[] commonData) {
        return commonDataService.add(accountBook, commonData);
    }

    @Override
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody CommonData[] commonData) {
        commonDataService.update(accountBook, commonData);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strIDs}", method = RequestMethod.DELETE)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs, new TypeToken<int[]>() {
        }.getType());
        commonDataService.remove(accountBook, ids);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strCond}", method = RequestMethod.GET)
    public CommonData[] find(@PathVariable("accountBook") String accountBook,
                                    @PathVariable("strCond") String condStr) {
        return commonDataService.find(accountBook, Condition.fromJson(condStr));
    }

    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return this.commonDataService.findCount(accountBook, Condition.fromJson(condStr));
    }
}
