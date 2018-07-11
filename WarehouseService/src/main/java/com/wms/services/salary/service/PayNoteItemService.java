package com.wms.services.salary.service;

import com.wms.services.salary.datestructures.CalculateTax;
import com.wms.services.salary.datestructures.PayNoteItemPay;
import com.wms.utilities.model.PayNoteItem;
import com.wms.utilities.model.PayNoteItemView;
import com.wms.utilities.service.BaseService;

public interface PayNoteItemService extends BaseService<PayNoteItem,PayNoteItemView> {
    void calculateTax(String accountBook, CalculateTax calculateTax);
    //void confirmItems(String accountBook,CalculateTax calculateTax);
    void realPayAll(String accountBook, PayNoteItemPay payNoteItemPay);
    void realPayPartItems(String accountBook, PayNoteItemView[] payNoteItemViews);
}
