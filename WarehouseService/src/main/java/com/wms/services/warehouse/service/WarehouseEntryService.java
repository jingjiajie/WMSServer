package com.wms.services.warehouse.service;

import com.wms.services.warehouse.datastructures.InspectArgs;
import com.wms.services.warehouse.datastructures.WarehouseEntryAndItems;
import com.wms.utilities.model.WarehouseEntry;
import com.wms.utilities.model.WarehouseEntryView;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.service.BaseService;

import java.util.List;

public interface WarehouseEntryService
        extends BaseService<WarehouseEntry, WarehouseEntryView> {

    /**
     * 送检
     *
     * @param accountBook 账套
     * @param inspectArgs 送检参数
     * @see InspectArgs
     */
    public List<Integer> inspect(String accountBook, InspectArgs inspectArgs);
    void updateState(String accountBook, List<Integer> ids);
    WarehouseEntry get(String accountBook,int id) throws WMSServiceException;

    //0待入库 1送检中 2.全部入库 3.部分入库
    int WAIT_FOR_PUT_IN_STORAGE = 0; //待入库
    int BEING_INSPECTED = 1; //送检中
    int ALL_PUT_IN_STORAGE = 2; //全部入库
    int PART_PUT_IN_STORAGE = 3;//部分入库

    List<WarehouseEntryAndItems> getPreviewData(String accountBook, List<Integer> warehouseEntryIDs) throws WMSServiceException;
    void receive(String accountBook,List<Integer> id) throws WMSServiceException;
    void reject(String accountBook,List<Integer> id) throws WMSServiceException;

    void test();
}
