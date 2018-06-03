package com.wms.services.warehouse.controller;


import com.wms.services.warehouse.datastructures.InspectArgs;
import com.wms.services.warehouse.datastructures.WarehouseEntryAndItems;
import com.wms.utilities.controller.BaseController;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.Warehouse;
import com.wms.utilities.model.WarehouseEntry;
import com.wms.utilities.model.WarehouseEntryView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

public interface WarehouseEntryController
        extends BaseController<WarehouseEntry,WarehouseEntryView>{
    List<Integer> inspect(String accountBook, InspectArgs inspectArgs) throws WMSServiceException;
    void updateState(String accountBook,List<Integer> ids) throws WMSServiceException;

    List<WarehouseEntryAndItems> getPreviewData(String accountBook,String strIDs);
    void receive(String accountBook,List<Integer> ids);
    void reject(String accountBook,List<Integer> ids);
}