package com.wms.services.warehouse.service;

import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.WarehouseEntryItem;
import com.wms.utilities.model.WarehouseEntryItemView;
import com.wms.utilities.service.BaseService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface WarehouseEntryItemService
        extends BaseService<WarehouseEntryItem, WarehouseEntryItemView> {
    int[] add1(String accountBook, WarehouseEntryItem[] warehouseEntryItems) throws WMSServiceException;
    void update(String accountBook, WarehouseEntryItem[] warehouseEntryItems, boolean allowUpdateInspectionAmount) throws WMSServiceException;
    void reject(String accountBook, List<Integer> ids,Map<Integer,BigDecimal> idAndReturnAmount) throws WMSServiceException;
    void receive(String accountBook, List<Integer> ids,Map<Integer,BigDecimal> idAndReturnAmount) throws WMSServiceException;

    void update1(String accountBook, WarehouseEntryItem[] warehouseEntryItems) throws WMSServiceException;
    void update1(String accountBook, WarehouseEntryItem[] warehouseEntryItems, boolean allowUpdateInspectionAmount) throws WMSServiceException;
    void reject1(String accountBook, List<Integer> ids,Map<Integer,BigDecimal> idAndReturnAmount) throws WMSServiceException;
    void receive1(String accountBook, List<Integer> ids,Map<Integer,BigDecimal> idAndReturnAmount) throws WMSServiceException;

    WarehouseEntryItem get(String accountBook,int id) throws WMSServiceException;

    int WAIT_FOR_PUT_IN_STORAGE = 0;//待入库
    int BEING_INSPECTED = 1;//送检中
    int QUALIFIED = 2; //正品入库
    int UNQUALIFIED = 3; //不良品入库
}
