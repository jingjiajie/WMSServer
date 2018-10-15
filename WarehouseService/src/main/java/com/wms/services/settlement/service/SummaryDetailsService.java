package com.wms.services.settlement.service;

import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.SummaryDetails;
import com.wms.utilities.model.SummaryDetailsView;
import com.wms.utilities.service.BaseService;

public interface SummaryDetailsService
extends BaseService<SummaryDetails,SummaryDetailsView>{
    int[] addIn(String accountBook, SummaryDetails[] summaryDetails);
    SummaryDetails[] findTable(String accountBook, Condition condition);
    void updateIn(String accountBook, SummaryDetails[] summaryDetails);
}
