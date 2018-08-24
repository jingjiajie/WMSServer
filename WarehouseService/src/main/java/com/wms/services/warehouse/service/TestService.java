package com.wms.services.warehouse.service;

import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.WarehouseEntry;
import com.wms.utilities.model.WarehouseEntryView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestService {
    @Autowired
    WarehouseEntryService warehouseEntryService;
    @Autowired
    TransferRecordService transferRecordService;

    @Transactional
    public void test(){
        WarehouseEntry warehouseEntry = this.warehouseEntryService.get("WMS_Template",2);
        System.out.println("直接查询到的单号:"+warehouseEntry.getNo());
        warehouseEntry.setNo("!!!");
        this.warehouseEntryService.update("WMS_Template",new WarehouseEntry[]{warehouseEntry});
        WarehouseEntryView warehouseEntry1 = this.warehouseEntryService.find("WMS_Template",new Condition().addCondition("id",2))[0];
        System.out.println("更新后未提交时查询到的单号:"+warehouseEntry1.getNo());
        throw new WMSServiceException("抛出测试错误");
    }
}
