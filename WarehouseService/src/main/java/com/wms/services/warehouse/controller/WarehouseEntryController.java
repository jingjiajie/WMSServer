package com.wms.services.warehouse.controller;


import com.wms.services.warehouse.datastructures.InspectArgs;
import com.wms.services.warehouse.datastructures.WarehouseEntryAndItems;
import com.wms.utilities.controller.BaseController;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.WarehouseEntry;
import com.wms.utilities.model.WarehouseEntryView;

import java.util.List;

public interface WarehouseEntryController
        extends BaseController<WarehouseEntry,WarehouseEntryView>{
    List<Integer> inspect(String accountBook, InspectArgs inspectArgs) throws WMSServiceException;
    void updateState(String accountBook,List<Integer> ids) throws WMSServiceException;

    List<WarehouseEntryAndItems> getPreviewData(String accountBook,String strIDs);
    void receive(String accountBook,List<Integer> ids);
    void reject(String accountBook,List<Integer> ids);
}