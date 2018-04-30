package com.wms.services.warehouse.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netflix.discovery.converters.Auto;
import com.wms.services.warehouse.service.InspectionNoteService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.InspectionNote;
import com.wms.utilities.model.InspectionNoteView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/inspection_note")
public class InspectionNoteControllerImpl implements InspectionNoteController {
    @Autowired
    InspectionNoteService inspectionNoteService;

    @Override
    @RequestMapping(value = "/",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public int[] add(@PathVariable("accountBook") String accountBook,
                     @RequestBody InspectionNote[] objs) {
        return this.inspectionNoteService.add(accountBook,objs);
    }

    @Override
    @RequestMapping(value = "/{strIDs}",method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs,new TypeToken<int[]>(){}.getType());
        this.inspectionNoteService.remove(accountBook,ids);
    }

    @Override
    @RequestMapping(value = "/",method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody InspectionNote[] objs) {
        this.inspectionNoteService.update(accountBook,objs);
    }

    @Override
    @RequestMapping(value = "/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public InspectionNoteView[] find(@PathVariable("accountBook") String accountBook,
                                     @PathVariable("condStr") String condStr) {
        return this.inspectionNoteService.find(accountBook, Condition.fromJson(condStr));
    }
}
