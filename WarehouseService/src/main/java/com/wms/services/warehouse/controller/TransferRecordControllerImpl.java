package com.wms.services.warehouse.controller;

import com.wms.services.warehouse.service.TransferRecordService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.TransferRecord;
import com.wms.utilities.model.TransferRecordView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/transfer_record")
public class TransferRecordControllerImpl implements TransferRecordController {

    @Autowired
    TransferRecordService transformRecordService;
    @RequestMapping(value="/",method = RequestMethod.POST)
    public ResponseEntity<int[]> add(@PathVariable("accountBook") String accountBook,
                                     @RequestBody TransferRecord[] transferRecords ){
        int ids[] = transformRecordService.add(accountBook,transferRecords);
        return new ResponseEntity<int[]>(ids, HttpStatus.OK);
    }

    @RequestMapping(value = "/{condStr}",method = RequestMethod.GET)
    public ResponseEntity<TransferRecordView[]> find(@PathVariable("accountBook") String accountBook,
                                                     @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
        TransferRecordView[] transferRecordViews =transformRecordService.find(accountBook, cond);
        return new ResponseEntity<TransferRecordView[]>(transferRecordViews, HttpStatus.OK);
    }

    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return this.transformRecordService.findCount(accountBook, Condition.fromJson(condStr));
    }
}
