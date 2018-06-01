package com.wms.services.warehouse.service;

import com.wms.utilities.model.InspectionNoteItem;
import com.wms.utilities.model.InspectionNoteItemView;
import com.wms.utilities.service.BaseService;

public interface InspectionNoteItemService
    extends BaseService<InspectionNoteItem,InspectionNoteItemView>{

    int NOT_INSPECTED = 0;
    int QUALIFIED = 1;
    int UNQUALIFIED = 2;

    InspectionNoteItem get(String accountBook, int id);
}
