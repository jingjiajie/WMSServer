package com.wms.utilities.dao;

import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.model.CommonData;

public class CommonDataDAOImpl implements CommonDataDAO {

    @Override
    public int[] add(String database, CommonData[] materials) throws WMSDAOException {
        return new int[0];
    }

    @Override
    public void update(String database, CommonData[] materials) throws WMSDAOException {

    }

    @Override
    public void remove(String database, int[] ids) throws WMSDAOException {

    }

    @Override
    public CommonData[] find(String database, Condition cond) throws WMSDAOException {
        return new CommonData[0];
    }
}
