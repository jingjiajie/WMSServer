package com.wms.services.warehouse.controller;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.utilities.model.Supplier;
import com.wms.utilities.model.SupplierView;
import com.wms.services.warehouse.service.SupplierServices;
import com.wms.utilities.datastructures.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/supplier")
public class SupplierControllerImpl implements SupplierController {
    @Autowired
    SupplierServices supplierServices;

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value="/",method = RequestMethod.POST)
    public ResponseEntity<int[]> add(@PathVariable("accountBook") String accountBook,
                                     @RequestBody Supplier[] suppliers) {
        int ids[] = supplierServices.add(accountBook, suppliers);
        return new ResponseEntity<int[]>(ids, HttpStatus.OK);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/",method = RequestMethod.PUT)
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody Supplier[] suppliers) {
        supplierServices.update(accountBook,suppliers);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strIDs}",method = RequestMethod.DELETE)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs,new TypeToken<int[]>(){}.getType());
        supplierServices.remove(accountBook,ids);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping("/{condStr}")
    public ResponseEntity<SupplierView[]> find(@PathVariable("accountBook") String accountBook,
                                           @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
        SupplierView[] suppliers = supplierServices.find(accountBook, cond);
        return new ResponseEntity<SupplierView[]>(suppliers, HttpStatus.OK);
    }
}
