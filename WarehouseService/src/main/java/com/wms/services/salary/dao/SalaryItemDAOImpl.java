package com.wms.services.salary.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.*;

public class SalaryItemDAOImpl extends BaseDAOImpl<SalaryItem,SalaryItemView> implements SalaryItemDAO {
    public SalaryItemDAOImpl(){
        super(SalaryItem.class,SalaryItemView.class,SalaryItem::getId);}
}
