package com.wms.services.warehouse.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.warehouse.datastructures.InspectArgs;
import com.wms.services.warehouse.datastructures.WarehouseEntryAndItems;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.services.warehouse.service.WarehouseEntryService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.InspectionNoteItem;
import com.wms.utilities.model.WarehouseEntry;
import com.wms.utilities.model.WarehouseEntryView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/{accountBook}/warehouse_entry")
public class WarehouseEntryControllerImpl implements WarehouseEntryController {
    @Autowired
    WarehouseEntryService warehouseEntryService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public int[] add(@PathVariable("accountBook") String accountBook,
                     @RequestBody WarehouseEntry[] warehouseEntries) {
        return warehouseEntryService.add(accountBook, warehouseEntries);
    }

    @Override
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody WarehouseEntry[] warehouseEntries) {
        warehouseEntryService.update(accountBook, warehouseEntries);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strIDs}", method = RequestMethod.DELETE)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs, new TypeToken<int[]>() {
        }.getType());
        warehouseEntryService.remove(accountBook, ids);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strCond}", method = RequestMethod.GET)
    public WarehouseEntryView[] find(@PathVariable("accountBook") String accountBook,
                                     @PathVariable("strCond") String condStr) {
        return warehouseEntryService.find(accountBook, Condition.fromJson(condStr));
    }

    @Override
    @RequestMapping(value = "/inspect", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> inspect(@PathVariable("accountBook") String accountBook,
                                 @RequestBody InspectArgs inspectArgs) {
        if (inspectArgs.getInspectItems().length != 1) {
            return new ArrayList<>();
        }
        for (InspectionNoteItem inspectionNoteItem : inspectArgs.getInspectItems()[0].getInspectionNoteItems())
        {
            if (inspectionNoteItem.getVersion().equals(inspectArgs.getInspectItems()[0].getInspectionNote().getVersion())) {
                throw new WMSServiceException("入库单和入库单条目不同！");
            }
        }
        if (inspectArgs.getInspectItems()[0].getInspectionNote().getVersion() == 0) {
            return this.warehouseEntryService.inspect(accountBook, inspectArgs);
        }
        else{
            return this.warehouseEntryService.inspect1(accountBook, inspectArgs);
        }
    }

    @Override
    @RequestMapping(value = "/update_state", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateState(
            @PathVariable("accountBook") String accountBook,
            @RequestBody List<Integer> ids) throws WMSServiceException {
        this.warehouseEntryService.updateState(accountBook, ids);
    }

    @Override
    @RequestMapping(value = "/count/{condStr}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr) {
        return warehouseEntryService.findCount(accountBook, Condition.fromJson(condStr));
    }

    @Override
    @RequestMapping(value = "/preview/{strIDs}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<WarehouseEntryAndItems> getPreviewData(@PathVariable("accountBook") String accountBook,
                                                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        List<Integer> ids = gson.fromJson(strIDs, new TypeToken<List<Integer>>() {
        }.getType());
        return warehouseEntryService.getPreviewData(accountBook, ids);
    }

    @Override
    @RequestMapping(value = "/receive", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void receive(@PathVariable("accountBook") String accountBook,
                        @RequestBody List<Integer> ids) {
        //在putin区分了
        this.warehouseEntryService.receive(accountBook, ids);
    }

    @Override
    @RequestMapping(value = "/reject", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void reject(@PathVariable("accountBook") String accountBook,
                       @RequestBody List<Integer> ids) {
        //在putin区分了
        this.warehouseEntryService.reject(accountBook, ids);
    }
}
