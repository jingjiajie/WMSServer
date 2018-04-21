package com.wms.services.warehouse.controller;

import com.wms.services.warehouse.model.Material;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.warehouse.service.MaterialService;
import com.wms.utilities.datastructures.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/material")
public class MaterialControllerImpl implements MaterialController{
    @Autowired
    MaterialService materialService;

    @RequestMapping(value="/",method = RequestMethod.POST)
    public ResponseEntity<int[]> add(@PathVariable("accountBook") String accountBook,
                                     @RequestBody Material[] materials) {
        int ids[] = materialService.add(accountBook, materials);
        return new ResponseEntity<int[]>(ids, HttpStatus.OK);
    }

    @RequestMapping(value = "/{strIDs}",method = RequestMethod.DELETE)
    @ResponseBody
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs,new TypeToken<int[]>(){}.getType());
        materialService.remove(accountBook,ids);
    }

    @RequestMapping(value = "/",method = RequestMethod.PUT)
    @ResponseBody
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody Material[] materials) {
        materialService.update(accountBook,materials);
    }

    @RequestMapping("/{condStr}")
    public ResponseEntity<Material[]> find(@PathVariable("accountBook") String accountBook,
                                         @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
        Material[] materials = materialService.find(accountBook,cond);
        return new ResponseEntity<Material[]>(materials, HttpStatus.OK);
    }

}
