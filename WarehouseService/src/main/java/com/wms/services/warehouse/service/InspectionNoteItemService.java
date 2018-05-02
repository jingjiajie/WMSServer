package com.wms.services.warehouse.service;

import com.wms.utilities.model.InspectionNoteItem;
import com.wms.utilities.model.InspectionNoteItemView;
import com.wms.utilities.service.BaseService;

public interface InspectionNoteItemService
    extends BaseService<InspectionNoteItem,InspectionNoteItemView>{

    int STATE_NOT_INSPECTED = 0;
    int STATE_QUALIFIED = 1;
    int STATE_UNQUALIFIED = 2;
}
