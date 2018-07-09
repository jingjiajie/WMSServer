package com.wms.services.ledger.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.utilities.model.AccountTitle;
import com.wms.services.ledger.service.AccountTitleService;
import com.wms.utilities.datastructures.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.wms.utilities.model.AccountTitle;
import com.wms.utilities.model.AccountTitleView;

@RestController
@RequestMapping("/{accountBook}/account_title")
public class AccountTitleControllerImpl implements AccountTitleController{
    @Autowired
    AccountTitleService accountTitleService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public int[] add(@PathVariable("accountBook") String accountBook,
                     @RequestBody AccountTitle[] accountTitles) {
        return accountTitleService.add(accountBook, accountTitles);
    }

    @Override
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody AccountTitle[] accountTitles) {
        accountTitleService.update(accountBook, accountTitles);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strIDs}", method = RequestMethod.DELETE)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs, new TypeToken<int[]>() {
        }.getType());
        accountTitleService.remove(accountBook, ids);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strCond}", method = RequestMethod.GET)
    public AccountTitleView[] find(@PathVariable("accountBook") String accountBook,
                                    @PathVariable("strCond") String condStr) {
        return accountTitleService.find(accountBook, Condition.fromJson(condStr));
    }

    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return this.accountTitleService.findCount(accountBook, Condition.fromJson(condStr));
    }
}
