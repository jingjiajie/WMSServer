package com.wms.services.salary.dao;
import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.SalaryPeriod;
import com.wms.utilities.model.SalaryPeriodView;
import com.wms.utilities.model.SalaryType;
import com.wms.utilities.model.SalaryTypeView;
import org.springframework.stereotype.Repository;
import com.wms.services.salary.dao.SalaryPeriodDAO;

@Repository
public class SalaryPeriodDAOImpl
        extends BaseDAOImpl<SalaryPeriod,SalaryPeriodView>
        implements SalaryPeriodDAO {
    public SalaryPeriodDAOImpl(){
        super(SalaryPeriod.class,SalaryPeriodView.class,SalaryPeriod::getId);}
}