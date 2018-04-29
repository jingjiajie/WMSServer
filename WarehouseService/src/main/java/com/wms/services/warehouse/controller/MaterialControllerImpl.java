package com.wms.services.warehouse.controller;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.utilities.model.Material;
import com.wms.utilities.model.MaterialView;
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

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value="/",method = RequestMethod.POST)
    public ResponseEntity<int[]> add(@PathVariable("accountBook") String accountBook,
                                     @RequestBody Material[] materials) {
        int ids[] = materialService.add(accountBook, materials);
        return new ResponseEntity<int[]>(ids, HttpStatus.OK);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/",method = RequestMethod.PUT)
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody Material[] materials) {
        materialService.update(accountBook,materials);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strIDs}",method = RequestMethod.DELETE)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs,new TypeToken<int[]>(){}.getType());
        materialService.remove(accountBook,ids);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping("/{condStr}")
    public ResponseEntity<MaterialView[]> find(@PathVariable("accountBook") String accountBook,
                                               @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
        MaterialView[] materials = materialService.find(accountBook, cond);
        return new ResponseEntity<MaterialView[]>(materials, HttpStatus.OK);
    }

}
