package com.wms.services.warehouse.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.warehouse.datastructures.InspectFinishArgs;
import com.wms.services.warehouse.service.TransferOrderItemService;
import com.wms.services.warehouse.service.TransferOrderService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.TransferOrderItem;
import com.wms.utilities.model.TransferOrderItemView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/transfer_order_order")
public class TransferOrderItemControllerImpl
        implements TransferOrderItemController {
    @Autowired
    TransferOrderItemService transferOrderItemService;

    @Override
    @RequestMapping(value = "/{strIDs}",method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs,new TypeToken<int[]>(){}.getType());
        this.transferOrderItemService.remove(accountBook,ids);
    }

    @Override
    @RequestMapping(value = "/",method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("accountBook")String accountBook,
                       @RequestBody TransferOrderItem[] objs) {
        this.transferOrderItemService.update(accountBook,objs);
    }

    @Override
    @RequestMapping(value = "/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public TransferOrderItemView[] find(@PathVariable("accountBook") String accountBook,
                                         @PathVariable("condStr") String condStr) {
        return this.transferOrderItemService.find(accountBook, Condition.fromJson(condStr));
    }
}
