package com.wms.services.warehouse.controller;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.warehouse.datastructures.SupplierAmount;
import com.wms.services.warehouse.service.SupplierServices;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.Supplier;
import com.wms.utilities.model.SupplierView;
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
    @RequestMapping(value = "/history_save",method = RequestMethod.PUT)
    public void updateHistorySave(@PathVariable("accountBook") String accountBook,
                       @RequestBody Supplier[] suppliers) {
        supplierServices.updateHistory(accountBook,suppliers);
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
    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping("/{condStr}/new")
    public ResponseEntity<SupplierView[]> findNew(@PathVariable("accountBook") String accountBook,
                                               @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
        SupplierView[] suppliers = supplierServices.findNew(accountBook, cond);
        return new ResponseEntity<SupplierView[]>(suppliers, HttpStatus.OK);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping("/{condStr}/history")
    public ResponseEntity<SupplierView[]> findHistory(@PathVariable("accountBook") String accountBook,
                                               @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
        SupplierView[] suppliers = supplierServices.findHistory(accountBook, cond);
        return new ResponseEntity<SupplierView[]>(suppliers, HttpStatus.OK);
    }

    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return this.supplierServices.findCount(accountBook, Condition.fromJson(condStr));
    }

    @Override
    @RequestMapping(value="/count/{condStr}/new",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCountNew(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return this.supplierServices.findCountNew(accountBook, Condition.fromJson(condStr));
    }

    @Override
    @RequestMapping(value="/count/{condStr}/history",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCountHistory(@PathVariable("accountBook") String accountBook,
                             @PathVariable("condStr") String condStr){
        return this.supplierServices.findCountHistory(accountBook, Condition.fromJson(condStr));
    }

    @Override
    @RequestMapping(value="/remind/{supplierId}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public SupplierAmount[] supplierRemind(@PathVariable("accountBook") String accountBook,
                                         @PathVariable("supplierId") String id){
        int supplierId=Integer.valueOf(id.substring(1,id.length()-1));
        return this.supplierServices.supplierRemind(accountBook, supplierId);
    }
}
