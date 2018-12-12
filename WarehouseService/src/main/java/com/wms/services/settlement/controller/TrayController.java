package com.wms.services.settlement.controller;

import com.wms.services.settlement.datastructures.ValidateTray;
import com.wms.utilities.controller.BaseController;
import com.wms.utilities.model.CommonData;

public interface TrayController extends BaseController<CommonData,CommonData> {
    void validateEntities(String accountBook, ValidateTray validateTray);
}
