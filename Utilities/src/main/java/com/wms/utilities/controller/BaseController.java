package com.wms.utilities.controller;

import org.springframework.http.ResponseEntity;

public interface BaseController<TTable,TView> {
    int[] add(String accountBook, TTable[] objs);
    void remove(String accountBook,String strIDs);
    void update(String accountBook,TTable objs[]);
    TView[] find(String accountBook, String condStr);
    long findCount(String accountBook,String condStr);
}
