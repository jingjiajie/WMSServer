package com.wms.services.salary.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.salary.Service.PayNoteService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.PayNote;
import com.wms.utilities.model.PayNoteView;
import com.wms.utilities.model.SalaryItem;
import com.wms.utilities.model.SalaryItemView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/pay_note")
public class PayNoteControllerImpl implements PayNoteController {
    @Autowired
    PayNoteService payNoteService;


    @RequestMapping(value="/",method = RequestMethod.POST)
    public int[] add(@PathVariable("accountBook") String accountBook,
                     @RequestBody PayNote[] payNotes){
        return payNoteService.add(accountBook,payNotes);
    }

    @RequestMapping(value = "/",method = RequestMethod.PUT)
    @ResponseBody
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody PayNote[] payNotes) {
        payNoteService.update(accountBook,payNotes);
    }

    @RequestMapping(value = "/{strIDs}",method = RequestMethod.DELETE)
    @ResponseBody
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs,new TypeToken<int[]>(){}.getType());
        payNoteService.remove(accountBook,ids);
    }

    @RequestMapping(value = "/{condStr}",method = RequestMethod.GET)
    public PayNoteView[] find(@PathVariable("accountBook") String accountBook,
                              @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
        PayNoteView[] payNoteViews =payNoteService.find(accountBook, cond);
        return payNoteViews;
    }

    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return this.payNoteService.findCount(accountBook, Condition.fromJson(condStr));
    }
}
