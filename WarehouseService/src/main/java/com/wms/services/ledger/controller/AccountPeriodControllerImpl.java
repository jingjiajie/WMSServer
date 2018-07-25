package com.wms.services.ledger.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.ledger.datestructures.CarryOver;
import com.wms.services.ledger.service.AccountPeriodService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.AccountPeriod;
import com.wms.utilities.model.AccountPeriodView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/account_period")
public class AccountPeriodControllerImpl implements AccountPeriodController {
    @Autowired
    AccountPeriodService accountPeriodService;

    @RequestMapping(value="/",method = RequestMethod.POST)
    public int[] add(@PathVariable("accountBook") String accountBook,
                     @RequestBody AccountPeriod[] accountPeriods ){
        return accountPeriodService.add(accountBook,accountPeriods);
    }

    @RequestMapping(value = "/",method = RequestMethod.PUT)
    @ResponseBody
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody AccountPeriod[] accountPeriods) {
        accountPeriodService.update(accountBook,accountPeriods);
    }

    @RequestMapping(value = "/{strIDs}",method = RequestMethod.DELETE)
    @ResponseBody
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs,new TypeToken<int[]>(){}.getType());
        accountPeriodService.remove(accountBook,ids);
    }

    @RequestMapping(value = "/{condStr}",method = RequestMethod.GET)
    public AccountPeriodView[] find(@PathVariable("accountBook") String accountBook,
                                   @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
        AccountPeriodView[] accountPeriodViews =accountPeriodService.find(accountBook, cond);
        return accountPeriodViews;
    }

    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return this.accountPeriodService.findCount(accountBook, Condition.fromJson(condStr));
    }

    @Override
    @RequestMapping(value = "/carry_over",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void carryOver(@PathVariable("accountBook") String accountBook,
                               @RequestBody CarryOver carryOver){
        this.accountPeriodService.carryOver(accountBook,carryOver);
    }
}
