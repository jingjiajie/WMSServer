package com.wms.services.salary.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.PayNote;
import com.wms.utilities.model.PayNoteItem;
import com.wms.utilities.model.PayNoteItemView;
import com.wms.utilities.model.PayNoteView;
import org.springframework.stereotype.Repository;

@Repository
public class PayNoteItemDAOImpl extends BaseDAOImpl<PayNoteItem,PayNoteItemView> implements
PayNoteItemDAO{
    public PayNoteItemDAOImpl(){
        super(PayNoteItem.class,PayNoteItemView.class,PayNoteItem::getId);}
}
