package com.wms.services.settlement.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.settlement.datastructures.AddAllItem;
import com.wms.services.settlement.service.SummaryNoteService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/summary_note")
public class SummaryNoteControllerImpl implements SummaryNoteController {
    @Autowired
    SummaryNoteService summaryNoteService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public int[] add(@PathVariable("accountBook") String accountBook,
                     @RequestBody SummaryNote[] summaryNotes) {
        return summaryNoteService.add(accountBook, summaryNotes);
    }

    @Override
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody SummaryNote[] summaryNotes) {
        summaryNoteService.update(accountBook, summaryNotes);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strIDs}", method = RequestMethod.DELETE)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs, new TypeToken<int[]>() {
        }.getType());
        summaryNoteService.remove(accountBook, ids);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strCond}", method = RequestMethod.GET)
    public SummaryNoteView[] find(@PathVariable("accountBook") String accountBook,
                                  @PathVariable("strCond") String condStr) {
        return summaryNoteService.find(accountBook, Condition.fromJson(condStr));
    }


    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return this.summaryNoteService.findCount(accountBook, Condition.fromJson(condStr));
    }


    @Override
    @RequestMapping(value = "/generate_summary", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void generateSummaryNotes(@PathVariable("accountBook") String accountBook,
                                     @RequestBody AddAllItem addAllItem) {
        this.summaryNoteService.generateSummaryNotes(accountBook, addAllItem.getWarehouseId(), addAllItem.getSummaryNoteId());
    }

}
