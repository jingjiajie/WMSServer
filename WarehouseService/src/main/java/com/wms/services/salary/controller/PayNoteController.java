package com.wms.services.salary.controller;

import com.wms.services.salary.datestructures.AccountSynchronize;
import com.wms.utilities.controller.BaseController;
import com.wms.utilities.model.AccountTitleView;
import com.wms.utilities.model.PayNote;
import com.wms.utilities.model.PayNoteView;
import org.springframework.web.bind.annotation.PathVariable;

public interface PayNoteController extends
        BaseController<PayNote,PayNoteView>{
    //void confirmToAccountTitle(String accountBook, AccountSynchronize accountSynchronize);
    void realPayToAccountTitle(String accountBook,AccountSynchronize accountSynchronize);
    AccountTitleView[] findSonAccountTitleForAssociation(String accountBook, String condStr);
}
