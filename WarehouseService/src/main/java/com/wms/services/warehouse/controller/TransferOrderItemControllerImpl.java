package com.wms.services.warehouse.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.warehouse.service.TransferOrderItemService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.TransferOrderItem;
import com.wms.utilities.model.TransferOrderItemView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/{accountBook}/transfer_order_item")
public class TransferOrderItemControllerImpl
        implements TransferOrderItemController {
    @Autowired
    TransferOrderItemService transferOrderItemService;

    @RequestMapping(value="/",method = RequestMethod.POST)
    public ResponseEntity<int[]> add(@PathVariable("accountBook") String accountBook,
                                     @RequestBody TransferOrderItem[] transferOrderItems) {
        int ids[] = transferOrderItemService.add(accountBook, transferOrderItems);
        return new ResponseEntity<int[]>(ids, HttpStatus.OK);
    }

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
    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return this.transferOrderItemService.findCount(accountBook, Condition.fromJson(condStr));
    }
}
