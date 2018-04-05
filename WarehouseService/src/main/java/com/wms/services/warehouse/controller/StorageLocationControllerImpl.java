package com.wms.services.warehouse.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.warehouse.model.StorageLocation;
import com.wms.services.warehouse.service.StorageLocationService;
import com.wms.utilities.datastructures.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/storageLocation")
public class StorageLocationControllerImpl implements StorageLocationController{
    @Autowired
    StorageLocationService storageLocationService;
    @RequestMapping(value="/",method = RequestMethod.POST)
    public ResponseEntity<int[]> add(@PathVariable("accountBook") String accountBook,
                                     @RequestBody StorageLocation[] storageLocations) {
        int ids[] = storageLocationService.add(accountBook, storageLocations);
        return new ResponseEntity<int[]>(ids, HttpStatus.OK);
    }
    @RequestMapping(value = "/",method = RequestMethod.PUT)
    @ResponseBody
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody StorageLocation[] storageLocations) {
        storageLocationService.update(accountBook,storageLocations);
    }
    @RequestMapping(value = "/{strIDs}",method = RequestMethod.DELETE)
    @ResponseBody
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs,new TypeToken<int[]>(){}.getType());
       storageLocationService.remove(accountBook,ids);
    }
    @RequestMapping("/{condStr}")
    public ResponseEntity<StorageLocation[]> find(@PathVariable("accountBook") String accountBook,
                                           @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
       StorageLocation[] storageLocations = storageLocationService.find(accountBook, cond);
        return new ResponseEntity<StorageLocation[]>(storageLocations, HttpStatus.OK);
    }
}

