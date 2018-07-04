package com.wms.services.salary.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.salary.Service.SalaryItemService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.SalaryItem;
import com.wms.utilities.model.SalaryItemView;
import com.wms.utilities.model.SalaryPeriod;
import com.wms.utilities.model.SalaryPeriodView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/salary_item")
public class SalaryitemControllerImpl implements SalaryItemController {
    @Autowired
    SalaryItemService salaryItemService;

    @RequestMapping(value="/",method = RequestMethod.POST)
    public int[] add(@PathVariable("accountBook") String accountBook,
                     @RequestBody SalaryItem[] salaryItems){
        return salaryItemService.add(accountBook,salaryItems);
    }

    @RequestMapping(value = "/",method = RequestMethod.PUT)
    @ResponseBody
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody SalaryItem[] salaryItems) {
        salaryItemService.update(accountBook,salaryItems);
    }

    @RequestMapping(value = "/{strIDs}",method = RequestMethod.DELETE)
    @ResponseBody
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs,new TypeToken<int[]>(){}.getType());
        salaryItemService.remove(accountBook,ids);
    }

    @RequestMapping(value = "/{condStr}",method = RequestMethod.GET)
    public SalaryItemView[] find(@PathVariable("accountBook") String accountBook,
                                 @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
        SalaryItemView[] salaryPeriodViews =salaryItemService.find(accountBook, cond);
        return salaryPeriodViews;
    }

    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return this.salaryItemService.findCount(accountBook, Condition.fromJson(condStr));
    }
}
