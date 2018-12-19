package com.wms.services.warehouse.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.warehouse.service.WarehouseEntryItemService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.WarehouseEntry;
import com.wms.utilities.model.WarehouseEntryItem;
import com.wms.utilities.model.WarehouseEntryItemView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/{accountBook}/warehouse_entry_item")
public class WarehouseEntryItemControllerImpl implements WarehouseEntryItemController {
    @Autowired
    WarehouseEntryItemService warehouseEntryItemService;

    @Override
    @RequestMapping(value = "/",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public int[] add(@PathVariable("accountBook") String accountBook,
                                     @RequestBody WarehouseEntryItem[] items) {
        //全部用新接口增加
        return this.warehouseEntryItemService.add1(accountBook,items);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/",method = RequestMethod.PUT)
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody WarehouseEntryItem[] items) {
        if(items.length==0){return;}
        List<WarehouseEntryItem> warehouseEntryItemList = Arrays.asList(items);
        warehouseEntryItemList.stream().sorted(Comparator.comparing(WarehouseEntryItem::getVersion)).reduce((last, cur) -> {
            if (!last.getVersion().equals(cur.getVersion())) {
                throw new WMSServiceException("入库单条目版本不一致！" );
            }
            return cur;
        });
        if(items[0].getVersion()==0){
        this.warehouseEntryItemService.update(accountBook,items);}
        else {this.warehouseEntryItemService.update1(accountBook,items);}
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{ids}",method = RequestMethod.DELETE)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("ids") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs, new TypeToken<int[]>() {}.getType());
        this.warehouseEntryItemService.remove(accountBook,ids);
    }

    @Override
    @RequestMapping(value = "/{cond}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public WarehouseEntryItemView[] find(@PathVariable("accountBook") String accountBook,
                                         @PathVariable("cond") String strCond) {
        return this.warehouseEntryItemService.find(accountBook, Condition.fromJson(strCond));
    }

    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return warehouseEntryItemService.findCount(accountBook, Condition.fromJson(condStr));
    }
}
