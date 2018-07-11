package com.wms.services.salary.controller;

import com.wms.services.salary.datestructures.CalculateTax;
import com.wms.utilities.controller.BaseController;
import com.wms.utilities.model.PayNoteItem;
import com.wms.utilities.model.PayNoteItemView;

public interface PayNoteItemController extends BaseController<PayNoteItem,PayNoteItemView> {
    void calculateTax(String accountBook, CalculateTax calculateTax);
   // void confirmItems(String accountBook,CalculateTax calculateTax);
    //void realPay(String accountBook,CalculateTax calculateTax);
}
