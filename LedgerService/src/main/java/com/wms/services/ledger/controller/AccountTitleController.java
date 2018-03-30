package com.wms.services.ledger.controller;

import com.wms.services.ledger.model.AccountTitle;
import org.springframework.http.ResponseEntity;

public interface AccountTitleController {
    ResponseEntity<int[]> add(String accountBook,AccountTitle[] accountTitles);
    void remove(String accountBook,String strIDs);
    void update(String accountBook,AccountTitle accountTitles[]);
    ResponseEntity<AccountTitle[]> find(String accountBook,String condStr);
}
