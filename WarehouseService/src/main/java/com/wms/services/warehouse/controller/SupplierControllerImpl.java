package com.wms.services.warehouse.controller;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.warehouse.model.Supplier;
import com.wms.services.warehouse.service.SupplierServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
