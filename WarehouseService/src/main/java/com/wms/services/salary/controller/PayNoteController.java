package com.wms.services.salary.controller;

import com.wms.utilities.controller.BaseController;
import com.wms.utilities.model.PayNote;
import com.wms.utilities.model.PayNoteView;

public interface PayNoteController extends
        BaseController<PayNote,PayNoteView>{
    void confirmToAccountTitle(String accountBook, int payNoteId,int personId);
    void realPayToAccountTitle(String accountBook,int payNoteId,int personId);
}
