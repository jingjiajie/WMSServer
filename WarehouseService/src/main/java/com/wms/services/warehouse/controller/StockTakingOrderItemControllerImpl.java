package com.wms.services.warehouse.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.warehouse.datastructures.StockTakingOrderItemAdd;
import com.wms.services.warehouse.service.StockTakingOrderItemService;
import com.wms.utilities.datastructures.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.wms.utilities.model.StockTakingOrderItemView;
import com.wms.utilities.model.StockTakingOrderItem;

@RestController
@RequestMapping("/{accountBook}/stocktaking_order_item")
public class StockTakingOrderItemControllerImpl implements StockTakingOrderItemController{
@Autowired
    StockTakingOrderItemService stockTakingOrderItemService;

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value="/",method = RequestMethod.POST)
    public ResponseEntity<int[]> add(@PathVariable("accountBook") String accountBook,
                                     @RequestBody StockTakingOrderItem[] stockTakingOrderItems) {
        int ids[] = stockTakingOrderItemService.add(accountBook, stockTakingOrderItems);
        return new ResponseEntity<int[]>(ids, HttpStatus.OK);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/",method = RequestMethod.PUT)
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody StockTakingOrderItem[] stockTakingOrderItems) {
        stockTakingOrderItemService.update(accountBook,stockTakingOrderItems);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strIDs}",method = RequestMethod.DELETE)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs,new TypeToken<int[]>(){}.getType());
        stockTakingOrderItemService.remove(accountBook,ids);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{condStr}",method =RequestMethod.GET)
    public ResponseEntity<StockTakingOrderItemView[]> find(@PathVariable("accountBook") String accountBook,
                                                       @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
        StockTakingOrderItemView[] stockTakingOrderItemViews= stockTakingOrderItemService.find(accountBook, cond);
        return new ResponseEntity<StockTakingOrderItemView[]>(stockTakingOrderItemViews, HttpStatus.OK);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/add_all",method =RequestMethod.POST)
    public void addStockTakingOrderItemAll
            (@PathVariable("accountBook") String accountBook,
             @RequestBody StockTakingOrderItemAdd stockTakingOrderItemAdd)
    {
        stockTakingOrderItemService.addStockTakingOrderItemAll(accountBook,stockTakingOrderItemAdd);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value ="/add_single",method =RequestMethod.POST)
    public void addStockTakingOrderItemSingle
            (@PathVariable("accountBook") String accountBook,
             @RequestBody StockTakingOrderItemAdd stockTakingOrderItemAdd)
    { stockTakingOrderItemService.addStockTakingOrderItemSingle(accountBook,stockTakingOrderItemAdd);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping("/set_real_amount")
    public void setRealAmount
            (@PathVariable("accountBook") String accountBook,
             @RequestBody StockTakingOrderItem stockTakingOrderItem)
    { stockTakingOrderItemService.setRealAmount(accountBook,stockTakingOrderItem);
    }

    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return this.stockTakingOrderItemService.findCount(accountBook, Condition.fromJson(condStr));
    }
}

