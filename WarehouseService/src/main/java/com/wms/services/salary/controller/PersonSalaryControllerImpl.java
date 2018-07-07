package com.wms.services.salary.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.salary.service.PersonSalaryService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.PersonSalary;
import com.wms.utilities.model.PersonSalaryView;
import com.wms.utilities.model.SalaryItem;
import com.wms.utilities.model.SalaryItemView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/person_salary")
public class PersonSalaryControllerImpl implements PersonSalaryController {
    @Autowired
    PersonSalaryService personSalaryService;

    @RequestMapping(value="/",method = RequestMethod.POST)
    public int[] add(@PathVariable("accountBook") String accountBook,
                     @RequestBody PersonSalary[] personSalaries){
        return personSalaryService.add(accountBook,personSalaries);
    }

    @RequestMapping(value = "/",method = RequestMethod.PUT)
    @ResponseBody
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody PersonSalary[] personSalaries) {
        personSalaryService.update(accountBook,personSalaries);
    }

    @RequestMapping(value = "/{strIDs}",method = RequestMethod.DELETE)
    @ResponseBody
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs,new TypeToken<int[]>(){}.getType());
        personSalaryService.remove(accountBook,ids);
    }

    @RequestMapping(value = "/{condStr}",method = RequestMethod.GET)
    public PersonSalaryView[] find(@PathVariable("accountBook") String accountBook,
                                  @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
        PersonSalaryView[] personSalaryViews =personSalaryService.find(accountBook, cond);
        return personSalaryViews;
    }

    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return this.personSalaryService.findCount(accountBook, Condition.fromJson(condStr));
    }
}
