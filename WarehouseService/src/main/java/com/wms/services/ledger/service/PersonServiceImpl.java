package com.wms.services.ledger.service;


import com.wms.services.ledger.dao.PersonDAO;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.Person;
import com.wms.utilities.model.PersonView;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Service
public class PersonServiceImpl implements PersonService {
    @Autowired
    PersonDAO personDAO;

    @Transactional
    public int[] add(String accountBook, Person[] persons) throws WMSServiceException {

        this.validateEntities(accountBook,persons);
        Object[] personNames = Stream.of(persons).map(Person::getName).toArray();
        int sameNamePersons = this.find(accountBook,new Condition().addCondition("name",personNames,ConditionItem.Relation.IN)).length;
        if(sameNamePersons > 0){
            throw new WMSServiceException("人员姓名不允许重复！");
        }
        return personDAO.add(accountBook, persons);
    }

    @Transactional
    public void update(String accountBook, Person[] persons) throws WMSServiceException {
        this.validateEntities(accountBook,persons);
        Stream.of(persons).forEach((person)->{
            if(this.find(accountBook,new Condition().addCondition("name",new String[]{person.getName()})
                    .addCondition("id",new Integer[]{person.getId()}, ConditionItem.Relation.NOT_EQUAL)).length > 0) {
                throw new WMSServiceException("人员名称：" + person.getName() +"在库存中已经存在!");
            }
        });
        personDAO.update(accountBook, persons);
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        personDAO.remove(accountBook, ids);
    }

    @Transactional
    public PersonView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.personDAO.find(accountBook, cond);
    }

    @Transactional
    public long findCount(String accountBook,Condition cond) throws WMSServiceException{
        return this.personDAO.findCount(accountBook,cond);
    }

    private void validateEntities(String accountBook,Person[] persons) throws WMSServiceException{
        Stream.of(persons).forEach((person -> {
            new Validator("人员姓名").notEmpty().validate(person.getName());
            new Validator("密码").notEmpty().validate(person.getPassword());
            new Validator("角色").notEmpty().validate(person.getRole());
            new Validator("权限字符串").notnull().validate(person.getAuthorityString());
        }));

        for(int i=0;i<persons.length;i++){
            for(int j=i+1;j<persons.length;j++){
                String name=persons[i].getName();
                if(name.equals(persons[j].getName())){throw new WMSServiceException("人员名称"+name+"在添加的列表中重复!");}
            }
        }
    }
}
