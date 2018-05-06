package com.wms.services.warehouse.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.utilities.model.StorageArea;
import com.wms.services.warehouse.service.StorageAreaService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.StorageAreaView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/storage_area")
public class StorageAreaControllerImpl implements StorageAreaController{
    @Autowired
    StorageAreaService storageAreaService;

    @RequestMapping(value="/",method = RequestMethod.POST)
    public ResponseEntity<int[]> add(@PathVariable("accountBook") String accountBook,
                                     @RequestBody StorageArea[] storageAreas) {
        int ids[] = storageAreaService.add(accountBook, storageAreas);
        return new ResponseEntity<int[]>(ids, HttpStatus.OK);
    }

    @RequestMapping(value = "/{strIDs}",method = RequestMethod.DELETE)
    @ResponseBody
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs,new TypeToken<int[]>(){}.getType());
        storageAreaService.remove(accountBook,ids);
    }

    @RequestMapping(value = "/",method = RequestMethod.PUT)
    @ResponseBody
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody StorageArea[] storageAreas) {
        storageAreaService.update(accountBook,storageAreas);
    }

    @RequestMapping("/{condStr}")
    public ResponseEntity<StorageAreaView[]> find(@PathVariable("accountBook") String accountBook,
                                                  @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
        StorageAreaView[] storageAreas = storageAreaService.find(accountBook,cond);
        return new ResponseEntity<StorageAreaView[]>(storageAreas, HttpStatus.OK);
    }
}

