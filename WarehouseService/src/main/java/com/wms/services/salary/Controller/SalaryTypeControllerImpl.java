package com.wms.services.salary.Controller;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.salary.Service.SalaryTypeService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.SalaryType;
import com.wms.utilities.model.SalaryTypeView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/salary_type")
public class SalaryTypeControllerImpl implements SalaryTypeController {

    @Autowired
    SalaryTypeService salaryTypeService;
    @RequestMapping(value="/",method = RequestMethod.POST)
    public int[] add(@PathVariable("accountBook") String accountBook,
                                     @RequestBody SalaryType[] salaryTypes ){
         return salaryTypeService.add(accountBook,salaryTypes);
}

    @RequestMapping(value = "/",method = RequestMethod.PUT)
    @ResponseBody
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody SalaryType[] salaryTypes) {
        salaryTypeService.update(accountBook,salaryTypes);
    }

    @RequestMapping(value = "/{strIDs}",method = RequestMethod.DELETE)
    @ResponseBody
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs,new TypeToken<int[]>(){}.getType());
        salaryTypeService.remove(accountBook,ids);
    }

    @RequestMapping(value = "/{condStr}",method = RequestMethod.GET)
    public SalaryTypeView[] find(@PathVariable("accountBook") String accountBook,
                                                 @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
        SalaryTypeView[] salaryTypeViews =salaryTypeService.find(accountBook, cond);
        return salaryTypeViews;
    }

    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return this.salaryTypeService.findCount(accountBook, Condition.fromJson(condStr));
    }

}
