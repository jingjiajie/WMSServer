package com.wms.services.warehouse.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.warehouse.datastructures.InspectFinishArgs;
import com.wms.services.warehouse.datastructures.InspectionNoteAndItems;
import com.wms.services.warehouse.service.InspectionNoteService;
import com.wms.services.warehouse.service.WarehouseEntryService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.InspectionNote;
import com.wms.utilities.model.InspectionNoteView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/{accountBook}/inspection_note")
public class InspectionNoteControllerImpl implements InspectionNoteController {
    @Autowired
    InspectionNoteService inspectionNoteService;
    @Autowired
    WarehouseEntryService warehouseEntryService;

    @Override
    @RequestMapping(value = "/{strIDs}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs, new TypeToken<int[]>() {
        }.getType());
        this.inspectionNoteService.remove(accountBook, ids);
    }

    @Override
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody InspectionNote[] objs) {
        this.inspectionNoteService.update(accountBook, objs);
    }

    @Override
    @RequestMapping(value = "/{condStr}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public InspectionNoteView[] find(@PathVariable("accountBook") String accountBook,
                                     @PathVariable("condStr") String condStr) {
        return this.inspectionNoteService.find(accountBook, Condition.fromJson(condStr));
    }

    @Override
    @RequestMapping(value = "/inspect_finish", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void inspectFinish(@PathVariable("accountBook") String accountBook,
                              @RequestBody InspectFinishArgs inspectFinishArgs) {
        if(inspectFinishArgs.getVersion()==0){
        this.inspectionNoteService.inspectFinish(accountBook, inspectFinishArgs);}
        else{this.inspectionNoteService.inspectFinish1(accountBook, inspectFinishArgs);}
        List<Integer> warehouseEntryIDs = new ArrayList<>();
        List<Integer> inspectionNoteIDs = new ArrayList<>();
        inspectionNoteIDs.add(inspectFinishArgs.getInspectionNoteId());
        warehouseEntryIDs.add(inspectFinishArgs.getWarehouseEntryId());
        this.inspectionNoteService.updateState(accountBook, inspectionNoteIDs);
        this.warehouseEntryService.updateState(accountBook, warehouseEntryIDs);
    }

    @Override
    @RequestMapping(value = "/count/{condStr}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr) {
        return this.inspectionNoteService.findCount(accountBook, Condition.fromJson(condStr));
    }

    @Override
    @RequestMapping(value = "/update_state", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateState(
            @PathVariable("accountBook") String accountBook,
            @RequestBody List<Integer> ids) throws WMSServiceException {
        this.inspectionNoteService.updateState(accountBook, ids);
    }

    @Override
    @RequestMapping(value = "/preview/{strIDs}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<InspectionNoteAndItems> getPreviewData(@PathVariable("accountBook") String accountBook,
                                                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        List<Integer> ids = gson.fromJson(strIDs, new TypeToken<List<Integer>>() {
        }.getType());
        return inspectionNoteService.getPreviewData(accountBook, ids, false);
    }

    @Override
    @RequestMapping(value = "/preview/qualified/{strIDs}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<InspectionNoteAndItems> getPreviewDataQualifiedOnly(@PathVariable("accountBook") String accountBook,
                                                                    @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        List<Integer> ids = gson.fromJson(strIDs, new TypeToken<List<Integer>>() {
        }.getType());
        return inspectionNoteService.getPreviewData(accountBook, ids, true);
    }

}
