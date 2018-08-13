package com.wms.services.salary.service;

import com.wms.services.ledger.service.PersonService;
import com.wms.services.salary.dao.PersonSalaryDAO;
import com.wms.services.salary.datestructures.AddPersonSalary;
import com.wms.services.salary.datestructures.SalaryItemTypeState;
import com.wms.services.warehouse.service.*;
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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
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

    public int[] add(String accountBook, PersonSalary[] personSalaries) throws WMSServiceException
    {

        for(int i=0;i<personSalaries.length;i++) {
            Validator validator = new Validator("薪金项目名称");
            validator.notnull().notEmpty().min(0).validate(personSalaries[i].getAmount());
        }

        for(int i=0;i<personSalaries.length;i++){
            for(int j=i+1;j<personSalaries.length;j++){
                if(personSalaries[i].getPersonId().equals(personSalaries[j].getPersonId())&&personSalaries[i].getWarehouseId().equals(personSalaries[j].getWarehouseId())&&personSalaries[i].getSalaryItemId().equals(personSalaries[j].getSalaryItemId())&&personSalaries[i].getSalaryPeriodId().equals(personSalaries[j].getSalaryPeriodId())){
                    throw new WMSServiceException("相同人员、期间、仓库、项目的条目在列表中重复！");}
            }
        }
        //重复
        Stream.of(personSalaries).forEach((personSalary)->{
            Condition cond = new Condition();
            cond.addCondition("warehouseId",personSalary.getWarehouseId());
            cond.addCondition("personId",personSalary.getPersonId());
            cond.addCondition("salaryItemId",personSalary.getSalaryItemId());
            cond.addCondition("salaryPeriodId",personSalary.getSalaryPeriodId());
            if(personSalaryDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("相同人员、期间、仓库、项目的条目已经存在！");
            }
        });
        //外键检测
        Stream.of(personSalaries).forEach(
                (personSalary)->{
                    if(this.warehouseService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ personSalary.getWarehouseId()})).length == 0){
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",personSalary.getWarehouseId()));
                    }
                    if(this.salaryPeriodService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ personSalary.getSalaryPeriodId()})).length == 0){
                        throw new WMSServiceException(String.format("期间不存在，请重新提交！(%d)",personSalary.getSalaryPeriodId()));
                    }
                    if(this.personService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ personSalary.getPersonId()})).length == 0){
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)",personSalary.getPersonId()));
                    }
                    if(this.salaryItemService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ personSalary.getSalaryItemId()})).length == 0){
                        throw new WMSServiceException(String.format("项目不存在，请重新提交！(%d)",personSalary.getSalaryItemId()));
                    }

                }
        );
        return personSalaryDAO.add(accountBook,personSalaries);
    }

    @Transactional
    public void update(String accountBook, PersonSalary[] personSalaries) throws WMSServiceException{
        for(int i=0;i<personSalaries.length;i++) {
            Validator validator = new Validator("薪金项目名称");
            validator.notnull().notEmpty().min(0).validate(personSalaries[i].getAmount());
        }

        for(int i=0;i<personSalaries.length;i++){
            for(int j=i+1;j<personSalaries.length;j++){
                if(personSalaries[i].getPersonId().equals(personSalaries[j].getPersonId())&&personSalaries[i].getWarehouseId().equals(personSalaries[j].getWarehouseId())&&personSalaries[i].getSalaryItemId().equals(personSalaries[j].getSalaryItemId())&&personSalaries[i].getSalaryPeriodId().equals(personSalaries[j].getSalaryPeriodId())){
                    throw new WMSServiceException("相同人员、期间、仓库、项目的条目在列表中重复！");}
            }
        }
        //变为已编辑
        for(int i=0;i<personSalaries.length;i++)
        {
            personSalaries[i].setEdited(1);//1为编辑过
        }
        //重复
        Stream.of(personSalaries).forEach((personSalary)->{
            Condition cond = new Condition();
            cond.addCondition("warehouseId",personSalary.getWarehouseId());
            cond.addCondition("personId",personSalary.getPersonId());
            cond.addCondition("salaryItemId",personSalary.getSalaryItemId());
            cond.addCondition("salaryPeriodId",personSalary.getSalaryPeriodId());
            cond.addCondition("id",personSalary.getId(), ConditionItem.Relation.NOT_EQUAL);
            if(personSalaryDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("相同人员、期间、仓库、项目的条目已经存在！");
            }
        });
        //外键检测
        Stream.of(personSalaries).forEach(
                (personSalary)->{
                    if(this.warehouseService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ personSalary.getWarehouseId()})).length == 0){
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",personSalary.getWarehouseId()));
                    }
                    if(this.salaryPeriodService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ personSalary.getSalaryPeriodId()})).length == 0){
                        throw new WMSServiceException(String.format("期间不存在，请重新提交！(%d)",personSalary.getSalaryPeriodId()));
                    }
                    if(this.personService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ personSalary.getPersonId()})).length == 0){
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)",personSalary.getPersonId()));
                    }
                    if(this.salaryItemService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ personSalary.getSalaryItemId()})).length == 0){
                        throw new WMSServiceException(String.format("项目不存在，请重新提交！(%d)",personSalary.getSalaryItemId()));
                    }

                }
        );

        personSalaryDAO.update(accountBook, personSalaries);
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
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

    public PersonSalaryView[] find(String accountBook, Condition cond) throws WMSServiceException{
        return this.personSalaryDAO.find(accountBook, cond);
    }

    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.personSalaryDAO.findCount(database,cond);
    }

    public void addPersonSalaryBySalaryType(String accountBook, AddPersonSalary addPersonSalary)
    {
        /*
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("(");
        for(int i=0;i<addPersonSalary.getSalaryTypeId().size();i++){ stringBuffer.append(addPersonSalary.getSalaryTypeId().toArray()[i]);
            if(i!=addPersonSalary.getSalaryTypeId().size()-1)
            {
                stringBuffer.append(",");
            }
        }
        stringBuffer.append(")");
        */
        Session session=this.sessionFactory.getCurrentSession();
        session.flush();
        try {
            session.createNativeQuery("USE " + accountBook + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException(accountBook);
        }
        try{
        Query query=null;
            String sql="DELETE FROM PersonSalary  where salaryPeriodId=:salaryPeriodId and warehouseId=:warehouseId and personId in (select a.personId from SalaryTypePerson as a WHERE a.salaryTypeId =:salaryTypeId  and salaryItemId in (select b.id from SalaryItem as b WHERE b.salaryTypeId =:salaryPeriodId)";
            query=session.createNativeQuery(sql);
            query.setParameter("salaryPeriodId",addPersonSalary.getSalaryPeriodId());
            query.setParameter("warehouseId",addPersonSalary.getWarehouseId());
            query.setParameter("salaryPeriodId",addPersonSalary.getSalaryPeriodId());
            query.executeUpdate();}
            catch (Exception e){
                throw new WMSServiceException("删除旧人员薪资失败！");
            }
        List<PersonSalary> personSalaryList=new ArrayList<>();
        SalaryTypePersonView[] salaryTypePersonViews=salaryTypePersonService.find(accountBook,new Condition().addCondition("salaryTypeId",addPersonSalary.getSalaryTypeId() ));
        if(salaryTypePersonViews.length==0){throw new WMSServiceException("此类型中无人员，无法添加！");}
        SalaryItemView[] salaryItemViews=salaryItemService.find(accountBook,new Condition().addCondition("salaryTypeId",addPersonSalary.getSalaryTypeId()));
        if(salaryItemViews.length==0){ throw new WMSServiceException("此类型中无薪资项目，无法添加！");}
        SalaryPeriodView[] salaryPeriodViews=salaryPeriodService.find(accountBook,new Condition().addCondition("id",addPersonSalary.getSalaryPeriodId()));
        if(salaryPeriodViews.length!=1){throw new WMSServiceException("查询薪资期间错误！");}
        //每次只添加一个类型
        for(SalaryTypePersonView salaryTypePersonView:salaryTypePersonViews){
            for(SalaryItemView salaryItemView:salaryItemViews) {
                PersonSalary personSalary = new PersonSalary();
                personSalary.setPersonId(salaryTypePersonView.getPersonId());
                //区分类型
                if(salaryItemView.getType()== SalaryItemTypeState.REGULAR_SALARY)
                {
                    personSalary.setAmount(salaryItemView.getDefaultAmount());
                }
                else
                {
                    BigDecimal amount=new BigDecimal(0);
                    //先按人员查找入库单、送检单、移库单
                    WarehouseEntryItemView[] warehouseEntryItemViews=warehouseEntryItemService.find(accountBook,new Condition().addCondition("personId",salaryTypePersonView.getPersonId()).addCondition("warehouseEntryCreateTime",new Timestamp[]{salaryPeriodViews[0].getStartTime(),salaryPeriodViews[0].getEndTime()}, ConditionItem.Relation.BETWEEN));
                    for(int i=0;i<warehouseEntryItemViews.length;i++){
                        amount=amount.add(warehouseEntryItemViews[i].getRealAmount());
                    }
                    InspectionNoteItemView[] inspectionNoteItemViews=inspectionNoteItemService.find(accountBook,new Condition().addCondition("personId",salaryTypePersonView.getPersonId()).addCondition("inspectionNoteCreateTime",new Timestamp[]{salaryPeriodViews[0].getStartTime(),salaryPeriodViews[0].getEndTime()}, ConditionItem.Relation.BETWEEN));
                    for(int i=0;i<inspectionNoteItemViews.length;i++){
                        amount=amount.add(inspectionNoteItemViews[i].getAmount());
                    }
                    TransferOrderItemView[] transferOrderItemViews=transferOrderItemService.find(accountBook,new Condition().addCondition("personId",salaryTypePersonView.getPersonId()).addCondition("transferOrderCreateTime",new Timestamp[]{salaryPeriodViews[0].getStartTime(),salaryPeriodViews[0].getEndTime()}, ConditionItem.Relation.BETWEEN));
                    for(int i=0;i<transferOrderItemViews.length;i++){
                        amount=amount.add(transferOrderItemViews[i].getRealAmount());
                    }
                    personSalary.setAmount(salaryItemView.getDefaultAmount().multiply(amount));
                }
                personSalary.setSalaryItemId(salaryItemView.getId());
                personSalary.setSalaryPeriodId(addPersonSalary.getSalaryPeriodId());
                personSalary.setWarehouseId(addPersonSalary.getWarehouseId());
                personSalaryList.add(personSalary);
            }
        }
/*       Map<Integer, List<SalaryTypePersonView>> groupByTypeIdPerson =  .addCondition("warehouseId",addPersonSalary.getWarehouseId()
                Stream.of(salaryTypePersonViews).collect(Collectors.groupingBy(SalaryTypePersonView::getSalaryTypeId));
        Map<Integer, List<SalaryItemView>> groupByTypeIdItem =
                Stream.of(salaryItemViews).collect(Collectors.groupingBy(SalaryItemView::getSalaryTypeId));
        for (Map.Entry<Integer, List<SalaryItemView>> entry : groupByTypeIdItem.entrySet()){
            for (Map.Entry<Integer, List<SalaryTypePersonView>> entry1 : groupByTypeIdPerson.entrySet()){
                if(entry.getKey().equals(entry1.getKey())){
                    List<SalaryTypePersonView> salaryTypePersonView=entry1.getValue();
                    SalaryTypePersonView[] salaryTypePersonViews1=new SalaryTypePersonView[salaryTypePersonView.size()];
                    salaryTypePersonView.toArray(salaryTypePersonViews1);
                    List<SalaryItemView> salaryItemViews1=entry.getValue();
                    SalaryItemView[] salaryItemViews2=new SalaryItemView[salaryItemViews1.size()];
                    salaryItemViews1.toArray(salaryItemViews2);
                    for(SalaryItemView salaryItem:salaryItemViews2){
                        for(SalaryTypePersonView salaryTypePerson:salaryTypePersonViews1) {
                            PersonSalary personSalary = new PersonSalary();
                            personSalary.setPersonId(salaryTypePerson.getPersonId());
                            personSalary.setAmount(salaryItem.getDefaultAmount());
                            personSalary.setSalaryItemId(salaryItem.getId());
                            personSalary.setSalaryPeriodId(addPersonSalary.getSalaryPeriodId());
                            personSalary.setWarehouseId(addPersonSalary.getWarehouseId());
                            personSalaryList.add(personSalary);
                        }
                    }
                }
            }
        }*/
        PersonSalary[] personSalaries=new PersonSalary[personSalaryList.size()];
    personSalaryList.toArray(personSalaries);
    personSalaryDAO.add(accountBook,personSalaries);
    }

    private SalaryPeriodView findNewestSalaryPeriod(String accountBook,int warehouseId)
    {
        SalaryPeriodView[] salaryPeriodViews=salaryPeriodService.find(accountBook,new Condition().addCondition("warehouseId",warehouseId).addOrder("startTime", OrderItem.Order.DESC));
        if(salaryPeriodViews.length==0)
        {
            throw new WMSServiceException("此仓库中无薪资期间！");
        }
        return salaryPeriodViews[0];
    }

    public void updatePersonSalary(String accountBook, AddPersonSalary addPersonSalary)
    {
        SalaryPeriodView salaryPeriodViewNewest=this.findNewestSalaryPeriod(accountBook,addPersonSalary.getWarehouseId());



    }

    public void updateNewestPeriodPerdonSalary(String accountBook,AddPersonSalary addPersonSalary)
    {
        SalaryPeriodView salaryPeriodViewNewest=this.findNewestSalaryPeriod(accountBook,addPersonSalary.getWarehouseId());
        Session session=this.sessionFactory.getCurrentSession();
        session.flush();
        try {
            session.createNativeQuery("USE " + accountBook + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException(accountBook);
        }
        try{
            Query query=null;
            // 诶编辑过的更新
            String sql="DELETE FROM PersonSalary  where salaryPeriodId=:salaryPeriodId and warehouseId=:warehouseId and personId in (select a.personId from SalaryTypePerson as a WHERE a.salaryTypeId =:salaryTypeId  and salaryItemId in (select b.id from SalaryItem as b WHERE b.salaryTypeId =:salaryPeriodId) and editaed=0";
            query=session.createNativeQuery(sql);
            query.setParameter("salaryPeriodId",addPersonSalary.getSalaryPeriodId());
            query.setParameter("warehouseId",addPersonSalary.getWarehouseId());
            query.setParameter("salaryPeriodId",addPersonSalary.getSalaryPeriodId());
            query.executeUpdate();}
        catch (Exception e){
            throw new WMSServiceException("删除旧人员薪资失败！");
        }
        List<PersonSalary> personSalaryList=new ArrayList<>();
        SalaryTypePersonView[] salaryTypePersonViews=salaryTypePersonService.find(accountBook,new Condition().addCondition("salaryTypeId",addPersonSalary.getSalaryTypeId() ));
        if(salaryTypePersonViews.length==0){throw new WMSServiceException("此类型中无人员，无法添加！");}
        SalaryItemView[] salaryItemViews=salaryItemService.find(accountBook,new Condition().addCondition("salaryTypeId",addPersonSalary.getSalaryTypeId()));
        if(salaryItemViews.length==0){ throw new WMSServiceException("此类型中无薪资项目，无法添加！");}
        SalaryPeriodView[] salaryPeriodViews=salaryPeriodService.find(accountBook,new Condition().addCondition("id",addPersonSalary.getSalaryPeriodId()));
        if(salaryPeriodViews.length!=1){throw new WMSServiceException("查询薪资期间错误！");}
        //每次只添加一个类型
        for(SalaryTypePersonView salaryTypePersonView:salaryTypePersonViews){
            for(SalaryItemView salaryItemView:salaryItemViews) {
                PersonSalary personSalary = new PersonSalary();
                personSalary.setPersonId(salaryTypePersonView.getPersonId());
                //区分类型
                if(salaryItemView.getType()== SalaryItemTypeState.REGULAR_SALARY)
                {
                    personSalary.setAmount(salaryItemView.getDefaultAmount());
                }
                else
                {
                    BigDecimal amount=new BigDecimal(0);
                    //先按人员查找入库单、送检单、移库单
                    WarehouseEntryItemView[] warehouseEntryItemViews=warehouseEntryItemService.find(accountBook,new Condition().addCondition("personId",salaryTypePersonView.getPersonId()).addCondition("warehouseEntryCreateTime",new Timestamp[]{salaryPeriodViews[0].getStartTime(),salaryPeriodViews[0].getEndTime()}, ConditionItem.Relation.BETWEEN));
                    for(int i=0;i<warehouseEntryItemViews.length;i++){
                        amount=amount.add(warehouseEntryItemViews[i].getRealAmount());
                    }
                    InspectionNoteItemView[] inspectionNoteItemViews=inspectionNoteItemService.find(accountBook,new Condition().addCondition("personId",salaryTypePersonView.getPersonId()).addCondition("inspectionNoteCreateTime",new Timestamp[]{salaryPeriodViews[0].getStartTime(),salaryPeriodViews[0].getEndTime()}, ConditionItem.Relation.BETWEEN));
                    for(int i=0;i<inspectionNoteItemViews.length;i++){
                        amount=amount.add(inspectionNoteItemViews[i].getAmount());
                    }
                    TransferOrderItemView[] transferOrderItemViews=transferOrderItemService.find(accountBook,new Condition().addCondition("personId",salaryTypePersonView.getPersonId()).addCondition("transferOrderCreateTime",new Timestamp[]{salaryPeriodViews[0].getStartTime(),salaryPeriodViews[0].getEndTime()}, ConditionItem.Relation.BETWEEN));
                    for(int i=0;i<transferOrderItemViews.length;i++){
                        amount=amount.add(transferOrderItemViews[i].getRealAmount());
                    }
                    personSalary.setAmount(salaryItemView.getDefaultAmount().multiply(amount));
                }
                personSalary.setSalaryItemId(salaryItemView.getId());
                personSalary.setSalaryPeriodId(addPersonSalary.getSalaryPeriodId());
                personSalary.setWarehouseId(addPersonSalary.getWarehouseId());
                personSalaryList.add(personSalary);
            }
        }
        PersonSalary[] personSalaries=new PersonSalary[personSalaryList.size()];
        personSalaryList.toArray(personSalaries);
        personSalaryDAO.add(accountBook,personSalaries);
    }
}

