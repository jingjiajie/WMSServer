package com.wms.services.ledger.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.ledger.model.AccountTitle;
import com.wms.services.ledger.service.AccountTitleService;
import com.wms.utilities.datastructures.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/accounttitle")
public class AccountTitleControllerImpl implements AccountTitleController{
    @Autowired
    AccountTitleService accountTitleService;

    @RequestMapping(value = "/",method = RequestMethod.POST)
    public ResponseEntity<int[]> add(@PathVariable("accountBook") String accountBook,
                                     @RequestBody AccountTitle[] accountTitles) {
        int ids[] = accountTitleService.add(accountBook, accountTitles);
        return new ResponseEntity<int[]>(ids, HttpStatus.OK);
    }
    @RequestMapping(value = "/{strIDs}",method = RequestMethod.DELETE)
    @ResponseBody
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs,new TypeToken<int[]>(){}.getType());
        accountTitleService.remove(accountBook,ids);
    }

    @RequestMapping(value = "/",method = RequestMethod.PUT)
    @ResponseBody
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody AccountTitle[] accountTitles) {
        accountTitleService.update(accountBook,accountTitles);
    }

    @RequestMapping("/{condStr}")
    public ResponseEntity<AccountTitle[]> find(@PathVariable("accountBook") String accountBook,
                                               @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
        AccountTitle[] accountTitles = accountTitleService.find(accountBook,cond);
        return new ResponseEntity<AccountTitle[]>(accountTitles, HttpStatus.OK);
    }
}
