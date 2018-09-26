package com.wms.services.settlement.service;

import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.Invoice;
import com.wms.utilities.model.InvoiceView;
import com.wms.utilities.service.BaseService;

import java.util.List;

public interface InvoiceService extends BaseService<Invoice,InvoiceView> {
    void confirm(String accountBook,List<Integer> ids) throws WMSServiceException;

    int To_be_confirmed = 0;
    int Confirmed = 1;
}
