package com.wms.services.salary.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.model.PersonSalary;
import com.wms.utilities.model.PersonSalaryView;
import com.wms.utilities.model.PersonSalaryWithSumAmount;
import com.wms.utilities.model.StockRecordViewNewest;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Array;
import java.util.List;

@Repository
public class PersonSalaryDAOImpl extends BaseDAOImpl<PersonSalary,PersonSalaryView> implements PersonSalaryDAO {
    public PersonSalaryDAOImpl(){
        super(PersonSalary.class,PersonSalaryView.class,PersonSalary::getId);}

    @Autowired
    private SessionFactory sessionFactory;
    @Override
    public PersonSalaryWithSumAmount[] findSum(String accountBook, Condition condition){
        Session session = sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + accountBook + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException(accountBook);
        }
        try {
            Query query = condition.createQuery(PersonSalaryWithSumAmount.class, session);
            List<PersonSalaryWithSumAmount> resultList = query.list();
            PersonSalaryWithSumAmount[] resultArray = (PersonSalaryWithSumAmount[]) Array.newInstance(PersonSalaryWithSumAmount.class,resultList.size());
            resultList.toArray(resultArray);
            return resultArray;
        } catch (Throwable ex) {
            throw new WMSDAOException(ex.getMessage());
        }
    }

    public long findCountSum(String database, Condition cond) throws WMSDAOException {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + database + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException(database);
        }
        try {
            Query query = cond.createQuery(PersonSalaryWithSumAmount.class, session,true);
            long result = (long)query.list().get(0);
            return result;
        } catch (Throwable ex) {
            throw new WMSDAOException(ex.getMessage());
        }
    }
}
