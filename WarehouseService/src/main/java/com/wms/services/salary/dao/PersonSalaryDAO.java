package com.wms.services.salary.dao;

import com.wms.utilities.dao.BaseDAO;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.model.PersonSalary;
import com.wms.utilities.model.PersonSalaryView;
import com.wms.utilities.model.PersonSalaryWithSumAmount;
import com.wms.utilities.model.StockRecordViewNewest;

public interface PersonSalaryDAO extends BaseDAO<PersonSalary,PersonSalaryView> {
//    PersonSalaryView[] findSum(String accountBook, Condition condition);
//    long findCountSum(String database,Condition cond) throws WMSDAOException;
}
