package com.wms.services.salary.controller;

import com.wms.services.salary.datestructures.CalculateTax;
import com.wms.services.salary.datestructures.PayNoteItemPay;
import com.wms.services.salary.datestructures.AddAllItem;
import com.wms.utilities.controller.BaseController;
import com.wms.utilities.model.PayNoteItem;
import com.wms.utilities.model.PayNoteItemView;

import java.util.List;

public interface PayNoteItemController extends BaseController<PayNoteItem,PayNoteItemView> {
    void calculateTax(String accountBook, CalculateTax calculateTax);
    void realPayAll(String accountBook,PayNoteItemPay payNoteItemPay);
    void realPayPartItems(String accountBook,PayNoteItemView[] payNoteItemViews);
    void addAllItems( String accountBook,AddAllItem AddAllItem);
}
