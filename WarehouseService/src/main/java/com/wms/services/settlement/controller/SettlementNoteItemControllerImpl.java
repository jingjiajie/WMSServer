package com.wms.services.settlement.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.settlement.service.SettlementNoteItemService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.SettlementNoteItem;
import com.wms.utilities.model.SettlementNoteItemView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/settlement_note_item")
public class SettlementNoteItemControllerImpl implements SettlementNoteItemController{
    @Autowired
    SettlementNoteItemService settlementNoteItemService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public int[] add(@PathVariable("accountBook") String accountBook,
                     @RequestBody SettlementNoteItem[] settlementNoteItems) {
        return settlementNoteItemService.add(accountBook, settlementNoteItems);
    }

    @Override
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody SettlementNoteItem[] settlementNoteItems) {
        settlementNoteItemService.update(accountBook, settlementNoteItems);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strIDs}", method = RequestMethod.DELETE)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs, new TypeToken<int[]>() {
        }.getType());
        settlementNoteItemService.remove(accountBook, ids);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strCond}", method = RequestMethod.GET)
    public SettlementNoteItemView[] find(@PathVariable("accountBook") String accountBook,
                                         @PathVariable("strCond") String condStr) {
        return settlementNoteItemService.find(accountBook, Condition.fromJson(condStr));
    }


    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return this.settlementNoteItemService.findCount(accountBook, Condition.fromJson(condStr));
    }
}
