package com.wms.services.settlement.controller;

import com.wms.utilities.controller.BaseController;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.Invoice;
import com.wms.utilities.model.InvoiceView;

public interface InvoiceController extends BaseController<Invoice,InvoiceView> {
    void confirm(String accountBook,int id) throws WMSServiceException;
}
