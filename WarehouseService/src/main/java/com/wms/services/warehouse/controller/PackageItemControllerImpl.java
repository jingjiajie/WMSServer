package com.wms.services.warehouse.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.warehouse.service.PackageItemService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.PackageItem;
import com.wms.utilities.model.PackageItemView;
import com.wms.utilities.model.WarehouseEntryItem;
import com.wms.utilities.model.WarehouseEntryItemView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/package_item")
public class PackageItemControllerImpl implements PackageItemController {
    @Autowired
    PackageItemService packageItemService;

    @Override
    @RequestMapping(value = "/",method = RequestMethod.POST)
    public ResponseEntity<int[]> add(@PathVariable("accountBook") String accountBook,
                                     @RequestBody PackageItem[] items) {
        return new ResponseEntity<>(this.packageItemService.add(accountBook,items), HttpStatus.OK);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/",method = RequestMethod.PUT)
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody PackageItem[] items) {
        this.packageItemService.update(accountBook,items);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{ids}",method = RequestMethod.DELETE)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("ids") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs, new TypeToken<int[]>() {}.getType());
        this.packageItemService.remove(accountBook,ids);
    }

    @Override
    @RequestMapping(value = "/{cond}",method = RequestMethod.GET)
    public ResponseEntity<PackageItemView[]> find(@PathVariable("accountBook") String accountBook,
                                                         @PathVariable("cond") String strCond) {
        return new ResponseEntity<>(this.packageItemService.find(accountBook, Condition.fromJson(strCond)),HttpStatus.OK);
    }
}
