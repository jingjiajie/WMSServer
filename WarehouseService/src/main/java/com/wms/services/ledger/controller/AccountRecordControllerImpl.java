package com.wms.services.ledger.controller;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.ledger.service.AccountRecordService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.AccountRecord;
import com.wms.utilities.model.AccountRecordView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/account_record")
public class AccountRecordControllerImpl implements AccountRecordController {

    @Autowired
    AccountRecordService accountRecordService;

    @RequestMapping(value="/",method = RequestMethod.POST)
    public int[] add(@PathVariable("accountBook") String accountBook,
                     @RequestBody AccountRecord[] accountRecords ){
        return accountRecordService.add(accountBook,accountRecords);
    }

    @RequestMapping(value = "/",method = RequestMethod.PUT)
    @ResponseBody
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody AccountRecord[] accountRecords) {
        accountRecordService.update(accountBook,accountRecords);
    }

    @RequestMapping(value = "/{strIDs}",method = RequestMethod.DELETE)
    @ResponseBody
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs,new TypeToken<int[]>(){}.getType());
        accountRecordService.remove(accountBook,ids);
    }

    @RequestMapping(value = "/{condStr}",method = RequestMethod.GET)
    public AccountRecordView[] find(@PathVariable("accountBook") String accountBook,
                                   @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
        AccountRecordView[] accountRecordViews =accountRecordService.find(accountBook, cond);
        return accountRecordViews;
    }

    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return this.accountRecordService.findCount(accountBook, Condition.fromJson(condStr));
    }
}
