package com.wms.services.warehouse.controller;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.warehouse.datastructures.TransferArgs;
import com.wms.services.warehouse.datastructures.TransferAuto;
import com.wms.services.warehouse.service.DeliveryOrderService;
import com.wms.utilities.datastructures.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.wms.utilities.model.DeliveryOrder;
import com.wms.utilities.model.DeliveryOrderView;

import java.util.List;

@RestController
@RequestMapping("/{accountBook}/delivery_order")
public class DeliveryOrderControllerImpl implements DeliveryOrderController {
    @Autowired
    DeliveryOrderService deliveryOrderService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public int[] add(@PathVariable("accountBook") String accountBook,
                     @RequestBody DeliveryOrder[] deliveryOrders) {
        return deliveryOrderService.add(accountBook, deliveryOrders);
    }

    @Override
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody DeliveryOrder[] deliveryOrders) {
        deliveryOrderService.update(accountBook, deliveryOrders);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strIDs}", method = RequestMethod.DELETE)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs, new TypeToken<int[]>() {
        }.getType());
        deliveryOrderService.remove(accountBook, ids);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strCond}", method = RequestMethod.GET)
    public DeliveryOrderView[] find(@PathVariable("accountBook") String accountBook,
                                     @PathVariable("strCond") String condStr) {
        return deliveryOrderService.find(accountBook, Condition.fromJson(condStr));
    }

    @Override
    @RequestMapping(value = "/transfer",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void transferPakage(@PathVariable("accountBook") String accountBook,
                        @RequestBody TransferArgs transferArgs){
        this.deliveryOrderService.transferPakage(accountBook,transferArgs);
    }

    @Override
    @RequestMapping(value = "/transfer_auto",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void transferAuto(@PathVariable("accountBook") String accountBook,
                               @RequestBody TransferAuto transferAuto){
        this.deliveryOrderService.transferAuto(accountBook,transferAuto);
    }

    @Override
    @RequestMapping(value = "/delivery_finish",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void deliveryFinish(@PathVariable("accountBook") String accountBook,
                             @RequestBody List<Integer> ids){
        this.deliveryOrderService.deliveryFinish(accountBook,ids);
    }

    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return this.deliveryOrderService.findCount(accountBook, Condition.fromJson(condStr));
    }
}
