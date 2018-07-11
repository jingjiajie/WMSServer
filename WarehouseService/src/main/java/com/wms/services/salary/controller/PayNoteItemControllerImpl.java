package com.wms.services.salary.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.salary.datestructures.CalculateTax;
import com.wms.services.salary.service.PayNoteItemService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.PayNoteItem;
import com.wms.utilities.model.PayNoteItemView;
import com.wms.utilities.model.SalaryItem;
import com.wms.utilities.model.SalaryItemView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/pay_note_item")
public class PayNoteItemControllerImpl implements PayNoteItemController {
    @Autowired
    PayNoteItemService payNoteItemService;

    @RequestMapping(value="/",method = RequestMethod.POST)
    public int[] add(@PathVariable("accountBook") String accountBook,
                     @RequestBody PayNoteItem[] payNoteItems){
        return payNoteItemService.add(accountBook,payNoteItems);
    }

    @RequestMapping(value = "/",method = RequestMethod.PUT)
    @ResponseBody
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody PayNoteItem[] payNoteItems) {
        payNoteItemService.update(accountBook,payNoteItems);
    }

    @RequestMapping(value = "/{strIDs}",method = RequestMethod.DELETE)
    @ResponseBody
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs,new TypeToken<int[]>(){}.getType());
        payNoteItemService.remove(accountBook,ids);
    }

    @RequestMapping(value = "/{condStr}",method = RequestMethod.GET)
    public PayNoteItemView[] find(@PathVariable("accountBook") String accountBook,
                                  @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
        PayNoteItemView[] payNoteItemViews =payNoteItemService.find(accountBook, cond);
        return payNoteItemViews;
    }

    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return this.payNoteItemService.findCount(accountBook, Condition.fromJson(condStr));
    }

    @Override
    @RequestMapping(value="/calculate_tax",method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void calculateTax(@PathVariable("accountBook") String accountBook,
                             @RequestBody CalculateTax calculateTax){
         this.payNoteItemService.calculateTax(accountBook,calculateTax);
    }
/*
    @Override
    @RequestMapping(value="/confirm_items",method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void confirmItems(@PathVariable("accountBook") String accountBook,
                             @RequestBody CalculateTax calculateTax){
        this.payNoteItemService.confirmItems(accountBook,calculateTax);
    }

    @Override
    @RequestMapping(value="/real_pay",method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void realPay(@PathVariable("accountBook") String accountBook,
                             @RequestBody CalculateTax calculateTax){
        this.payNoteItemService.realPay(accountBook,calculateTax);
    }
*/
}