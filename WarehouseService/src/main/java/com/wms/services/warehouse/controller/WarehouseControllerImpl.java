package com.wms.services.warehouse.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.warehouse.model.Supplier;
import com.wms.services.warehouse.model.Warehouse;
import com.wms.services.warehouse.service.SupplierServices;
import com.wms.services.warehouse.service.WarehouseService;
import com.wms.utilities.datastructures.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/warehouse")
public class WarehouseControllerImpl implements WarehouseController {
    @Autowired
    WarehouseService warehouseService;
    @RequestMapping(value="/",method = RequestMethod.POST)
    public ResponseEntity<int[]> add(@PathVariable("accountBook") String accountBook,
                                     @RequestBody Warehouse[] warehouses ){
        int ids[] = warehouseService.add(accountBook,warehouses);
        return new ResponseEntity<int[]>(ids, HttpStatus.OK);
    }

    @RequestMapping(value = "/",method = RequestMethod.PUT)
    @ResponseBody
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody Warehouse[] warehouses) {
        warehouseService.update(accountBook,warehouses);
    }
    @RequestMapping(value = "/{strIDs}",method = RequestMethod.DELETE)
    @ResponseBody
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs,new TypeToken<int[]>(){}.getType());
        warehouseService.remove(accountBook,ids);
    }
    @RequestMapping("/{condStr}")
    public ResponseEntity<Warehouse[]> find(@PathVariable("accountBook") String accountBook,
                                           @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
        Warehouse[] warehouses =warehouseService.find(accountBook, cond);
        return new ResponseEntity<Warehouse[]>(warehouses, HttpStatus.OK);
    }

}
