package com.wms.services.settlement.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.TrayThresholds;
import com.wms.utilities.model.TrayThresholdsView;

public class TrayThresholdsDAOImpl extends BaseDAOImpl<TrayThresholds,TrayThresholdsView>
implements TrayThresholdsDAO{
    public TrayThresholdsDAOImpl(){
        super(TrayThresholds.class,TrayThresholdsView.class,TrayThresholds::getId);}

}
