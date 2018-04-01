package com.wms.services.warehouse.controller;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.warehouse.dao.SupplierDAO;
import com.wms.services.warehouse.model.Supplier;
import com.wms.services.warehouse.service.SupplierServices;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/person")
public class SupplierControllerImpl implements SupplierController {
    @Autowired
    SupplierServices supplierServices;
    @RequestMapping(value="/",method = RequestMethod.POST)
    public ResponseEntity<int[]> add(@PathVariable("accountBook") String accountBook,
                                     @RequestBody Supplier[] suppliers) {
        int ids[] = supplierServices.add(accountBook, suppliers);
        return new ResponseEntity<int[]>(ids, HttpStatus.OK);
    }

    @RequestMapping(value = "/",method = RequestMethod.PUT)
    @ResponseBody
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody Supplier[] suppliers) {
        supplierServices.update(accountBook,suppliers);
    }
    @RequestMapping(value = "/{strIDs}",method = RequestMethod.DELETE)
    @ResponseBody
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs,new TypeToken<int[]>(){}.getType());
        supplierServices.remove(accountBook,ids);
    }
    @RequestMapping("/{condStr}")
    public ResponseEntity<Supplier[]> find(@PathVariable("accountBook") String accountBook,
                                         @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
        Supplier[] suppliers = supplierServices.find(accountBook, cond);
        return new ResponseEntity<Supplier[]>(suppliers, HttpStatus.OK);
    }
}
