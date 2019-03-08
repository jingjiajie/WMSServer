package com.wms.services.warehouse.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.warehouse.datastructures.DailyReportRequest;
import com.wms.services.warehouse.datastructures.DailyReports;
import com.wms.services.warehouse.datastructures.SupplierAmount;
import com.wms.services.warehouse.service.DeliveryOrderItemService;
import com.wms.services.warehouse.service.SupplierServices;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.DeliveryOrderItemView;
import com.wms.utilities.model.Supplier;
import com.wms.utilities.model.SupplierView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/{accountBook}/supplier")
public class SupplierControllerImpl implements SupplierController {
    @Autowired
    SupplierServices supplierServices;
    @Autowired
    DeliveryOrderItemService deliveryOrderItemService;

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<int[]> add(@PathVariable("accountBook") String accountBook,
                                     @RequestBody Supplier[] suppliers) {
        int ids[] = supplierServices.add(accountBook, suppliers);
        return new ResponseEntity<int[]>(ids, HttpStatus.OK);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody Supplier[] suppliers) {
        supplierServices.update(accountBook, suppliers);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/history_save", method = RequestMethod.PUT)
    public void updateHistorySave(@PathVariable("accountBook") String accountBook,
                                  @RequestBody Supplier[] suppliers) {
        supplierServices.updateHistory(accountBook, suppliers);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strIDs}", method = RequestMethod.DELETE)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs, new TypeToken<int[]>() {
        }.getType());
        supplierServices.remove(accountBook, ids);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping("/{condStr}")
    public ResponseEntity<SupplierView[]> find(@PathVariable("accountBook") String accountBook,
                                               @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
        SupplierView[] suppliers = supplierServices.find(accountBook, cond);
        return new ResponseEntity<SupplierView[]>(suppliers, HttpStatus.OK);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping("/{condStr}/new")
    public ResponseEntity<SupplierView[]> findNew(@PathVariable("accountBook") String accountBook,
                                                  @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
        SupplierView[] suppliers = supplierServices.findNew(accountBook, cond);
        return new ResponseEntity<SupplierView[]>(suppliers, HttpStatus.OK);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping("/{condStr}/history")
    public ResponseEntity<SupplierView[]> findHistory(@PathVariable("accountBook") String accountBook,
                                                      @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
        SupplierView[] suppliers = supplierServices.findHistory(accountBook, cond);
        return new ResponseEntity<SupplierView[]>(suppliers, HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/count/{condStr}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr) {
        return this.supplierServices.findCount(accountBook, Condition.fromJson(condStr));
    }

    @Override
    @RequestMapping(value = "/count/{condStr}/new", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCountNew(@PathVariable("accountBook") String accountBook,
                             @PathVariable("condStr") String condStr) {
        return this.supplierServices.findCountNew(accountBook, Condition.fromJson(condStr));
    }

    @Override
    @RequestMapping(value = "/count/{condStr}/history", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCountHistory(@PathVariable("accountBook") String accountBook,
                                 @PathVariable("condStr") String condStr) {
        return this.supplierServices.findCountHistory(accountBook, Condition.fromJson(condStr));
    }

    @Override
    @RequestMapping(value = "/check_delivery", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public DeliveryOrderItemView[] checkDelivery(@PathVariable("accountBook") String accountBook,
                                                 @RequestBody DailyReportRequest dailyReportRequest) {

        if (dailyReportRequest.getTime() == null) {
            dailyReportRequest.setTime(new Timestamp(System.currentTimeMillis()).toString());
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = format.parse(dailyReportRequest.getTime());
        } catch (Exception e) {
            throw new WMSServiceException("请检查时间格式是否正确1");
        }
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        Timestamp timestampStart = new Timestamp(date.getTime());
        date.setHours(23);
        date.setMinutes(59);
        date.setSeconds(59);
        Timestamp timestampEnd = new Timestamp(date.getTime());
        dailyReportRequest.setStartTime(timestampStart);
        dailyReportRequest.setEndTime(timestampEnd);
        return this.deliveryOrderItemService.find(accountBook,new Condition().addCondition("supplierId",dailyReportRequest.getSupplierId()).
                addCondition("deliveryOrderItemCreatTime",new Object[]{dailyReportRequest.getStartTime(),dailyReportRequest.getEndTime()}, ConditionItem.Relation.BETWEEN));
    }

    @Override
    @RequestMapping(value = "/remind/{supplierId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public SupplierAmount[] supplierRemind(@PathVariable("accountBook") String accountBook,
                                           @PathVariable("supplierId") String id) {
        int supplierId = Integer.valueOf(id.substring(1, id.length() - 1));
        return this.supplierServices.supplierRemind(accountBook, supplierId);
    }

    @Override
    @RequestMapping(value = "/generate_daily_reports", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public List<DailyReports> generateDailyReports(@PathVariable("accountBook") String accountBook,
                                                   @RequestBody DailyReportRequest dailyReportRequest) {

        if (dailyReportRequest.getTime() == null) {
            dailyReportRequest.setTime(new Timestamp(System.currentTimeMillis()).toString());
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = format.parse(dailyReportRequest.getTime());
        } catch (Exception e) {
            throw new WMSServiceException("请检查时间格式是否正确1");
        }
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        Timestamp timestampStart = new Timestamp(date.getTime());
        date.setHours(23);
        date.setMinutes(59);
        date.setSeconds(59);
        Timestamp timestampEnd = new Timestamp(date.getTime());
        dailyReportRequest.setStartTime(timestampStart);
        dailyReportRequest.setEndTime(timestampEnd);
        List<DailyReports> dailyReportRequestList = this.supplierServices.generateDailyReports(accountBook, dailyReportRequest);
        if (dailyReportRequest.getMaterialName() != null) {
            if (!dailyReportRequest.getMaterialName().equals("")) {
                Iterator<DailyReports> iter = dailyReportRequestList.iterator();
                while (iter.hasNext()) {
                    //list.remove(0);
                    DailyReports dailyReports = iter.next();
                    if (!dailyReports.getMaterialName().contains(dailyReportRequest.getMaterialName())) {
                        iter.remove();
                    }
                }
            }
        }
        Collections.reverse(dailyReportRequestList);
        return dailyReportRequestList;
    }

    @Override
    @RequestMapping(value = "/generate_daily_reports_by_year", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public List<DailyReports> generateDailyReportsByYear(@PathVariable("accountBook") String accountBook,
                                                   @RequestBody DailyReportRequest dailyReportRequest) {

        if (dailyReportRequest.getTime() == null) {
            dailyReportRequest.setTime(new Timestamp(System.currentTimeMillis()).toString());
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = format.parse(dailyReportRequest.getTime());
        } catch (Exception e) {
            throw new WMSServiceException("请检查时间格式是否正确1");
        }
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        Timestamp timestampStart = new Timestamp(date.getTime());
        date.setHours(23);
        date.setMinutes(59);
        date.setSeconds(59);
        Timestamp timestampEnd = new Timestamp(date.getTime());
        dailyReportRequest.setStartTime(timestampStart);
        dailyReportRequest.setEndTime(timestampEnd);
        List<DailyReports> dailyReportRequestList = this.supplierServices.generateDailyReportsByYear(accountBook, dailyReportRequest.getSupplierId());
        /*if (dailyReportRequest.getMaterialName() != null) {
            if (!dailyReportRequest.getMaterialName().equals("")) {
                Iterator<DailyReports> iter = dailyReportRequestList.iterator();
                while (iter.hasNext()) {
                    //list.remove(0);
                    DailyReports dailyReports = iter.next();
                    if (!dailyReports.getMaterialName().contains(dailyReportRequest.getMaterialName())) {
                        iter.remove();
                    }
                }
            }
        }*/
        //Collections.reverse(dailyReportRequestList);
        return dailyReportRequestList;
    }
}
