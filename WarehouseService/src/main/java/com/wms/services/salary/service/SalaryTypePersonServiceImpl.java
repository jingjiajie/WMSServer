package com.wms.services.salary.service;

import com.wms.services.salary.dao.SalaryTypeDAO;
import com.wms.services.salary.dao.SalaryTypePersonDAO;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.SalaryType;
import com.wms.utilities.model.SalaryTypePerson;
import com.wms.utilities.model.SalaryTypePersonView;
import com.wms.utilities.model.SalaryTypeView;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Service
@Transactional
public class SalaryTypePersonServiceImpl implements SalaryTypePersonService{
    @Autowired
    SalaryTypePersonDAO salaryTypePersonDAO;


    public int[] add(String accountBook, SalaryTypePerson[] salaryTypePeople) throws WMSServiceException
    {

        for(int i=0;i<salaryTypePeople.length;i++){
            for(int j=i+1;j<salaryTypePeople.length;j++){
                int personId=salaryTypePeople[i].getPersonId();
                if(personId==(salaryTypePeople[j].getPersonId())){throw new WMSServiceException("人员名称"+"在添加的列表中重复!");}
            }
        }
        //重复
        Stream.of(salaryTypePeople).forEach((salaryTypePerson)->{
            Condition cond = new Condition();
            cond.addCondition("personId",salaryTypePerson.getPersonId());
            cond.addCondition("salaryTypeId",salaryTypePerson.getSalaryTypeId());
            if(salaryTypePersonDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("相同的人员已存在于此类型中!");
            }
        });
        return salaryTypePersonDAO.add(accountBook,salaryTypePeople);

    }

    @Transactional
    public void update(String accountBook, SalaryTypePerson[] salaryTypePeople) throws WMSServiceException{


        for(int i=0;i<salaryTypePeople.length;i++){
            for(int j=i+1;j<salaryTypePeople.length;j++){
                int personId=salaryTypePeople[i].getPersonId();
                if(personId==(salaryTypePeople[j].getPersonId())){throw new WMSServiceException("人员名称"+"在添加的列表中重复!");}
            }
        }
        //重复
        Stream.of(salaryTypePeople).forEach((salaryTypePerson)->{
            Condition cond = new Condition();
            cond.addCondition("personId",salaryTypePerson.getPersonId());
            cond.addCondition("salaryTypeId",salaryTypePerson.getSalaryTypeId());
            cond.addCondition("id",new Integer[]{salaryTypePerson.getId()}, ConditionItem.Relation.NOT_EQUAL);
            if(salaryTypePersonDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("相同的人员已存在于此类型中!");
            }
        });

        salaryTypePersonDAO.update(accountBook, salaryTypePeople);
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
        for (int id : ids) {
            if (salaryTypePersonDAO.find(accountBook, new Condition().addCondition("id", id)).length == 0) {
                throw new WMSServiceException(String.format("删除薪金类型人员不存在，请重新查询！(%d)", id));
            }
        }
        try {
            salaryTypePersonDAO.remove(accountBook, ids);
        } catch (Exception e) {
            throw new WMSServiceException("删除供薪金类型失败，如果薪金类型人员已经被引用，需要先删除引用的内容，才能删除新薪金类型人员！");
        }
    }

    public SalaryTypePersonView[] find(String accountBook, Condition cond) throws WMSServiceException{
        return this.salaryTypePersonDAO.find(accountBook, cond);
    }

    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.salaryTypePersonDAO.findCount(database,cond);
    }


}
