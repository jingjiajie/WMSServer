package com.wms.services.settlement.service;


import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.SettlementNoteItem;
import com.wms.utilities.model.SettlementNoteItemView;
import com.wms.utilities.service.BaseService;

import java.util.List;

public interface SettlementNoteItemService
        extends BaseService<SettlementNoteItem,SettlementNoteItemView>{
    public void confirm(String accountBook,List<Integer> ids) throws WMSServiceException ;

    int To_be_confirmed = 0;
    int Confirmed = 1;
}
