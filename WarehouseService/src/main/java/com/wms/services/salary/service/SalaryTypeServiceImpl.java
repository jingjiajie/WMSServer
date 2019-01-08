package com.wms.services.salary.service;

import com.wms.services.salary.dao.SalaryTypeDAO;
import com.wms.services.salary.datestructures.AddPersonSalary;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.SalaryType;
import com.wms.utilities.model.SalaryTypeView;
import com.wms.utilities.model.Warehouse;
import com.wms.utilities.model.WarehouseView;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Service
@Transactional
public class SalaryTypeServiceImpl implements SalaryTypeService {
    @Autowired
    SalaryTypeDAO salaryTypeDAO;
    @Autowired
    PersonSalaryService personSalaryService;


    public int[] add(String accountBook, SalaryType[] salaryTypes) throws WMSServiceException
    {
        for(int i=0;i<salaryTypes.length;i++) {
            Validator validator = new Validator("薪金类型名");
            validator.notnull().notEmpty().validate(salaryTypes[i].getName());
        }
        for(int i=0;i<salaryTypes.length;i++){
            for(int j=i+1;j<salaryTypes.length;j++){
                String name=salaryTypes[i].getName();
                if(name.equals(salaryTypes[j].getName())){throw new WMSServiceException("薪金类型名称"+name+"在添加的列表中重复!");}
            }
        }
        //重复
        Stream.of(salaryTypes).forEach((salaryType)->{
            Condition cond = new Condition();
            cond.addCondition("name",new String[]{salaryType.getName()}).addCondition("warehouseId",salaryType.getWarehouseId());
            if(salaryTypeDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("薪金类型名："+salaryType.getName()+"已经存在!");
            }
        });
        //新加类型需要更新最新期间的人员薪资，没用
        int[] ids= salaryTypeDAO.add(accountBook,salaryTypes);
        for(int i=0;i<ids.length;i++){
        AddPersonSalary addPersonSalary=new AddPersonSalary();
        addPersonSalary.setWarehouseId(salaryTypes[0].getWarehouseId());
        addPersonSalary.setSalaryTypeId(ids[i]);
        //this.personSalaryService.updateNewestPeriodPersonSalary(accountBook,addPersonSalary);
        }
        return ids;
    }

    @Transactional
    public void update(String accountBook, SalaryType[] salaryTypes) throws WMSServiceException{
        for(int i=0;i<salaryTypes.length;i++) {
            Validator validator = new Validator("薪金类型名");
            validator.notnull().notEmpty().validate(salaryTypes[i].getName());
        }
        for(int i=0;i<salaryTypes.length;i++){
            for(int j=i+1;j<salaryTypes.length;j++){
                String name=salaryTypes[i].getName();
                if(name.equals(salaryTypes[j].getName())){throw new WMSServiceException("薪金类型名称"+name+"在添加的列表中重复!");}
            }
        }
        Stream.of(salaryTypes).forEach(
                (salaryType)->{
                    if(this.salaryTypeDAO.find(accountBook,
                            new Condition().addCondition("id",salaryType.getId())).length == 0){
                        throw new WMSServiceException(String.format("要修改的项目不存在请确认后修改(%d)",salaryType.getId()));
                    }
                }
        );
        for(int i=0;i<salaryTypes.length;i++){
            Condition cond = new Condition();
            cond.addCondition("name",new String[]{salaryTypes[i].getName()});
            cond.addCondition("warehouseId",salaryTypes[i].getWarehouseId());
            cond.addCondition("id",new Integer[]{salaryTypes[i].getId()}, ConditionItem.Relation.NOT_EQUAL);
            if(salaryTypeDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("薪金类型名称重复："+salaryTypes[i].getName());
            }
        }
        salaryTypeDAO.update(accountBook, salaryTypes);
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
        for (int id : ids) {
            if (salaryTypeDAO.find(accountBook, new Condition().addCondition("id", id)).length == 0) {
                throw new WMSServiceException(String.format("删除薪金类型不存在，请重新查询！(%d)", id));
            }
        }
        try {
            salaryTypeDAO.remove(accountBook, ids);
        } catch (Exception e) {
            throw new WMSServiceException("删除供薪金类型失败，如果薪金类型已经被引用，需要先删除引用的内容，才能删除新薪金类型！");
        }
    }

    public SalaryTypeView[] find(String accountBook, Condition cond) throws WMSServiceException{
        return this.salaryTypeDAO.find(accountBook, cond);
    }

    public SalaryType[] findTable(String accountBook, Condition cond) throws WMSServiceException{
        return this.salaryTypeDAO.findTable(accountBook, cond);
    }

    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.salaryTypeDAO.findCount(database,cond);
    }


}
