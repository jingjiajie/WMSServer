package com.wms.services.warehouse.controller;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.utilities.model.Supply;
import com.wms.utilities.model.SupplyView;
import com.wms.services.warehouse.service.SupplyService;
import com.wms.utilities.datastructures.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/supply")
public class SupplyControllerImpl implements SupplyController {
    @Autowired
    SupplyService supplyService;
    @RequestMapping(value="/",method = RequestMethod.POST)
    public ResponseEntity<int[]> add(@PathVariable("accountBook") String accountBook,
                                     @RequestBody Supply[] supplies) {
        int ids[] = supplyService.add(accountBook, supplies);
        return new ResponseEntity<int[]>(ids, HttpStatus.OK);
    }

    @RequestMapping(value = "/",method = RequestMethod.PUT)
    @ResponseBody
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody Supply[] supplies) {
        supplyService.update(accountBook,supplies);
    }
    @RequestMapping(value = "/{strIDs}",method = RequestMethod.DELETE)
    @ResponseBody
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs,new TypeToken<int[]>(){}.getType());
        supplyService.remove(accountBook,ids);
    }
    @RequestMapping("/{condStr}")
    public ResponseEntity<SupplyView[]> find(@PathVariable("accountBook") String accountBook,
                                           @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
        SupplyView[] supplies = supplyService.find(accountBook, cond);
        return new ResponseEntity<SupplyView[]>(supplies, HttpStatus.OK);
    }
    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return this.supplyService.findCount(accountBook, Condition.fromJson(condStr));
    }

}
