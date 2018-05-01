package com.wms.services.warehouse.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.warehouse.service.InspectionNoteItemService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.InspectionNoteItem;
import com.wms.utilities.model.InspectionNoteItemView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/inspection_note_item")
public class InspectionNoteItemControllerImpl
    implements InspectionNoteItemController{

    @Autowired
    InspectionNoteItemService inspectionNoteItemService;

    @Override
    @RequestMapping(value = "/{strIDs}",method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs,new TypeToken<int[]>(){}.getType());
        this.inspectionNoteItemService.remove(accountBook,ids);
    }

    @Override
    @RequestMapping(value = "/",method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("accountBook")String accountBook,
                       @RequestBody InspectionNoteItem[] objs) {
        this.inspectionNoteItemService.update(accountBook,objs);
    }

    @Override
    @RequestMapping(value = "/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public InspectionNoteItemView[] find(@PathVariable("accountBook") String accountBook,
                                         @PathVariable("condStr") String condStr) {
        return this.inspectionNoteItemService.find(accountBook, Condition.fromJson(condStr));
    }
}
