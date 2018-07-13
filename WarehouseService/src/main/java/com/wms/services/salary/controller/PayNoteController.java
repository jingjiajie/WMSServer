package com.wms.services.salary.controller;

import com.wms.services.salary.datestructures.addAllItem;
import com.wms.utilities.controller.BaseController;
import com.wms.utilities.model.PayNote;
import com.wms.utilities.model.PayNoteView;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface PayNoteController extends
        BaseController<PayNote,PayNoteView>{
    void confirmToAccountTitle(String accountBook, int payNoteId );
    void realPayToAccountTitle(String accountBook,int payNoteId);
}
