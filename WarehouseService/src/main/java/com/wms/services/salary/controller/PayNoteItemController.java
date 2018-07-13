package com.wms.services.salary.controller;

import com.wms.services.salary.datestructures.CalculateTax;
import com.wms.services.salary.datestructures.PayNoteItemPay;
import com.wms.services.salary.datestructures.addAllItem;
import com.wms.utilities.controller.BaseController;
import com.wms.utilities.model.PayNoteItem;
import com.wms.utilities.model.PayNoteItemView;

public interface PayNoteItemController extends BaseController<PayNoteItem,PayNoteItemView> {
    void calculateTax(String accountBook, CalculateTax calculateTax);
    void realPayAll(String accountBook,PayNoteItemPay payNoteItemPay);
    void realPayPartItems(String accountBook,PayNoteItemView[] payNoteItemViews);
    void addAllItems( String accountBook,addAllItem addAllItem);
}
