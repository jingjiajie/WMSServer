package com.wms.services.salary.controller;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.salary.service.SalaryPeriodService;
import com.wms.services.salary.service.SalaryTypeService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.SalaryPeriod;
import com.wms.utilities.model.SalaryPeriodView;
import com.wms.utilities.model.SalaryType;
import com.wms.utilities.model.SalaryTypeView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/salary_period")
public class SalaryPeriodControllerImpl implements SalaryPeriodController {
    @Autowired
    SalaryPeriodService salaryPeriodService;
    @RequestMapping(value="/",method = RequestMethod.POST)
    public int[] add(@PathVariable("accountBook") String accountBook,
                     @RequestBody SalaryPeriod[] salaryPeriods ){
        return salaryPeriodService.add(accountBook,salaryPeriods);
    }

    @RequestMapping(value = "/",method = RequestMethod.PUT)
    @ResponseBody
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody SalaryPeriod[] salaryPeriods) {
        salaryPeriodService.update(accountBook,salaryPeriods);
    }

    @RequestMapping(value = "/{strIDs}",method = RequestMethod.DELETE)
    @ResponseBody
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs,new TypeToken<int[]>(){}.getType());
        salaryPeriodService.remove(accountBook,ids);
    }

    @RequestMapping(value = "/{condStr}",method = RequestMethod.GET)
    public SalaryPeriodView[] find(@PathVariable("accountBook") String accountBook,
                                   @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
        SalaryPeriodView[] salaryPeriodViews =salaryPeriodService.find(accountBook, cond);
        return salaryPeriodViews;
    }

    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return this.salaryPeriodService.findCount(accountBook, Condition.fromJson(condStr));
    }


}
