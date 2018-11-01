package com.wms.services.settlement.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.SummaryDetails;
import com.wms.utilities.model.SummaryDetailsView;
import org.springframework.stereotype.Repository;

@Repository
public class SummaryDetailsDAOImpl
extends BaseDAOImpl<SummaryDetails,SummaryDetailsView>
implements SummaryDetailsDAO{
    public SummaryDetailsDAOImpl(){
        super(SummaryDetails.class,SummaryDetailsView.class,SummaryDetails::getId);}

}
