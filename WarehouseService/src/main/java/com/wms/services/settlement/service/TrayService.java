package com.wms.services.settlement.service;

import com.wms.services.settlement.datastructures.ValidateTray;
import com.wms.utilities.model.CommonData;
import com.wms.utilities.service.BaseService;

public interface TrayService extends BaseService<CommonData,CommonData> {
    void validateEntities(String accountBook, ValidateTray validateTray);
}
