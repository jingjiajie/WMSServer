package com.wms.services.warehouse.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.utilities.model.WarehouseEntryItem;
import com.wms.utilities.model.WarehouseEntryItemView;
import com.wms.services.warehouse.service.WarehouseEntryItemService;
import com.wms.utilities.datastructures.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/warehouse_entry_item")
public class WarehouseEntryItemControllerImpl implements WarehouseEntryItemController {
    @Autowired
    WarehouseEntryItemService warehouseEntryItemService;

    @Override
    @RequestMapping(value = "/",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public int[] add(@PathVariable("accountBook") String accountBook,
                                     @RequestBody WarehouseEntryItem[] items) {
        return this.warehouseEntryItemService.add(accountBook,items);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/",method = RequestMethod.PUT)
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody WarehouseEntryItem[] items) {
        this.warehouseEntryItemService.update(accountBook,items);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{ids}",method = RequestMethod.DELETE)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("ids") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs, new TypeToken<int[]>() {}.getType());
        this.warehouseEntryItemService.remove(accountBook,ids);
    }

    @Override
    @RequestMapping(value = "/{cond}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public WarehouseEntryItemView[] find(@PathVariable("accountBook") String accountBook,
                                                         @PathVariable("cond") String strCond) {
        return this.warehouseEntryItemService.find(accountBook, Condition.fromJson(strCond));
    }

    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return warehouseEntryItemService.findCount(accountBook, Condition.fromJson(condStr));
    }
}
