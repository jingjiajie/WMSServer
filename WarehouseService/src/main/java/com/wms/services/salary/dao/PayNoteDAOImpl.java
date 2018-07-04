package com.wms.services.salary.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.PayNote;
import com.wms.utilities.model.PayNoteView;
import org.springframework.stereotype.Repository;

@Repository

public class PayNoteDAOImpl extends BaseDAOImpl<PayNote,PayNoteView> implements PayNoteDAO {
    public PayNoteDAOImpl(){
        super(PayNote.class,PayNoteView.class,PayNote::getId);}
}
