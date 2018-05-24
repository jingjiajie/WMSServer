package com.wms.services.warehouse.controller;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.warehouse.service.PackageService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.Package;
import com.wms.utilities.model.PackageView;
import com.wms.utilities.model.WarehouseEntryView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/package")
public class PackageControllerImpl implements PackageController {
    @Autowired
    PackageService packageService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public int[] add(@PathVariable("accountBook") String accountBook,
                     @RequestBody Package[] packages) {
        return packageService.add(accountBook, packages);
    }

    @Override
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody Package[] packages) {
        packageService.update(accountBook, packages);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strIDs}", method = RequestMethod.DELETE)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs, new TypeToken<int[]>() {
        }.getType());
        packageService.remove(accountBook, ids);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strCond}", method = RequestMethod.GET)
    public PackageView[] find(@PathVariable("accountBook") String accountBook,
                              @PathVariable("strCond") String condStr) {
        return packageService.find(accountBook, Condition.fromJson(condStr));
    }

    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return packageService.findCount(accountBook, Condition.fromJson(condStr));
    }

}
