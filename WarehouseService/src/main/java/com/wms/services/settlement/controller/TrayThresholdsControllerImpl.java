package com.wms.services.settlement.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.settlement.service.TrayThresholdsService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.TrayThresholds;
import com.wms.utilities.model.TrayThresholdsView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/tray_thresholds")
public class TrayThresholdsControllerImpl implements TrayThresholdsController{
    @Autowired
    TrayThresholdsService trayThresholdsService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public int[] add(@PathVariable("accountBook") String accountBook,
                     @RequestBody TrayThresholds[] trayThresholds) {
        return trayThresholdsService.add(accountBook, trayThresholds);
    }

    @Override
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody TrayThresholds[] trayThresholds) {
        trayThresholdsService.update(accountBook, trayThresholds);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strIDs}", method = RequestMethod.DELETE)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs, new TypeToken<int[]>() {
        }.getType());
        trayThresholdsService.remove(accountBook, ids);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strCond}", method = RequestMethod.GET)
    public TrayThresholdsView[] find(@PathVariable("accountBook") String accountBook,
                                     @PathVariable("strCond") String condStr) {
        return trayThresholdsService.find(accountBook, Condition.fromJson(condStr));
    }


    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return this.trayThresholdsService.findCount(accountBook, Condition.fromJson(condStr));
    }

}
