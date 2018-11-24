package com.wms.services.warehouse.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.warehouse.datastructures.*;
import com.wms.services.warehouse.service.StockRecordService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/stock_record")

public class StockRecordControllerImpl implements StockRecordController {

    @Autowired
    StockRecordService stockRecordService;

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public int[] add(@PathVariable("accountBook") String accountBook,
                     @RequestBody StockRecord[] stockRecords) {
        return stockRecordService.add(accountBook, stockRecords);
    }

    @Override
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody StockRecord[] stockRecords) {
        stockRecordService.update(accountBook, stockRecords);
    }


    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strIDs}", method = RequestMethod.DELETE)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs, new TypeToken<int[]>() {
        }.getType());
        //stockRecordService.remove(accountBook, ids);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strCond}", method = RequestMethod.GET)
    public StockRecordView[] find(@PathVariable("accountBook") String accountBook,
                                  @PathVariable("strCond") String condStr) {
        return stockRecordService.find(accountBook, Condition.fromJson(condStr));
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/find_newest/{strCond}", method = RequestMethod.GET)
    public StockRecordViewNewest[] findNewest(@PathVariable("accountBook") String accountBook,
                                        @PathVariable("strCond") String condStr) {
        return stockRecordService.findNewest(accountBook, Condition.fromJson(condStr));
    }
    
    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/real_transfer", method = RequestMethod.POST)
    public void RealTransferStock(@PathVariable("accountBook") String accountBook,
                                  @RequestBody TransferStock transferStock) {

        stockRecordService.RealTransformStock(accountBook, transferStock);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/add_amount", method = RequestMethod.POST)
    public void addAmount(@PathVariable("accountBook") String accountBook,
                          @RequestBody TransferStock transferStock) {
        stockRecordService.addAmount(accountBook, transferStock);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/return_supply", method = RequestMethod.POST)
    public void returnSupply(@PathVariable("accountBook") String accountBook,
                          @RequestBody StockRecord[] stockRecords) {
        stockRecordService.returnSupply(accountBook, stockRecords);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/throw", method = RequestMethod.POST)
    public void throwException() {
       throw new WMSServiceException("无法修改库存记录!");
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/modify_available_amount", method = RequestMethod.POST)
    public void modifyAvailableAmount(@PathVariable("accountBook") String accountBook,
                                      @RequestBody TransferStock transferStock) {
        stockRecordService.modifyAvailableAmount(accountBook, transferStock);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/find_by_time", method = RequestMethod.POST)
    public StockRecordViewAndSumGroupBySupplyId[] findByTime(@PathVariable("accountBook") String accountBook,
                                                             @RequestBody StockRecordFindByTime[] stockRecordFindByTimes) {
      return   stockRecordService.findByTime(accountBook, stockRecordFindByTimes);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/stock_record_find", method = RequestMethod.POST)
    public StockRecord[] find(@PathVariable("accountBook") String accountBook,
                                  @RequestBody StockRecordFind stockRecordFind) {
        return stockRecordService.find(accountBook, stockRecordFind);
    }


    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return this.stockRecordService.findCount(accountBook, Condition.fromJson(condStr));
    }

    @Override
    @RequestMapping(value="/judgeBatch",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void judgeOldestBatch(@PathVariable("accountBook") String accountBook,
                          @RequestBody JudgeOldestBatch judgeOldestBatch)
    {
        this.stockRecordService.judgeOldestBatch(accountBook,judgeOldestBatch);
    }
}