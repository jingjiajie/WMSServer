package com.wms.services.warehouse.service;

import com.wms.services.warehouse.datastructures.InspectFinishArgs;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.InspectionNote;
import com.wms.utilities.model.InspectionNoteView;
import com.wms.utilities.service.BaseService;

import java.util.List;

public interface InspectionNoteService
        extends BaseService<InspectionNote,InspectionNoteView> {
    void inspectFinish(String accountBook, InspectFinishArgs inspectFinishArgs) throws WMSServiceException;

    //0送检中 1部分送检完成 2全部送检完成
    int NOT_INSPECTED = 0;
    int PART_INSPECTED = 1;
    int ALL_INSPECTED = 2;

    void updateState(String accountBook, List<Integer> ids);
}
