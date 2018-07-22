package com.wms.services.salary.service;

import com.wms.services.ledger.service.PersonService;
import com.wms.services.salary.dao.PersonSalaryDAO;
import com.wms.services.salary.datestructures.AddPersonSalary;
import com.wms.services.warehouse.service.WarehouseService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        //重复
        Stream.of(personSalaries).forEach((personSalary)->{
            Condition cond = new Condition();
            cond.addCondition("warehouseId",personSalary.getWarehouseId());
            cond.addCondition("personId",personSalary.getPersonId());
            cond.addCondition("salaryItemId",personSalary.getSalaryItemId());
            cond.addCondition("salaryPeriodId",personSalary.getSalaryPeriodId());
            cond.addCondition("id",personSalary.getId());
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
        List<PersonSalary> personSalaryList=new ArrayList<>();
        SalaryTypePersonView[] salaryTypePersonViews=salaryTypePersonService.find(accountBook,new Condition().addCondition("salaryTypeId",addPersonSalary.getSalaryTypeId().toArray(), ConditionItem.Relation.IN));
       SalaryItemView[] salaryItemViews=salaryItemService.find(accountBook,new Condition().addCondition("salaryTypeId",addPersonSalary.getSalaryTypeId().toArray(), ConditionItem.Relation.IN));
        Map<Integer, List<SalaryTypePersonView>> groupByTypeIdPerson =
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
        }
        PersonSalary[] personSalaries=new PersonSalary[personSalaryList.size()];
    personSalaryList.toArray(personSalaries);
    personSalaryDAO.add(accountBook,personSalaries);
    }
}

