package com.wms.services.settlement.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.TrayThresholds;
import com.wms.utilities.model.TrayThresholdsView;
import org.springframework.stereotype.Repository;

@Repository
public class TrayThresholdsDAOImpl extends BaseDAOImpl<TrayThresholds,TrayThresholdsView>
implements TrayThresholdsDAO{
    public TrayThresholdsDAOImpl(){
        super(TrayThresholds.class,TrayThresholdsView.class,TrayThresholds::getId);}

}
