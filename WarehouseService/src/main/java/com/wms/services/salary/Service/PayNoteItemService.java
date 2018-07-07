package com.wms.services.salary.service;

import com.wms.services.salary.datestructures.CalculateTax;
import com.wms.utilities.model.PayNoteItem;
import com.wms.utilities.model.PayNoteItemView;
import com.wms.utilities.service.BaseService;

public interface PayNoteItemService extends BaseService<PayNoteItem,PayNoteItemView> {
    void calculateTax(String accountBook, CalculateTax calculateTax);
    void confirmItems(String accountBook,CalculateTax calculateTax);
    void realPay(String accountBook,CalculateTax calculateTax);
    //void addByPerson(String accountBook,int personId);
}
