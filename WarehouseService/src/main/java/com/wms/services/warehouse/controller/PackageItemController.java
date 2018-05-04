package com.wms.services.warehouse.controller;

import com.wms.utilities.model.PackageItem;
import com.wms.utilities.model.PackageItemView;
import org.springframework.http.ResponseEntity;

public interface PackageItemController {
    ResponseEntity<int[]> add(String accountBook, PackageItem[] items);

    void update(String accountBook, PackageItem[] items);

    void remove(String accountBook, String strIDs);

    ResponseEntity<PackageItemView[]> find(String accountBook, String strCond);
}
