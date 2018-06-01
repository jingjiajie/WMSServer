package com.wms.services.warehouse.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.warehouse.datastructures.StockTakingItemDelete;
import com.wms.services.warehouse.datastructures.StockTakingOrderItemAdd;
import com.wms.services.warehouse.service.StockTakingOrderService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.StockTakingOrder;
import com.wms.utilities.model.StockTakingOrderView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/stocktaking_order")
public class StockTakingOrderControllerImpl implements StockTakingOrderController{
    @Autowired
    StockTakingOrderService stockTakingOrderService;
    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value="/",method = RequestMethod.POST)
    public ResponseEntity<int[]> add(@PathVariable("accountBook") String accountBook,
                                     @RequestBody StockTakingOrder[] stockTakingOrders) {
        int ids[] = stockTakingOrderService.add(accountBook, stockTakingOrders);
        return new ResponseEntity<int[]>(ids, HttpStatus.OK);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/",method = RequestMethod.PUT)
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody StockTakingOrder[] stockTakingOrders) {
        stockTakingOrderService.update(accountBook,stockTakingOrders);
    }


    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strIDs}",method = RequestMethod.DELETE)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs,new TypeToken<int[]>(){}.getType());
        stockTakingOrderService.remove(accountBook,ids);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{condStr}",method =RequestMethod.GET)
    public ResponseEntity<StockTakingOrderView[]> find(@PathVariable("accountBook") String accountBook,
                                                       @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
        StockTakingOrderView[] stockTakingOrderViews= stockTakingOrderService.find(accountBook, cond);
        return new ResponseEntity<StockTakingOrderView[]>(stockTakingOrderViews, HttpStatus.OK);
    }

    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return this.stockTakingOrderService.findCount(accountBook, Condition.fromJson(condStr));
    }
}
