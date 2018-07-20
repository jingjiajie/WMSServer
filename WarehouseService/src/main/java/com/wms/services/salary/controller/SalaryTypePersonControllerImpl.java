package com.wms.services.salary.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.salary.service.SalaryTypePersonService;
import com.wms.services.salary.service.SalaryTypeService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.SalaryType;
import com.wms.utilities.model.SalaryTypePerson;
import com.wms.utilities.model.SalaryTypePersonView;
import com.wms.utilities.model.SalaryTypeView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/salary_type_person")
public class SalaryTypePersonControllerImpl implements SalaryTypePersonCntroller {

        @Autowired
        SalaryTypePersonService salaryTypePersonService;
        @RequestMapping(value="/",method = RequestMethod.POST)
        public int[] add(@PathVariable("accountBook") String accountBook,
                         @RequestBody SalaryTypePerson[] salaryTypePeople ){
            return salaryTypePersonService.add(accountBook,salaryTypePeople);
        }

        @RequestMapping(value = "/",method = RequestMethod.PUT)
        @ResponseBody
        public void update(@PathVariable("accountBook") String accountBook,
                           @RequestBody SalaryTypePerson[] salaryTypePeople) {
            salaryTypePersonService.update(accountBook,salaryTypePeople);
        }

        @RequestMapping(value = "/{strIDs}",method = RequestMethod.DELETE)
        @ResponseBody
        public void remove(@PathVariable("accountBook") String accountBook,
                           @PathVariable("strIDs") String strIDs) {
            Gson gson = new Gson();
            int ids[] = gson.fromJson(strIDs,new TypeToken<int[]>(){}.getType());
            salaryTypePersonService.remove(accountBook,ids);
        }

        @RequestMapping(value = "/{condStr}",method = RequestMethod.GET)
        public SalaryTypePersonView[] find(@PathVariable("accountBook") String accountBook,
                                           @PathVariable("condStr") String condStr) {
            Condition cond = Condition.fromJson(condStr);
            SalaryTypePersonView[] salaryTypePersonViews =salaryTypePersonService.find(accountBook, cond);
            return salaryTypePersonViews;
        }

        @Override
        @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
        @ResponseStatus(HttpStatus.OK)
        public long findCount(@PathVariable("accountBook") String accountBook,
                              @PathVariable("condStr") String condStr){
            return this.salaryTypePersonService.findCount(accountBook, Condition.fromJson(condStr));
        }
}
