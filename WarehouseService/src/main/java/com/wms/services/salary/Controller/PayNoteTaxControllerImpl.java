package com.wms.services.salary.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.salary.Service.PayNoteTaxService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.PayNoteItem;
import com.wms.utilities.model.PayNoteItemView;
import com.wms.utilities.model.PayNoteTax;
import com.wms.utilities.model.PayNoteTaxView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/pay_note_tax")
public class PayNoteTaxControllerImpl implements PayNoteTaxController {
    @Autowired
    PayNoteTaxService payNoteTaxService;

    @RequestMapping(value="/",method = RequestMethod.POST)
    public int[] add(@PathVariable("accountBook") String accountBook,
                     @RequestBody PayNoteTax[] payNoteTaxes){
        return payNoteTaxService.add(accountBook,payNoteTaxes);
    }

    @RequestMapping(value = "/",method = RequestMethod.PUT)
    @ResponseBody
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody PayNoteTax[] payNoteTaxes) {
        payNoteTaxService.update(accountBook,payNoteTaxes);
    }

    @RequestMapping(value = "/{strIDs}",method = RequestMethod.DELETE)
    @ResponseBody
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs,new TypeToken<int[]>(){}.getType());
        payNoteTaxService.remove(accountBook,ids);
    }

    @RequestMapping(value = "/{condStr}",method = RequestMethod.GET)
    public PayNoteTaxView[] find(@PathVariable("accountBook") String accountBook,
                                 @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
        PayNoteTaxView[] payNoteTaxViews =payNoteTaxService.find(accountBook, cond);
        return payNoteTaxViews;
    }

    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return this.payNoteTaxService.findCount(accountBook, Condition.fromJson(condStr));
    }
}
