package com.wms.services.settlement.controller;

import com.wms.utilities.controller.BaseController;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.SettlementNoteItem;
import com.wms.utilities.model.SettlementNoteItemView;

import java.util.List;

public interface SettlementNoteItemController extends BaseController<SettlementNoteItem,SettlementNoteItemView> {
    public void confirm(String accountBook,List<Integer> ids) throws WMSServiceException;

    }
