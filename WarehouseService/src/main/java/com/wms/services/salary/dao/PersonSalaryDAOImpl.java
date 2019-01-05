package com.wms.services.salary.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.datastructures.OrderItem;
import com.wms.utilities.exceptions.ConditionException;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PersonSalaryDAOImpl extends BaseDAOImpl<PersonSalary, PersonSalaryView> implements PersonSalaryDAO {
    public PersonSalaryDAOImpl() {
        super(PersonSalary.class, PersonSalaryView.class, PersonSalary::getId);
    }

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public PersonSalaryWithSumAmount[] findSum(String accountBook, Condition condition) {
//        Session session = sessionFactory.getCurrentSession();
//        try {
//            session.createNativeQuery("USE " + accountBook + ";").executeUpdate();
//        } catch (Throwable ex) {
//            throw new DatabaseNotFoundException(accountBook);
//        }
//        try {
//            Query query = condition.createQuery(PersonSalaryWithSumAmount.class, session);
//            List<PersonSalaryWithSumAmount> resultList = query.list();
//            PersonSalaryWithSumAmount[] resultArray = (PersonSalaryWithSumAmount[]) Array.newInstance(PersonSalaryWithSumAmount.class,resultList.size());
//            resultList.toArray(resultArray);
//            return resultArray;
//        } catch (Throwable ex) {
//            throw new WMSDAOException(ex.getMessage());
//        }

        Session session = this.sessionFactory.getCurrentSession();
        session.flush();
        try {
            session.createNativeQuery("USE " + accountBook + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException(accountBook);
        }
        Query querySum = null;
        Query queryAll = null;
        String sqlNew = "select * from \n" +
                "(select id,WarehouseID,SalaryPeriodID,PersonID,SalaryItemID,sum(amount) as \"amount\",WarehouseName,PersonName,SalaryPeriodName,\"总金额\" as SalaryItemName\n" +
                ",Edited,SalaryTypeID,GiveOut,post,salaryTypeName from PersonSalaryView where GiveOut=1\n" +
                "GROUP BY PersonID,SalaryTypeID,WarehouseID,SalaryPeriodID) as PWS";
        String sqlPostfix = " where 1=1";
        sqlPostfix = this.addConditions(condition, sqlPostfix);
        session.flush();
        querySum = session.createNativeQuery(sqlNew + sqlPostfix, PersonSalaryWithSumAmount.class);
        queryAll = session.createNativeQuery("select * from PersonSalaryView as PWS " + sqlPostfix, PersonSalaryWithSumAmount.class);
        List<PersonSalaryWithSumAmount> resultListSum = querySum.list();
        List<PersonSalaryWithSumAmount> resultListAll = queryAll.list();
        resultListSum.addAll(resultListAll);
        PersonSalaryWithSumAmount[] resultArray = (PersonSalaryWithSumAmount[]) Array.newInstance(PersonSalaryWithSumAmount.class, resultListSum.size());
        resultListSum.toArray(resultArray);
        return resultArray;
    }

    public long findCountSum(String database, Condition cond) throws WMSDAOException {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + database + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException(database);
        }
        try {
            Query query = cond.createQuery(PersonSalaryWithSumAmount.class, session, true);
            long result = (long) query.list().get(0);
            return result;
        } catch (Throwable ex) {
            throw new WMSDAOException(ex.getMessage());
        }
    }

    private String addConditions(Condition condition, String sqlPostfix) {
        ConditionItem[] conditionItems = condition.getConditions();
        for (int i = 0; i < conditionItems.length; i++) {
            ConditionItem conditionItem = conditionItems[i];
            if (conditionItem.getKey().equals("salaryPeriodId")) {
                sqlPostfix = sqlPostfix + " and PWS.SalaryPeriodId=" + conditionItem.getValues()[0].toString();
            }
            if (conditionItem.getKey().equals("salaryTypeId")) {
                sqlPostfix = sqlPostfix + " and PWS.SalaryTypeId=" + conditionItem.getValues()[0].toString();
            }
        }
        return sqlPostfix;
    }
}
