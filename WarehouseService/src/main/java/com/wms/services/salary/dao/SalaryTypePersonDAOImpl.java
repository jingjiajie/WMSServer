package com.wms.services.salary.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.SalaryType;
import com.wms.utilities.model.SalaryTypePerson;
import com.wms.utilities.model.SalaryTypePersonView;
import com.wms.utilities.model.SalaryTypeView;
import org.springframework.stereotype.Repository;

@Repository
public class SalaryTypePersonDAOImpl
        extends BaseDAOImpl<SalaryTypePerson,SalaryTypePersonView>
        implements SalaryTypePersonDAO {
    public SalaryTypePersonDAOImpl(){
        super(SalaryTypePerson.class,SalaryTypePersonView.class,SalaryTypePerson::getId);}
}
