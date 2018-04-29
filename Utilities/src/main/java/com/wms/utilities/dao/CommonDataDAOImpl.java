package com.wms.utilities.dao;

import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.model.CommonData;
import org.springframework.stereotype.Repository;

@Repository
public class CommonDataDAOImpl
        extends BaseDAOImpl<CommonData,CommonData>
        implements CommonDataDAO {

    public CommonDataDAOImpl(){
        super(CommonData.class,CommonData.class,CommonData::getId);
    }
}
