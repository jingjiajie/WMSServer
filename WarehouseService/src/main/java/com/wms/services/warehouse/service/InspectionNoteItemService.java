package com.wms.services.warehouse.service;

import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.InspectionNoteItem;
import com.wms.utilities.model.InspectionNoteItemView;
import com.wms.utilities.service.BaseService;

public interface InspectionNoteItemService
    extends BaseService<InspectionNoteItem,InspectionNoteItemView>{

    int NOT_INSPECTED = 0;
    int QUALIFIED = 1;
    int UNQUALIFIED = 2;

    int[] add1(String accountBook, InspectionNoteItem[] objs) throws WMSServiceException;
    void update1(String accountBook, InspectionNoteItem[] objs) throws WMSServiceException;
    InspectionNoteItem get(String accountBook, int id);
}
