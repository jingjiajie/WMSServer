package com.wms.services.warehouse.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netflix.discovery.converters.Auto;
import com.wms.services.warehouse.datastructures.InspectArgs;
import com.wms.utilities.OrderNoGenerator;
import com.wms.utilities.model.WarehouseEntry;
import com.wms.utilities.model.WarehouseEntryView;
import com.wms.services.warehouse.service.WarehouseEntryService;
import com.wms.utilities.datastructures.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/warehouse_entry")
public class WarehouseEntryControllerImpl implements WarehouseEntryController {
    @Autowired
    WarehouseEntryService warehouseEntryService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public int[] add(@PathVariable("accountBook") String accountBook,
                                     @RequestBody WarehouseEntry[] warehouseEntries) {
        return warehouseEntryService.add(accountBook, warehouseEntries);
    }

    @Override
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody WarehouseEntry[] warehouseEntries) {
        warehouseEntryService.update(accountBook, warehouseEntries);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strIDs}", method = RequestMethod.DELETE)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs, new TypeToken<int[]>() {
        }.getType());
        warehouseEntryService.remove(accountBook, ids);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strCond}", method = RequestMethod.GET)
    public WarehouseEntryView[] find(@PathVariable("accountBook") String accountBook,
                                                     @PathVariable("strCond") String condStr) {
        return warehouseEntryService.find(accountBook, Condition.fromJson(condStr));
    }

    @Override
    @RequestMapping(value = "/inspect",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void inspect(@PathVariable("accountBook") String accountBook,
                        @RequestBody InspectArgs inspectArgs){
        this.warehouseEntryService.inspect(accountBook,inspectArgs);
    }
}
