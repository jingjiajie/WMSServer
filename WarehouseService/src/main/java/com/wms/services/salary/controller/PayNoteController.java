package com.wms.services.salary.controller;

import com.wms.services.salary.datestructures.AccountSynchronize;
import com.wms.utilities.controller.BaseController;
import com.wms.utilities.model.PayNote;
import com.wms.utilities.model.PayNoteView;

public interface PayNoteController extends
        BaseController<PayNote,PayNoteView>{
    void confirmToAccountTitle(String accountBook, AccountSynchronize accountSynchronize);
    void realPayToAccountTitle(String accountBook,AccountSynchronize accountSynchronize);
}
