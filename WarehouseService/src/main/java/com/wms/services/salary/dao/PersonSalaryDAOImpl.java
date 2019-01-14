package com.wms.services.salary.dao;

import com.wms.services.salary.datestructures.PersonSalaryViewGroupByTypeAndPeriod;
import com.wms.services.warehouse.datastructures.StockRecordViewAndSumGroupBySupplyId;
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
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class PersonSalaryDAOImpl extends BaseDAOImpl<PersonSalary, PersonSalaryView> implements PersonSalaryDAO {
    public PersonSalaryDAOImpl() {
        super(PersonSalary.class, PersonSalaryView.class, PersonSalary::getId);
    }

//    @Autowired
//    private SessionFactory sessionFactory;
//
//    @Override
//    public PersonSalaryView[] findSum(String accountBook, Condition condition) {
////        Session session = sessionFactory.getCurrentSession();
////        try {
////            session.createNativeQuery("USE " + accountBook + ";").executeUpdate();
////        } catch (Throwable ex) {
////            throw new DatabaseNotFoundException(accountBook);
////        }
////        try {
////            Query query = condition.createQuery(PersonSalaryWithSumAmount.class, session);
////            List<PersonSalaryWithSumAmount> resultList = query.list();
////            PersonSalaryWithSumAmount[] resultArray = (PersonSalaryWithSumAmount[]) Array.newInstance(PersonSalaryWithSumAmount.class,resultList.size());
////            resultList.toArray(resultArray);
////            return resultArray;
////        } catch (Throwable ex) {
////            throw new WMSDAOException(ex.getMessage());
////        }
//
////        Session session = this.sessionFactory.getCurrentSession();
////        session.flush();
////        try {
////            session.createNativeQuery("USE " + accountBook + ";").executeUpdate();
////        } catch (Throwable ex) {
////            throw new DatabaseNotFoundException(accountBook);
////        }
////        Query querySum = null;
////        Query queryAll = null;
////        String sqlNew = "select * from \n" +
////                "(select id,WarehouseID,SalaryPeriodID,PersonID,SalaryItemID,sum(amount) as \"amount\",WarehouseName,PersonName,SalaryPeriodName,\"总金额\" as SalaryItemName\n" +
////                ",Edited,SalaryTypeID,GiveOut,post,salaryTypeName from PersonSalaryView where GiveOut=1\n" +
////                "GROUP BY PersonID,SalaryTypeID,WarehouseID,SalaryPeriodID) as PWS";
////        String sqlPostfix = " where 1=1";
////        sqlPostfix = this.addConditions(condition, sqlPostfix);
////        session.flush();
////        queryAll = session.createNativeQuery("select * from PersonSalaryView as PWS " + sqlPostfix, PersonSalaryView.class);
////        querySum = session.createNativeQuery(sqlNew + sqlPostfix, PersonSalaryView.class);
////        List<PersonSalaryView> resultListAll = queryAll.list();
//        List<PersonSalaryView> personSalaryViewResult = new ArrayList<>();
//        PersonSalaryView[] personSalaryViews = this.find(accountBook, condition);
//        List<PersonSalaryViewGroupByTypeAndPeriod> personSalaryViewGroupByTypeAndPeriodArrayList = new ArrayList<>();
//        for (int i = 0; i < personSalaryViews.length; i++) {
//            PersonSalaryViewGroupByTypeAndPeriod personSalaryViewGroupByTypeAndPeriod = new PersonSalaryViewGroupByTypeAndPeriod();
//            StringBuffer stringBuffer = new StringBuffer();
//            stringBuffer.append(personSalaryViews[i].getPersonId());
//            stringBuffer.append(";");
//            stringBuffer.append(personSalaryViews[i].getSalaryPeriodId());
//            stringBuffer.append(";");
//            stringBuffer.append(personSalaryViews[i].getSalaryTypeId());
//            stringBuffer.append(";");
//            personSalaryViewGroupByTypeAndPeriod.setGroupCondition(stringBuffer.toString());
//            personSalaryViewGroupByTypeAndPeriod.setPersonSalaryViews(personSalaryViews[i]);
//            personSalaryViewGroupByTypeAndPeriodArrayList.add(personSalaryViewGroupByTypeAndPeriod);
//        }
//        PersonSalaryViewGroupByTypeAndPeriod[] resultArray = null;
//        resultArray = (PersonSalaryViewGroupByTypeAndPeriod[]) Array.newInstance(PersonSalaryViewGroupByTypeAndPeriod.class, personSalaryViewGroupByTypeAndPeriodArrayList.size());
//        personSalaryViewGroupByTypeAndPeriodArrayList.toArray(resultArray);
//        Map<String, List<PersonSalaryViewGroupByTypeAndPeriod>> personSalaryGroup = Stream.of(resultArray).collect(Collectors.groupingBy(PersonSalaryViewGroupByTypeAndPeriod::getGroupCondition));
//        Iterator<Map.Entry<String, List<PersonSalaryViewGroupByTypeAndPeriod>>> entries = personSalaryGroup.entrySet().iterator();
//        //将每组求和然后加到一个列表中
//        while (entries.hasNext()) {
//            Map.Entry<String, List<PersonSalaryViewGroupByTypeAndPeriod>> entry = entries.next();
//            List<PersonSalaryViewGroupByTypeAndPeriod> personSalaryViewGroupByTypeAndPeriods = entry.getValue();
//            PersonSalaryViewGroupByTypeAndPeriod[] resultArray1 = null;
//            resultArray1 = (PersonSalaryViewGroupByTypeAndPeriod[]) Array.newInstance(PersonSalaryViewGroupByTypeAndPeriod.class, personSalaryViewGroupByTypeAndPeriods.size());
//            personSalaryViewGroupByTypeAndPeriods.toArray(resultArray1);
//            BigDecimal amountAll = new BigDecimal(0);
//            PersonSalaryView personSalaryView = new PersonSalaryView();
//            for (int i = 0; i < resultArray1.length; i++) {
//                personSalaryView = resultArray1[i].getPersonSalaryView();
//                if (personSalaryView.getGiveOut() == 1)
//                    amountAll = amountAll.add(personSalaryView.getAmount());
//            }
//            personSalaryView.setAmount(amountAll);
//            personSalaryView.setSalaryItemName("总金额");
//            if (resultArray1.length != 0) {
//                personSalaryViewResult.add(personSalaryView);
//            }
//        }
//        PersonSalaryView[] personSalaryViewResultArray = (PersonSalaryView[]) Array.newInstance(PersonSalaryView.class, personSalaryViewResult.size());
//        personSalaryViewResult.toArray(personSalaryViewResultArray);
//        return personSalaryViewResultArray;
//    }
//
//    public long findCountSum(String database, Condition cond) throws WMSDAOException {
//        Session session = sessionFactory.getCurrentSession();
//        try {
//            session.createNativeQuery("USE " + database + ";").executeUpdate();
//        } catch (Throwable ex) {
//            throw new DatabaseNotFoundException(database);
//        }
//        try {
//            Query query = cond.createQuery(PersonSalaryWithSumAmount.class, session, true);
//            long result = (long) query.list().get(0);
//            return result;
//        } catch (Throwable ex) {
//            throw new WMSDAOException(ex.getMessage());
//        }
//    }
//
//    private String addConditions(Condition condition, String sqlPostfix) {
//        ConditionItem[] conditionItems = condition.getConditions();
//        for (int i = 0; i < conditionItems.length; i++) {
//            ConditionItem conditionItem = conditionItems[i];
//            if (conditionItem.getKey().equals("salaryPeriodId")) {
//                sqlPostfix = sqlPostfix + " and PWS.SalaryPeriodId=" + conditionItem.getValues()[0].toString();
//            }
//            if (conditionItem.getKey().equals("salaryTypeId")) {
//                sqlPostfix = sqlPostfix + " and PWS.SalaryTypeId=" + conditionItem.getValues()[0].toString();
//            }
//        }
//        return sqlPostfix;
//    }
}
