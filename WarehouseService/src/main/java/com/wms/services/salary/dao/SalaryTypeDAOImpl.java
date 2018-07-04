package com.wms.services.salary.dao;
import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.SalaryType;
import com.wms.utilities.model.SalaryTypeView;
import org.springframework.stereotype.Repository;

@Repository
public class SalaryTypeDAOImpl
        extends BaseDAOImpl<SalaryType,SalaryTypeView>
        implements SalaryTypeDAO {
        public SalaryTypeDAOImpl(){
                super(SalaryType.class,SalaryTypeView.class,SalaryType::getId);}
}