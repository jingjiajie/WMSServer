package com.wms.services.warehouse.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
    @Override
    public ResponseEntity<int[]> add(@PathVariable("accountBook") String accountBook,
                                     @RequestBody WarehouseEntry[] warehouseEntries) {
        int[] ids = warehouseEntryService.add(accountBook, warehouseEntries);
        return new ResponseEntity<>(ids, HttpStatus.OK);
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
        int ids[] = gson.fromJson(strIDs, new TypeToken<int[]>() {}.getType());
        warehouseEntryService.remove(accountBook, ids);
    }

    @Override
    @RequestMapping(value = "/{strCond}", method = RequestMethod.GET)
    public ResponseEntity<WarehouseEntryView[]> find(@PathVariable("accountBook") String accountBook,
                                                     @PathVariable("strCond") String condStr) {
        WarehouseEntryView[] results = warehouseEntryService.find(accountBook, Condition.fromJson(condStr));
        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}
