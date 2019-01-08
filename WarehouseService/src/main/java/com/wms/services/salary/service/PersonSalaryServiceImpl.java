package com.wms.services.salary.service;

import com.wms.services.ledger.service.PersonService;
import com.wms.services.salary.dao.PersonSalaryDAO;
import com.wms.services.salary.datestructures.AddPersonSalary;
import com.wms.services.salary.datestructures.PersonSalaryViewGroupByTypeAndPeriod;
import com.wms.services.salary.datestructures.SalaryItemTypeState;
import com.wms.services.warehouse.service.*;
import com.wms.utilities.ReflectHelper;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.datastructures.OrderItem;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import com.wms.utilities.vaildator.Validator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
@Service
public class PersonSalaryServiceImpl implements PersonSalaryService {
    @Autowired
    PersonSalaryDAO personSalaryDAO;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    SalaryPeriodService salaryPeriodService;
    @Autowired
    PersonService personService;
    @Autowired
    SalaryItemService salaryItemService;
    @Autowired
    SalaryTypePersonService salaryTypePersonService;
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    WarehouseEntryItemService warehouseEntryItemService;
    @Autowired
    InspectionNoteItemService inspectionNoteItemService;
    @Autowired
    TransferOrderItemService transferOrderItemService;
    @Autowired
    SalaryTypeService salaryTypeService;

    public int[] add(String accountBook, PersonSalary[] personSalaries) throws WMSServiceException {

        for (int i = 0; i < personSalaries.length; i++) {
            Validator validator = new Validator("薪金项目名称");
            validator.notnull().notEmpty().min(0).validate(personSalaries[i].getAmount());
        }

        for (int i = 0; i < personSalaries.length; i++) {
            for (int j = i + 1; j < personSalaries.length; j++) {
                if (personSalaries[i].getPersonId().equals(personSalaries[j].getPersonId()) && personSalaries[i].getWarehouseId().equals(personSalaries[j].getWarehouseId()) && personSalaries[i].getSalaryItemId().equals(personSalaries[j].getSalaryItemId()) && personSalaries[i].getSalaryPeriodId().equals(personSalaries[j].getSalaryPeriodId())) {
                    throw new WMSServiceException("相同人员、期间、仓库、项目的条目在列表中重复！");
                }
            }
        }
        //未编辑
        for (int i = 0; i < personSalaries.length; i++) {
            personSalaries[i].setEdited(0);//1为编辑过
        }
        //重复
        Stream.of(personSalaries).forEach((personSalary) -> {
            Condition cond = new Condition();
            cond.addCondition("warehouseId", personSalary.getWarehouseId());
            cond.addCondition("personId", personSalary.getPersonId());
            cond.addCondition("salaryItemId", personSalary.getSalaryItemId());
            cond.addCondition("salaryPeriodId", personSalary.getSalaryPeriodId());
            if (personSalaryDAO.find(accountBook, cond).length > 0) {
                throw new WMSServiceException("相同人员、期间、仓库、项目的条目已经存在！");
            }
        });
        //外键检测
        Stream.of(personSalaries).forEach(
                (personSalary) -> {
                    if (this.warehouseService.find(accountBook,
                            new Condition().addCondition("id", new Integer[]{personSalary.getWarehouseId()})).length == 0) {
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)", personSalary.getWarehouseId()));
                    }
                    if (this.salaryPeriodService.find(accountBook,
                            new Condition().addCondition("id", new Integer[]{personSalary.getSalaryPeriodId()})).length == 0) {
                        throw new WMSServiceException(String.format("期间不存在，请重新提交！(%d)", personSalary.getSalaryPeriodId()));
                    }
                    if (this.personService.find(accountBook,
                            new Condition().addCondition("id", new Integer[]{personSalary.getPersonId()})).length == 0) {
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)", personSalary.getPersonId()));
                    }
                    if (this.salaryItemService.find(accountBook,
                            new Condition().addCondition("id", new Integer[]{personSalary.getSalaryItemId()})).length == 0) {
                        throw new WMSServiceException(String.format("项目不存在，请重新提交！(%d)", personSalary.getSalaryItemId()));
                    }

                }
        );
        return personSalaryDAO.add(accountBook, personSalaries);
    }

    @Transactional
    public void update(String accountBook, PersonSalary[] personSalaries) throws WMSServiceException {
        for (int i = 0; i < personSalaries.length; i++) {
            Validator validator = new Validator("薪金项目名称");
            validator.notnull().notEmpty().min(0).validate(personSalaries[i].getAmount());
        }

        for (int i = 0; i < personSalaries.length; i++) {
            for (int j = i + 1; j < personSalaries.length; j++) {
                if (personSalaries[i].getPersonId().equals(personSalaries[j].getPersonId()) && personSalaries[i].getWarehouseId().equals(personSalaries[j].getWarehouseId()) && personSalaries[i].getSalaryItemId().equals(personSalaries[j].getSalaryItemId()) && personSalaries[i].getSalaryPeriodId().equals(personSalaries[j].getSalaryPeriodId())) {
                    throw new WMSServiceException("相同人员、期间、仓库、项目的条目在列表中重复！");
                }
            }
        }
        //变为已编辑
        for (int i = 0; i < personSalaries.length; i++) {
            personSalaries[i].setEdited(1);//1为编辑过
        }
        //重复
        Stream.of(personSalaries).forEach((personSalary) -> {
            Condition cond = new Condition();
            cond.addCondition("warehouseId", personSalary.getWarehouseId());
            cond.addCondition("personId", personSalary.getPersonId());
            cond.addCondition("salaryItemId", personSalary.getSalaryItemId());
            cond.addCondition("salaryPeriodId", personSalary.getSalaryPeriodId());
            cond.addCondition("id", personSalary.getId(), ConditionItem.Relation.NOT_EQUAL);
            PersonSalaryView[] personSalaryViews = personSalaryDAO.find(accountBook, cond);
            if (personSalaryDAO.find(accountBook, cond).length > 0) {
                throw new WMSServiceException("相同人员、期间、仓库、项目的条目已经存在！");
            }
        });
        //外键检测
        Stream.of(personSalaries).forEach(
                (personSalary) -> {
                    if (this.warehouseService.find(accountBook,
                            new Condition().addCondition("id", new Integer[]{personSalary.getWarehouseId()})).length == 0) {
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)", personSalary.getWarehouseId()));
                    }
                    if (this.salaryPeriodService.find(accountBook,
                            new Condition().addCondition("id", new Integer[]{personSalary.getSalaryPeriodId()})).length == 0) {
                        throw new WMSServiceException(String.format("期间不存在，请重新提交！(%d)", personSalary.getSalaryPeriodId()));
                    }
                    if (this.personService.find(accountBook,
                            new Condition().addCondition("id", new Integer[]{personSalary.getPersonId()})).length == 0) {
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)", personSalary.getPersonId()));
                    }
                    if (this.salaryItemService.find(accountBook,
                            new Condition().addCondition("id", new Integer[]{personSalary.getSalaryItemId()})).length == 0) {
                        throw new WMSServiceException(String.format("项目不存在，请重新提交！(%d)", personSalary.getSalaryItemId()));
                    }

                }
        );
        personSalaryDAO.update(accountBook, personSalaries);
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        for (int id : ids) {
            if (personSalaryDAO.find(accountBook, new Condition().addCondition("id", id)).length == 0) {
                throw new WMSServiceException(String.format("删除项目不存在，请重新查询！(%d)", id));
            }
        }
        try {
            personSalaryDAO.remove(accountBook, ids);
        } catch (Exception e) {
            throw new WMSServiceException("删除人员薪资失败，如果人员薪资已经被引用，需要先删除引用的内容，才能删除人员薪资！");
        }
    }

    public PersonSalaryView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.personSalaryDAO.find(accountBook, cond);
    }

    public PersonSalaryView[] findSum(String accountBook, Condition cond) throws WMSServiceException {
        List<PersonSalaryView> personSalaryViewResult = new ArrayList<>();
        PersonSalaryView[] personSalaryViews = this.find(accountBook, cond);
        List<PersonSalaryViewGroupByTypeAndPeriod> personSalaryViewGroupByTypeAndPeriodArrayList = new ArrayList<>();
        for (int i = 0; i < personSalaryViews.length; i++) {
            PersonSalaryViewGroupByTypeAndPeriod personSalaryViewGroupByTypeAndPeriod = new PersonSalaryViewGroupByTypeAndPeriod();
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(personSalaryViews[i].getPersonId());
            stringBuffer.append(";");
            stringBuffer.append(personSalaryViews[i].getSalaryPeriodId());
            stringBuffer.append(";");
            stringBuffer.append(personSalaryViews[i].getSalaryTypeId());
            stringBuffer.append(";");
            personSalaryViewGroupByTypeAndPeriod.setGroupCondition(stringBuffer.toString());
            personSalaryViewGroupByTypeAndPeriod.setPersonSalaryViews(personSalaryViews[i]);
            personSalaryViewGroupByTypeAndPeriodArrayList.add(personSalaryViewGroupByTypeAndPeriod);
        }
        PersonSalaryViewGroupByTypeAndPeriod[] resultArray = null;
        resultArray = (PersonSalaryViewGroupByTypeAndPeriod[]) Array.newInstance(PersonSalaryViewGroupByTypeAndPeriod.class, personSalaryViewGroupByTypeAndPeriodArrayList.size());
        personSalaryViewGroupByTypeAndPeriodArrayList.toArray(resultArray);
        Map<String, List<PersonSalaryViewGroupByTypeAndPeriod>> personSalaryGroup = Stream.of(resultArray).collect(Collectors.groupingBy(PersonSalaryViewGroupByTypeAndPeriod::getGroupCondition));
        Iterator<Map.Entry<String, List<PersonSalaryViewGroupByTypeAndPeriod>>> entries = personSalaryGroup.entrySet().iterator();
        //将每组求和然后加到一个列表中
        while (entries.hasNext()) {
            Map.Entry<String, List<PersonSalaryViewGroupByTypeAndPeriod>> entry = entries.next();
            List<PersonSalaryViewGroupByTypeAndPeriod> personSalaryViewGroupByTypeAndPeriods = entry.getValue();
            PersonSalaryViewGroupByTypeAndPeriod[] resultArray1 = null;
            resultArray1 = (PersonSalaryViewGroupByTypeAndPeriod[]) Array.newInstance(PersonSalaryViewGroupByTypeAndPeriod.class, personSalaryViewGroupByTypeAndPeriods.size());
            personSalaryViewGroupByTypeAndPeriods.toArray(resultArray1);
            BigDecimal amountAll = new BigDecimal(0);
            PersonSalaryView personSalaryView = new PersonSalaryView();
            for (int i = 0; i < resultArray1.length; i++) {
                personSalaryView = resultArray1[i].getPersonSalaryView();
                if (personSalaryView.getGiveOut() == 1)
                    amountAll = amountAll.add(personSalaryView.getAmount());
            }
            personSalaryView.setAmount(amountAll);
            personSalaryView.setSalaryItemName("总金额");
            if (resultArray1.length != 0) {
                personSalaryViewResult.add(personSalaryView);
            }
        }
        PersonSalaryView[] personSalaryViewResultArray = (PersonSalaryView[]) Array.newInstance(PersonSalaryView.class, personSalaryViewResult.size());
        personSalaryViewResult.toArray(personSalaryViewResultArray);
        return this.find(accountBook, cond);
    }

    public PersonSalary[] findTable(String accountBook, Condition cond) throws WMSServiceException {
        return this.personSalaryDAO.findTable(accountBook, cond);
    }

    public long findCount(String database, Condition cond) throws WMSServiceException {
        return this.personSalaryDAO.findCount(database, cond);
        //return this.personSalaryDAO.findCountSum(database, cond);
    }

    //把所有跟计价有关的人员薪资先删除，再添加
    public void refreshValuation(String accountBook, AddPersonSalary addPersonSalary) {
        Session session = this.sessionFactory.getCurrentSession();
        session.flush();
        int[] ids = null;
        try {
            session.createNativeQuery("USE " + accountBook + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException(accountBook);
        }
        try {
            Query query = null;
            String sql = "SELECT  p.* FROM PersonSalary as p where p.salaryPeriodId=:salaryPeriodId and p.warehouseId=:warehouseId " +
                    "and p.personId in (select a.personId from SalaryTypePerson as a WHERE a.salaryTypeId =:salaryTypeId) " +
                    "and p.salaryItemId in (select b.id from SalaryItem as b WHERE b.salaryTypeId =:salaryTypeId) " +
                    "and (SELECT s.type from SalaryItem as s where s.id=p.salaryItemId )=1";
            query = session.createNativeQuery(sql, PersonSalary.class);
            query.setParameter("salaryPeriodId", addPersonSalary.getSalaryPeriodId());
            query.setParameter("warehouseId", addPersonSalary.getWarehouseId());
            query.setParameter("salaryTypeId", addPersonSalary.getSalaryTypeId());
            PersonSalary[] resultArray = null;
            List<PersonSalary> resultList = query.list();
            resultArray = (PersonSalary[]) Array.newInstance(PersonSalary.class, resultList.size());
            resultList.toArray(resultArray);
            ids = new int[resultArray.length];
            for (int i = 0; i < resultArray.length; i++) {
                ids[i] = resultArray[i].getId();
            }
        } catch (Exception e) {
            throw new WMSServiceException("查询人员薪资出错！");
        }
        personSalaryDAO.remove(accountBook, ids);
        this.addValuation(accountBook, addPersonSalary);
    }

    private void addValuation(String accountBook, AddPersonSalary addPersonSalary) {
        SalaryTypePerson[] salaryTypePersons = salaryTypePersonService.findTable(accountBook, new Condition().addCondition("salaryTypeId", addPersonSalary.getSalaryTypeId()));
        if (salaryTypePersons.length == 0) {
            throw new WMSServiceException("此类型中无人员，无法添加！");
        }
        SalaryItem[] salaryItems = salaryItemService.findTable(accountBook, new Condition().addCondition("salaryTypeId", addPersonSalary.getSalaryTypeId()).addCondition("type",SalaryItemTypeState.VALUATION_SALARY));
        if (salaryItems.length == 0) {
            return;
        }
        SalaryPeriod[] salaryPeriods = salaryPeriodService.findTable(accountBook, new Condition().addCondition("id", addPersonSalary.getSalaryPeriodId()));
        if (salaryPeriods.length != 1) {
            throw new WMSServiceException("查询薪资期间错误！");
        }
        List<PersonSalary> personSalaryList = new ArrayList<>();
        for (SalaryTypePerson salaryTypePerson : salaryTypePersons) {
            for (SalaryItem salaryItem : salaryItems) {
                PersonSalary personSalary = new PersonSalary();
                personSalary.setPersonId(salaryTypePerson.getPersonId());
                BigDecimal amount = new BigDecimal(0);
                //先按人员查找入库单、送检单、移库单
                WarehouseEntryItemView[] warehouseEntryItemViews = warehouseEntryItemService.find(accountBook, new Condition().addCondition("personId", salaryTypePerson.getPersonId()).addCondition("warehouseEntryCreateTime", new Timestamp[]{salaryPeriods[0].getStartTime(), salaryPeriods[0].getEndTime()}, ConditionItem.Relation.BETWEEN));
                for (int i = 0; i < warehouseEntryItemViews.length; i++) {
                    amount = amount.add(warehouseEntryItemViews[i].getRealAmount());
                }
                InspectionNoteItemView[] inspectionNoteItemViews = inspectionNoteItemService.find(accountBook, new Condition().addCondition("personId", salaryTypePerson.getPersonId()).addCondition("inspectionNoteCreateTime", new Timestamp[]{salaryPeriods[0].getStartTime(), salaryPeriods[0].getEndTime()}, ConditionItem.Relation.BETWEEN));
                for (int i = 0; i < inspectionNoteItemViews.length; i++) {
                    amount = amount.add(inspectionNoteItemViews[i].getAmount());
                }
                TransferOrderItemView[] transferOrderItemViews = transferOrderItemService.find(accountBook, new Condition().addCondition("personId", salaryTypePerson.getPersonId()).addCondition("transferOrderCreateTime", new Timestamp[]{salaryPeriods[0].getStartTime(), salaryPeriods[0].getEndTime()}, ConditionItem.Relation.BETWEEN));
                for (int i = 0; i < transferOrderItemViews.length; i++) {
                    amount = amount.add(transferOrderItemViews[i].getRealAmount());
                }
                personSalary.setSalaryItemId(salaryItem.getId());
                personSalary.setSalaryPeriodId(addPersonSalary.getSalaryPeriodId());
                personSalary.setWarehouseId(addPersonSalary.getWarehouseId());
                personSalary.setAmount(amount);
                //没编辑过
                personSalary.setEdited(0);
                personSalaryList.add(personSalary);
            }
        }
        PersonSalary[] personSalaries = new PersonSalary[personSalaryList.size()];
        personSalaryList.toArray(personSalaries);
        personSalaryDAO.add(accountBook, personSalaries);
    }

    //把所有跟公式有关的人员薪资先删除，再添加
    public void refreshFormula(String accountBook, AddPersonSalary addPersonSalary) {
        Session session = this.sessionFactory.getCurrentSession();
        session.flush();
        int[] ids = null;
        try {
            session.createNativeQuery("USE " + accountBook + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException(accountBook);
        }
        try {
            Query query = null;
            String sql = "SELECT  p.* FROM PersonSalary as p where p.salaryPeriodId=:salaryPeriodId and p.warehouseId=:warehouseId and p.personId in (select a.personId from SalaryTypePerson as a WHERE a.salaryTypeId =:salaryTypeId) and p.salaryItemId in (select b.id from SalaryItem as b WHERE b.salaryTypeId =:salaryTypeId) and (SELECT s.type from SalaryItem as s where s.id=p.salaryItemId )=2";
            query = session.createNativeQuery(sql, PersonSalary.class);
            query.setParameter("salaryPeriodId", addPersonSalary.getSalaryPeriodId());
            query.setParameter("warehouseId", addPersonSalary.getWarehouseId());
            query.setParameter("salaryTypeId", addPersonSalary.getSalaryTypeId());
            PersonSalary[] resultArray = null;
            List<PersonSalary> resultList = query.list();
            resultArray = (PersonSalary[]) Array.newInstance(PersonSalary.class, resultList.size());
            resultList.toArray(resultArray);
            ids = new int[resultArray.length];
            for (int i = 0; i < resultArray.length; i++) {
                ids[i] = resultArray[i].getId();
            }
        } catch (Exception e) {
            throw new WMSServiceException("查询人员薪资出错！");
        }
        personSalaryDAO.remove(accountBook, ids);
        this.addFormula(accountBook, addPersonSalary);
    }

    //将一个类型的公式添加进去 不管删除
    private void addFormula(String accountBook, AddPersonSalary addPersonSalary) {
        List<PersonSalary> personSalaryList = new ArrayList<>();
        SalaryTypePerson[] salaryTypePersons = salaryTypePersonService.findTable(accountBook, new Condition().addCondition("salaryTypeId", addPersonSalary.getSalaryTypeId()));
        SalaryItem[] salaryItems = salaryItemService.findTable(accountBook, new Condition().addCondition("salaryTypeId", addPersonSalary.getSalaryTypeId()).addOrder("priority", OrderItem.Order.DESC));
        SalaryPeriod[] salaryPeriods = salaryPeriodService.findTable(accountBook, new Condition().addCondition("id", addPersonSalary.getSalaryPeriodId()));
        if (salaryPeriods.length != 1) {
            throw new WMSServiceException("查询薪资期间错误！");
        }
        //每次只添加一个类型
        for (SalaryTypePerson salaryTypePerson : salaryTypePersons) {
            //已经按优先级排序
            ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
            ScriptEngine nashorn = scriptEngineManager.getEngineByName("nashorn");
            for (SalaryItem salaryItem : salaryItems) {
                if (salaryItem.getIdentifier() == null) {
                    continue;
                }
                if (salaryItem.getIdentifier().equals("")) {
                    continue;
                }
                PersonSalary personSalary = new PersonSalary();
                personSalary.setPersonId(salaryTypePerson.getPersonId());
                String formula = "";
                String identifier = "";
                if (salaryItem.getType() == SalaryItemTypeState.Formula) {
                    formula = salaryItem.getIdentifier() + "=" + salaryItem.getFormula() + ";";
                    identifier = "var " + salaryItem.getIdentifier() + ";";
                } else if (salaryItem.getType() == SalaryItemTypeState.REGULAR_SALARY) {
                    formula = salaryItem.getIdentifier() + "=" + salaryItem.getDefaultAmount() + ";";
                    identifier = "var " + salaryItem.getIdentifier() + ";";
                } else if (salaryItem.getType() == SalaryItemTypeState.VALUATION_SALARY) {
                    formula = salaryItem.getIdentifier() + "=" + salaryItem.getDefaultAmount() + ";";
                    identifier = "var " + salaryItem.getIdentifier() + ";";
                }
                PersonSalary[] personSalaries = personSalaryDAO.findTable(accountBook, new Condition().addCondition("salaryItemId", salaryItem.getId()).addCondition("salaryPeriodId", addPersonSalary.getSalaryPeriodId()).addCondition("personId", salaryTypePerson.getPersonId()));
                if (personSalaries.length == 1) {
                    formula = salaryItem.getIdentifier() + "=" + personSalaries[0].getAmount();
                }
                BigDecimal result = null;
                try {
                    nashorn.eval(identifier);
                    result = GetBigDecimal.getBigDecimal(nashorn.eval(formula));
                } catch (Exception e) {
                    throw new WMSServiceException("请检查项目：(" + salaryItem.getName() + ") 公式中标识符是否定义、公式格式和相关项目的优先级是否正确!");
                }
                personSalary.setAmount(result);
                personSalary.setSalaryItemId(salaryItem.getId());
                personSalary.setSalaryPeriodId(addPersonSalary.getSalaryPeriodId());
                personSalary.setWarehouseId(addPersonSalary.getWarehouseId());
                //没编辑过
                personSalary.setEdited(0);
                if (salaryItem.getType() == SalaryItemTypeState.Formula) {
                    personSalaryList.add(personSalary);
                }
            }
        }
        PersonSalary[] personSalaries = new PersonSalary[personSalaryList.size()];
        personSalaryList.toArray(personSalaries);
        personSalaryDAO.add(accountBook, personSalaries);
    }

    public void addPersonSalaryBySalaryType(String accountBook, AddPersonSalary addPersonSalary) {
        Session session = this.sessionFactory.getCurrentSession();
        session.flush();
        try {
            session.createNativeQuery("USE " + accountBook + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException(accountBook);
        }
        try {
            Query query = null;
            String sql = "DELETE FROM PersonSalary  where salaryPeriodId=:salaryPeriodId and warehouseId=:warehouseId and personId in (select a.personId from SalaryTypePerson as a WHERE a.salaryTypeId =:salaryTypeId) and salaryItemId in (select b.id from SalaryItem as b WHERE b.salaryTypeId =:salaryTypeId) and edited=0";
            query = session.createNativeQuery(sql);
            query.setParameter("salaryPeriodId", addPersonSalary.getSalaryPeriodId());
            query.setParameter("warehouseId", addPersonSalary.getWarehouseId());
            query.setParameter("salaryTypeId", addPersonSalary.getSalaryTypeId());
            query.executeUpdate();
        } catch (Exception e) {
            throw new WMSServiceException("删除旧人员薪资失败！");
        }
        List<PersonSalary> personSalaryList = new ArrayList<>();
        SalaryTypePerson[] salaryTypePersons = salaryTypePersonService.findTable(accountBook, new Condition().addCondition("salaryTypeId", addPersonSalary.getSalaryTypeId()));
        if (salaryTypePersons.length == 0) {
            throw new WMSServiceException("此类型中无人员，无法添加！");
        }
        SalaryItem[] salaryItems = salaryItemService.findTable(accountBook, new Condition().addCondition("salaryTypeId", addPersonSalary.getSalaryTypeId()));
        if (salaryItems.length == 0) {
            throw new WMSServiceException("此类型中无薪资项目，无法添加！");
        }
        SalaryPeriod[] salaryPeriods = salaryPeriodService.findTable(accountBook, new Condition().addCondition("id", addPersonSalary.getSalaryPeriodId()));
        if (salaryPeriods.length != 1) {
            throw new WMSServiceException("查询薪资期间错误！");
        }
        //每次只添加一个类型
        for (SalaryTypePerson salaryTypePerson : salaryTypePersons) {
            for (SalaryItem salaryItem : salaryItems) {
                PersonSalary personSalary = new PersonSalary();
                personSalary.setPersonId(salaryTypePerson.getPersonId());
                //区分类型
                if (salaryItem.getType() == SalaryItemTypeState.REGULAR_SALARY) {
                    personSalary.setAmount(salaryItem.getDefaultAmount());
                } else if (salaryItem.getType() == SalaryItemTypeState.VALUATION_SALARY && salaryItem.getGiveOut() == SalaryItemTypeState.GIVE_OUT_ON) {
                    BigDecimal amount = new BigDecimal(0);
                    //先按人员查找入库单、送检单、移库单
                    WarehouseEntryItemView[] warehouseEntryItemViews = warehouseEntryItemService.find(accountBook, new Condition().addCondition("personId", salaryTypePerson.getPersonId()).addCondition("warehouseEntryCreateTime", new Timestamp[]{salaryPeriods[0].getStartTime(), salaryPeriods[0].getEndTime()}, ConditionItem.Relation.BETWEEN));
                    for (int i = 0; i < warehouseEntryItemViews.length; i++) {
                        amount = amount.add(warehouseEntryItemViews[i].getRealAmount());
                    }
                    InspectionNoteItemView[] inspectionNoteItemViews = inspectionNoteItemService.find(accountBook, new Condition().addCondition("personId", salaryTypePerson.getPersonId()).addCondition("inspectionNoteCreateTime", new Timestamp[]{salaryPeriods[0].getStartTime(), salaryPeriods[0].getEndTime()}, ConditionItem.Relation.BETWEEN));
                    for (int i = 0; i < inspectionNoteItemViews.length; i++) {
                        amount = amount.add(inspectionNoteItemViews[i].getAmount());
                    }
                    TransferOrderItemView[] transferOrderItemViews = transferOrderItemService.find(accountBook, new Condition().addCondition("personId", salaryTypePerson.getPersonId()).addCondition("transferOrderCreateTime", new Timestamp[]{salaryPeriods[0].getStartTime(), salaryPeriods[0].getEndTime()}, ConditionItem.Relation.BETWEEN));
                    for (int i = 0; i < transferOrderItemViews.length; i++) {
                        amount = amount.add(transferOrderItemViews[i].getRealAmount());
                    }
                    personSalary.setAmount(salaryItem.getDefaultAmount().multiply(amount));
                }
                personSalary.setSalaryItemId(salaryItem.getId());
                personSalary.setSalaryPeriodId(addPersonSalary.getSalaryPeriodId());
                personSalary.setWarehouseId(addPersonSalary.getWarehouseId());
                //没编辑过
                personSalary.setEdited(0);
                personSalaryList.add(personSalary);
            }
        }
        PersonSalary[] personSalaries = new PersonSalary[personSalaryList.size()];
        personSalaryList.toArray(personSalaries);
        personSalaryDAO.add(accountBook, personSalaries);
    }

    private SalaryPeriod findNewestSalaryPeriod(String accountBook, int warehouseId) {
        SalaryPeriod[] salaryPeriods = salaryPeriodService.findTable(accountBook, new Condition().addOrder("endTime", OrderItem.Order.DESC));
        if (salaryPeriods.length == 0) {
            throw new WMSServiceException("此仓库中无薪资期间！");
        }
        return salaryPeriods[0];
    }


    //先刷新公式 再刷新其他 所有id的条目都删除
    public void refreshPersonSalary(String accountBook, AddPersonSalary addPersonSalary) {
        int[] ids = (int[]) Array.newInstance(int.class, addPersonSalary.getPersonSalaryIds().size());
        for (int i = 0; i < ids.length; i++) {
            ids[i] = addPersonSalary.getPersonSalaryIds().get(i);
        }
        //全部删除
        personSalaryDAO.remove(accountBook, ids);
        //添加非公式条目
        this.updatePersonSalary(accountBook, addPersonSalary);
        //添加公式条目
        this.addFormula(accountBook, addPersonSalary);
    }

    //刷新一个区间的非公式条目
    private void updatePersonSalary(String accountBook, AddPersonSalary addPersonSalary) {
        SalaryItem[] salaryItems = this.salaryItemService.findTable(accountBook, new Condition().addCondition("salaryTypeId", addPersonSalary.getSalaryTypeId()));
        List<Integer> itemIds = new ArrayList<>();
        for (int i = 0; i < salaryItems.length; i++) {
            itemIds.add(salaryItems[i].getId());
        }
        //TODO 加人员
        PersonSalary[] personSalaryExist = null;
        if (itemIds.size() != 0) {
            personSalaryExist = this.findExistPersonSalary(accountBook, addPersonSalary);
        }
        List<PersonSalary> personSalaryList = new ArrayList<>();
        SalaryTypePerson[] salaryTypePersons = salaryTypePersonService.findTable(accountBook, new Condition().addCondition("salaryTypeId", addPersonSalary.getSalaryTypeId()));
        SalaryPeriod[] salaryPeriods = salaryPeriodService.findTable(accountBook, new Condition().addCondition("id", addPersonSalary.getSalaryPeriodId()));
        if (salaryPeriods.length != 1) {
            throw new WMSServiceException("查询薪资期间错误！");
        }
        //每次只添加一个类型
        for (SalaryTypePerson salaryTypePerson : salaryTypePersons) {
            for (SalaryItem salaryItem : salaryItems) {
                PersonSalary personSalary = new PersonSalary();
                personSalary.setPersonId(salaryTypePerson.getPersonId());
                //区分类型
                if (salaryItem.getType() == SalaryItemTypeState.REGULAR_SALARY) {
                    personSalary.setAmount(salaryItem.getDefaultAmount());
                } else if (salaryItem.getType() == SalaryItemTypeState.VALUATION_SALARY) {
                    BigDecimal amount = new BigDecimal(0);
                    //先按人员查找入库单、送检单、移库单
                    WarehouseEntryItemView[] warehouseEntryItemViews = warehouseEntryItemService.find(accountBook, new Condition().addCondition("personId", salaryTypePerson.getPersonId()).addCondition("warehouseEntryCreateTime", new Timestamp[]{salaryPeriods[0].getStartTime(), salaryPeriods[0].getEndTime()}, ConditionItem.Relation.BETWEEN));
                    for (int i = 0; i < warehouseEntryItemViews.length; i++) {
                        amount = amount.add(warehouseEntryItemViews[i].getRealAmount());
                    }
                    InspectionNoteItemView[] inspectionNoteItemViews = inspectionNoteItemService.find(accountBook, new Condition().addCondition("personId", salaryTypePerson.getPersonId()).addCondition("inspectionNoteCreateTime", new Timestamp[]{salaryPeriods[0].getStartTime(), salaryPeriods[0].getEndTime()}, ConditionItem.Relation.BETWEEN));
                    for (int i = 0; i < inspectionNoteItemViews.length; i++) {
                        amount = amount.add(inspectionNoteItemViews[i].getAmount());
                    }
                    TransferOrderItemView[] transferOrderItemViews = transferOrderItemService.find(accountBook, new Condition().addCondition("personId", salaryTypePerson.getPersonId()).addCondition("transferOrderCreateTime", new Timestamp[]{salaryPeriods[0].getStartTime(), salaryPeriods[0].getEndTime()}, ConditionItem.Relation.BETWEEN));
                    for (int i = 0; i < transferOrderItemViews.length; i++) {
                        amount = amount.add(transferOrderItemViews[i].getRealAmount());
                    }
                    personSalary.setAmount(salaryItem.getDefaultAmount().multiply(amount));
                }
                else if(salaryItem.getType()==SalaryItemTypeState.Formula){continue;}
                personSalary.setSalaryItemId(salaryItem.getId());
                personSalary.setSalaryPeriodId(addPersonSalary.getSalaryPeriodId());
                personSalary.setWarehouseId(addPersonSalary.getWarehouseId());
                personSalaryList.add(personSalary);
            }
        }
        PersonSalary[] personSalaries = new PersonSalary[personSalaryList.size()];
        personSalaryList.toArray(personSalaries);
        personSalaryDAO.add(accountBook, personSalaries);
    }

    public void addLastPeriod(String accountBook, AddPersonSalary addPersonSalary) {
        SalaryPeriodView[] salaryPeriodViews = salaryPeriodService.find(accountBook,
                new Condition().addOrder("endTime", OrderItem.Order.DESC));
        if (salaryPeriodViews.length == 0) {
            throw new WMSServiceException("无薪资期间，无法执行！");
        }
        if (salaryPeriodViews.length == 1) {
            throw new WMSServiceException("只有一个薪资期间，无法按上个期间生成！");
        }
        if (salaryPeriodViews[0].getId() != addPersonSalary.getSalaryPeriodId()) {
            throw new WMSServiceException("只有最新区间才能按上个期间生成！");
        }
        int lastPeriodId = salaryPeriodViews[1].getId();
        //查找上个期间所有的人员薪资
        PersonSalary[] personSalaries = this.personSalaryDAO.findTable(accountBook
                , new Condition().addCondition("salaryPeriodId", lastPeriodId));
        //删除当前区间所有人员薪资
        PersonSalary[] personSalariesCur = this.personSalaryDAO.findTable(accountBook
                , new Condition().addCondition("salaryPeriodId", addPersonSalary.getSalaryPeriodId()));
        List<Integer> curIds = Stream.of(personSalariesCur).map(item -> item.getId()).collect(Collectors.toList());
        personSalaryDAO.remove(accountBook, ReflectHelper.IntegerToIntArray(curIds));
        for (int i = 0; i < personSalaries.length; i++) {
            personSalaries[i].setSalaryPeriodId(addPersonSalary.getSalaryPeriodId());
        }
        personSalaryDAO.add(accountBook, personSalaries);
    }

    private PersonSalary[] findExistPersonSalary(String accountBook, AddPersonSalary addPersonSalary) {
        PersonSalary[] resultArray = null;
        Session session = this.sessionFactory.getCurrentSession();
        session.flush();
        try {
            session.createNativeQuery("USE " + accountBook + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException(accountBook);
        }
        try {
            Query query = null;
            String sql = "SELECT  p.* FROM PersonSalary as p where p.salaryPeriodId=:salaryPeriodId and p.warehouseId=:warehouseId and p.personId in (select a.personId from SalaryTypePerson as a WHERE a.salaryTypeId =:salaryTypeId) and p.salaryItemId in (select b.id from SalaryItem as b WHERE b.salaryTypeId =:salaryTypeId) and (SELECT s.type from SalaryItem as s where s.id=p.salaryItemId )!=2";
            query = session.createNativeQuery(sql, PersonSalary.class);
            query.setParameter("salaryPeriodId", addPersonSalary.getSalaryPeriodId());
            query.setParameter("warehouseId", addPersonSalary.getWarehouseId());
            query.setParameter("salaryTypeId", addPersonSalary.getSalaryTypeId());
            List<PersonSalary> resultList = query.list();
            resultArray = (PersonSalary[]) Array.newInstance(PersonSalary.class, resultList.size());
            resultList.toArray(resultArray);
        } catch (Exception e) {
            throw new WMSServiceException("查询人员薪资出错！");
        }
        return resultArray;
    }
}

