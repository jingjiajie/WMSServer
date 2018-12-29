package com.wms.services.ledger.controller;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.ledger.datestructures.AccrualCheck;
import com.wms.services.ledger.datestructures.TransferAccount;
import com.wms.services.ledger.datestructures.TreeViewData;
import com.wms.services.ledger.service.AccountRecordService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.AccountRecord;
import com.wms.utilities.model.AccountRecordView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/{accountBook}/account_record")
public class AccountRecordControllerImpl implements AccountRecordController {

    @Autowired
    AccountRecordService accountRecordService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public int[] add(@PathVariable("accountBook") String accountBook,
                     @RequestBody AccountRecord[] accountRecords) {
        return accountRecordService.add(accountBook, accountRecords);
    }

    @Override
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody AccountRecord[] accountRecords) {
        accountRecordService.update(accountBook, accountRecords);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strIDs}", method = RequestMethod.DELETE)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs, new TypeToken<int[]>() {
        }.getType());
        accountRecordService.remove(accountBook, ids);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strCond}", method = RequestMethod.GET)
    public AccountRecordView[] find(@PathVariable("accountBook") String accountBook,
                                    @PathVariable("strCond") String condStr) {
        return accountRecordService.find(accountBook, Condition.fromJson(condStr));
    }

    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return this.accountRecordService.findCount(accountBook, Condition.fromJson(condStr));
    }

    @Override
    @RequestMapping(value = "/write_off",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void writeOff(@PathVariable("accountBook") String accountBook,
                            @RequestBody List<Integer> ids){
        this.accountRecordService.writeOff(accountBook,ids);
    }

    @Override
    @RequestMapping(value = "/accrual_check",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public List<AccrualCheck> accrualCheck(@PathVariable("accountBook") String accountBook,
                                 @RequestBody AccrualCheck accrualCheck){
        return this.accountRecordService.accrualCheck(accountBook,accrualCheck);
    }

    @Override
    @RequestMapping(value = "/deficit_check",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public List<AccountRecordView> deficitCheck(@PathVariable("accountBook") String accountBook,
                                           @RequestBody AccrualCheck accrualCheck){
        return this.accountRecordService.deficitCheck(accountBook,accrualCheck);
    }

    @Override
    @RequestMapping(value = "/real_transfer_account",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void RealTransferAccount(@PathVariable("accountBook") String accountBook,
                         @RequestBody TransferAccount transferAccount){
        this.accountRecordService.RealTransferAccount(accountBook,transferAccount);
    }

    @Override
    @RequestMapping(value = "/show_balance",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public List<AccrualCheck> showBalance(@PathVariable("accountBook") String accountBook,
                         @RequestBody AccrualCheck accrualCheck){
        return this.accountRecordService.showBalance(accountBook,accrualCheck);
    }

    @Override
    @RequestMapping(value = "/build_tree_view",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public List<TreeViewData> buildAccountTitleTreeView(@PathVariable("accountBook") String accountBook){
        return this.accountRecordService.buildAccountTitleTreeView(accountBook);
    }
}
