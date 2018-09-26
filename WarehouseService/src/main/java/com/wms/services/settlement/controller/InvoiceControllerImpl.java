package com.wms.services.settlement.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.settlement.service.InvoiceService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.Invoice;
import com.wms.utilities.model.InvoiceView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/{accountBook}/invoice")
public class InvoiceControllerImpl implements InvoiceController{
    @Autowired
    InvoiceService invoiceService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public int[] add(@PathVariable("accountBook") String accountBook,
                     @RequestBody Invoice[] invoices) {
        return invoiceService.add(accountBook, invoices);
    }

    @Override
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody Invoice[] invoices) {
        invoiceService.update(accountBook, invoices);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strIDs}", method = RequestMethod.DELETE)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs, new TypeToken<int[]>() {
        }.getType());
        invoiceService.remove(accountBook, ids);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strCond}", method = RequestMethod.GET)
    public InvoiceView[] find(@PathVariable("accountBook") String accountBook,
                              @PathVariable("strCond") String condStr) {
        return invoiceService.find(accountBook, Condition.fromJson(condStr));
    }


    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return this.invoiceService.findCount(accountBook, Condition.fromJson(condStr));
    }

    @Override
    @RequestMapping(value = "/confirm/{id}",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void confirm(@PathVariable("accountBook") String accountBook,
                        @PathVariable("id") int id) {
        List<Integer> ids=new ArrayList<>();
        ids.add(id);
        this.invoiceService.confirm(accountBook,ids);
    }
}
