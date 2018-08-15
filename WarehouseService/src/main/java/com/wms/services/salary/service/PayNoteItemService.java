package com.wms.services.salary.service;

import com.wms.services.salary.datestructures.CalculateTax;
import com.wms.services.salary.datestructures.PayNoteItemPay;
import com.wms.services.salary.datestructures.AddAllItem;
import com.wms.services.salary.datestructures.PayNoteItemState;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.PayNote;
import com.wms.utilities.model.PayNoteItem;
import com.wms.utilities.model.PayNoteItemView;
import com.wms.utilities.service.BaseService;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface PayNoteItemService extends BaseService<PayNoteItem,PayNoteItemView> {
    void calculateTax(String accountBook, CalculateTax calculateTax);
    void realPayAll(String accountBook, PayNoteItemPay payNoteItemPay);
    void realPayPartItems(String accountBook, PayNoteItemView[] payNoteItemViews);
    void addAllItem(String accountBook,AddAllItem AddAllItem);
    boolean judgeAllFinish(String accountBook,int state,int payNoteId);
    PayNoteItem[] findTable(String accountBook, Condition cond) throws WMSServiceException;
}
