package com.wms.services.settlement.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.settlement.datastructures.LedgerSynchronous;
import com.wms.services.settlement.service.SettlementNoteService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/settlement_note")
public class SettlementNoteControllerImpl implements SettlementNoteController{
    @Autowired
    SettlementNoteService settlementNoteService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public int[] add(@PathVariable("accountBook") String accountBook,
                     @RequestBody SettlementNote[] settlementNotes) {
        return settlementNoteService.add(accountBook, settlementNotes);
    }

    @Override
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody SettlementNote[] settlementNotes) {
        settlementNoteService.update(accountBook, settlementNotes);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strIDs}", method = RequestMethod.DELETE)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs, new TypeToken<int[]>() {
        }.getType());
        settlementNoteService.remove(accountBook, ids);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strCond}", method = RequestMethod.GET)
    public SettlementNoteView[] find(@PathVariable("accountBook") String accountBook,
                                  @PathVariable("strCond") String condStr) {
        return settlementNoteService.find(accountBook, Condition.fromJson(condStr));
    }


    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return this.settlementNoteService.findCount(accountBook, Condition.fromJson(condStr));
    }

    @Override
    @RequestMapping(value="/synchronous_receivables",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void synchronousReceivables(@PathVariable("accountBook") String accountBook,
                                       @RequestBody LedgerSynchronous ledgerSynchronous){
        this.settlementNoteService.synchronousReceivables(accountBook, ledgerSynchronous);
    }

    @Override
    @RequestMapping(value="/synchronous_receipt",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void synchronousReceipt(@PathVariable("accountBook") String accountBook,
                                   @RequestBody LedgerSynchronous ledgerSynchronous){
        this.settlementNoteService.synchronousReceipt(accountBook, ledgerSynchronous);
    }
}
