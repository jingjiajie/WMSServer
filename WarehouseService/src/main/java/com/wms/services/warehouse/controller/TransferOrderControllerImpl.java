package com.wms.services.warehouse.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.warehouse.datastructures.InspectFinishArgs;
import com.wms.services.warehouse.service.TransferOrderService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.TransferOrder;
import com.wms.utilities.model.TransferOrderView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/transfer_order")
public class TransferOrderControllerImpl implements  TransferOrderController{
    @Autowired
    TransferOrderService transferOrderService;

    @Override
    @RequestMapping(value = "/{strIDs}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs, new TypeToken<int[]>() {
        }.getType());
        this.transferOrderService.remove(accountBook, ids);
    }

    @Override
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody TransferOrder[] objs) {
        this.transferOrderService.update(accountBook, objs);
    }

    @Override
    @RequestMapping(value = "/{condStr}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public TransferOrderView[] find(@PathVariable("accountBook") String accountBook,
                                     @PathVariable("condStr") String condStr) {
        return this.transferOrderService.find(accountBook, Condition.fromJson(condStr));
    }

    @Override
    @RequestMapping(value = "/transfer_finish", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void transferFinish(@PathVariable("accountBook") String accountBook,
                              @RequestBody InspectFinishArgs inspectFinishArgs) {
        this.transferOrderService.transferFinish(accountBook, inspectFinishArgs);
    }
}
