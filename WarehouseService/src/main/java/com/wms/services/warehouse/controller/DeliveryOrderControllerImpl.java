package com.wms.services.warehouse.controller;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.warehouse.datastructures.*;
import com.wms.services.warehouse.service.DeliveryOrderService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.DeliveryOrder;
import com.wms.utilities.model.DeliveryOrderView;
import com.wms.utilities.model.TransferOrderItem;
import com.wms.utilities.model.TransferOrderItemView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    @RequestMapping(value = "/transfer_package",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void transferPakage(@PathVariable("accountBook") String accountBook,
                        @RequestBody TransferArgs transferArgs){
        this.deliveryOrderService.transferPakage(accountBook,transferArgs);
    }

    @Override
    @RequestMapping(value = "/transfer_auto",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public List<TransferOrderItemView> transferAuto(@PathVariable("accountBook") String accountBook,
                                                    @RequestBody TransferAuto transferAuto){
        return this.deliveryOrderService.transferAuto(accountBook,transferAuto);
    }

    @Override
    @RequestMapping(value = "/delivery_finish",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void deliveryFinish(@PathVariable("accountBook") String accountBook,
                             @RequestBody DeliveryFinish deliveryFinish){
        this.deliveryOrderService.deliveryFinish(accountBook,deliveryFinish);
    }

    @Override
    @RequestMapping(value = "/decrease_in_accounting",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void decreaseInAccounting(@PathVariable("accountBook") String accountBook,
                               @RequestBody List<Integer> ids){
        this.deliveryOrderService.decreaseInAccounting(accountBook,ids);
    }

    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return this.deliveryOrderService.findCount(accountBook, Condition.fromJson(condStr));
    }

    @Override
    @RequestMapping(value="/preview/{strIDs}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<DeliveryOrderAndItems> getPreviewData(@PathVariable("accountBook") String accountBook,
                                                         @PathVariable("strIDs") String strIDs){
        Gson gson = new Gson();
        List<Integer> ids = gson.fromJson(strIDs, new TypeToken<List<Integer>>() {}.getType());
        return deliveryOrderService.getPreviewData(accountBook,ids);
    }

    @Override
    @RequestMapping(value = "/delivery_by_package",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void deliveryByPakage(@PathVariable("accountBook") String accountBook,
                             @RequestBody DeliveryByPakage deliveryByPakage){
        this.deliveryOrderService.deliveryByPakage(accountBook,deliveryByPakage);
    }
}
