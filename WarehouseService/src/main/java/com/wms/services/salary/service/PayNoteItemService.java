package com.wms.services.salary.service;

import com.wms.services.salary.datestructures.CalculateTax;
import com.wms.services.salary.datestructures.PayNoteItemPay;
import com.wms.services.salary.datestructures.AddAllItem;
import com.wms.utilities.model.PayNoteItem;
import com.wms.utilities.model.PayNoteItemView;
import com.wms.utilities.service.BaseService;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface PayNoteItemService extends BaseService<PayNoteItem,PayNoteItemView> {
    void calculateTax(String accountBook, CalculateTax calculateTax);
    void realPayAll(String accountBook, PayNoteItemPay payNoteItemPay);
    void realPayPartItems(String accountBook, List<Integer> payNoteItemId);
    void addAllItem(String accountBook,AddAllItem AddAllItem);
}
