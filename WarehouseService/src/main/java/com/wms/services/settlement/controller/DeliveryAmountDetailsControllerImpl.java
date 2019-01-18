package com.wms.services.settlement.controller;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.settlement.service.DeliveryAmountDetailsService;
import com.wms.utilities.datastructures.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.wms.utilities.model.DeliveryAmountDetails;
import com.wms.utilities.model.DeliveryAmountDetailsView;

@RestController
@RequestMapping("/{accountBook}/delivery_amount_details")
public class DeliveryAmountDetailsControllerImpl implements DeliveryAmountDetailsController{
    @Autowired
    DeliveryAmountDetailsService deliveryAmountDetailsService;

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value="/",method = RequestMethod.POST)
    public int[] add(@PathVariable("accountBook") String accountBook,
                     @RequestBody DeliveryAmountDetails[] deliveryAmountDetails) {
        int ids[] = deliveryAmountDetailsService.add(accountBook, deliveryAmountDetails);
        return  ids;
    }

    @Override
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody DeliveryAmountDetails[] deliveryAmountDetails) {
        deliveryAmountDetailsService.update(accountBook, deliveryAmountDetails);
    }

    @Override
    @RequestMapping(value = "/{strIDs}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs, new TypeToken<int[]>() {
        }.getType());
        this.deliveryAmountDetailsService.remove(accountBook, ids);
    }


    @Override
    @RequestMapping(value = "/{condStr}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public DeliveryAmountDetailsView[] find(@PathVariable("accountBook") String accountBook,
                                  @PathVariable("condStr") String condStr) {
        return this.deliveryAmountDetailsService.find(accountBook, Condition.fromJson(condStr));
    }


    @Override
    @RequestMapping(value = "/count/{condStr}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr) {
        return this.deliveryAmountDetailsService.findCount(accountBook, Condition.fromJson(condStr));
    }
}
