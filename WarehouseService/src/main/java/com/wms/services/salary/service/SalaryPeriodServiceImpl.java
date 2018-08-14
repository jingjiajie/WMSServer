package com.wms.services.salary.service;

import com.wms.services.salary.dao.SalaryPeriodDAO;
import com.wms.services.salary.datestructures.AddPersonSalary;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.SalaryPeriod;
import com.wms.utilities.model.SalaryPeriodView;
import com.wms.utilities.model.TaxItem;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional
public class SalaryPeriodServiceImpl implements SalaryPeriodService {
    @Autowired
    SalaryPeriodDAO salaryPeriodDAO;
    @Autowired
    com.wms.services.warehouse.service.WarehouseService warehouseService;
    @Autowired
    PersonSalaryService personSalaryService;



    public int[] add(String accountBook, SalaryPeriod[] salaryPeriods) throws WMSServiceException
    {

        for(int i=0;i<salaryPeriods.length;i++) {
            Validator validator = new Validator("期间型名");
            validator.notnull().notEmpty().validate(salaryPeriods[i].getName());
            Validator validator1 = new Validator("期间截止时间");
            validator1.notnull().notEmpty().validate(salaryPeriods[i].getEndTime());
            Validator validator2 = new Validator("期间起始时间");
            validator2.notnull().notEmpty().validate(salaryPeriods[i].getStartTime());
        }

        for(int i=0;i<salaryPeriods.length;i++){
            for(int j=i+1;j<salaryPeriods.length;j++){
                String name=salaryPeriods[i].getName();
                if(name.equals(salaryPeriods[j].getName())){throw new WMSServiceException("期间名称"+name+"在添加的列表中重复!");}
            }
        }
        //重复
        Stream.of(salaryPeriods).forEach((salaryPeriod)->{
            Condition cond = new Condition();
            cond.addCondition("name",new String[]{salaryPeriod.getName()});
            cond.addCondition("warehouseId",salaryPeriod.getWarehouseId());
            if(salaryPeriodDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("期间名："+salaryPeriod.getName()+"已经存在!");
            }
            if(salaryPeriod.getEndTime().compareTo(salaryPeriod.getStartTime())<0){throw new WMSServiceException("期间："+salaryPeriod.getName()+"的截止时间必须在开始时间之后!");}
            this.validateEndTime(accountBook,salaryPeriod);
        });

        //外键检测
        Stream.of(salaryPeriods).forEach(
                (salaryPeriod)->{
                    if(this.warehouseService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ salaryPeriod.getWarehouseId()})).length == 0){
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",salaryPeriod.getWarehouseId()));
                    }}
        );
        int[] ids= salaryPeriodDAO.add(accountBook,salaryPeriods);
        this.validateOverlap(accountBook,salaryPeriods[0].getWarehouseId());
        for(int i=0;i<ids.length;i++){
            AddPersonSalary addPersonSalary=new AddPersonSalary();
            addPersonSalary.setWarehouseId(salaryPeriods[0].getWarehouseId());
            addPersonSalary.setSalaryTypeId(ids[i]);
            this.personSalaryService.updateNewestPeriodPersonSalary(accountBook,addPersonSalary);}
        return ids;
    }

    @Transactional
    public void update(String accountBook, SalaryPeriod[] salaryPeriods) throws WMSServiceException{
        for(int i=0;i<salaryPeriods.length;i++) {
            Validator validator = new Validator("期间名称");
            validator.notnull().notEmpty().validate(salaryPeriods[i].getName());
            Validator validator1 = new Validator("期间截止时间");
            validator1.notnull().notEmpty().validate(salaryPeriods[i].getEndTime());
            Validator validator2 = new Validator("期间起始时间");
            validator2.notnull().notEmpty().validate(salaryPeriods[i].getStartTime());
        }

        for(int i=0;i<salaryPeriods.length;i++){
            for(int j=i+1;j<salaryPeriods.length;j++){
                String name=salaryPeriods[i].getName();
                if(name.equals(salaryPeriods[j].getName())){throw new WMSServiceException("期间名称"+name+"在添加的列表中重复!");}
            }
        }
        Stream.of(salaryPeriods).forEach(
                (salaryPeriod)->{
                    if(this.salaryPeriodDAO.find(accountBook,
                            new Condition().addCondition("id",salaryPeriod.getId())).length == 0){
                        throw new WMSServiceException(String.format("要修改的项目不存在请确认后修改(%d)",salaryPeriod.getId()));
                    }
                    this.validateEndTime(accountBook,salaryPeriod);
                }
        );
        for(int i=0;i<salaryPeriods.length;i++){
            Condition cond = new Condition();
            cond.addCondition("name",new String[]{salaryPeriods[i].getName()});
            cond.addCondition("id",new Integer[]{salaryPeriods[i].getId()}, ConditionItem.Relation.NOT_EQUAL);
            cond.addCondition("warehouseId",salaryPeriods[i].getWarehouseId());
            if(salaryPeriodDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("期间名称重复："+salaryPeriods[i].getName());
            }
            if(salaryPeriods[i].getEndTime().compareTo(salaryPeriods[i].getStartTime())<0){throw new WMSServiceException("期间："+salaryPeriods[i].getName()+"的截止时间必须在开始时间之后!");}
        }
        //外键检测
        Stream.of(salaryPeriods).forEach(
                (salaryPeriod)->{
                    if(this.warehouseService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ salaryPeriod.getWarehouseId()})).length == 0){
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",salaryPeriod.getWarehouseId()));
                    }}
        );
        salaryPeriodDAO.update(accountBook, salaryPeriods);
        this.validateOverlap(accountBook,salaryPeriods[0].getWarehouseId());
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
        for (int id : ids) {
            if (salaryPeriodDAO.find(accountBook, new Condition().addCondition("id", id)).length == 0) {
                throw new WMSServiceException(String.format("删除期间不存在，请重新查询！(%d)", id));
            }
        }
        try {
            salaryPeriodDAO.remove(accountBook, ids);
        } catch (Exception e) {
            throw new WMSServiceException("删除供薪金期间失败，如果薪金期间已经被引用，需要先删除引用的内容，才能删除薪金期间！");
        }
    }

    public SalaryPeriodView[] find(String accountBook, Condition cond) throws WMSServiceException{
        return this.salaryPeriodDAO.find(accountBook, cond);
    }

    public SalaryPeriod[] findTable(String accountBook, Condition cond) throws WMSServiceException{
        return this.salaryPeriodDAO.findTable(accountBook, cond);
    }

    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.salaryPeriodDAO.findCount(database,cond);
    }

    private void validateOverlap(String accountBook,int warehouseId)
    {
        SalaryPeriod[] salaryPeriods=this.salaryPeriodDAO.findTable(accountBook,new Condition().addCondition("warehouseId",warehouseId));
        List<SalaryPeriod> salaryPeriodList= Arrays.asList(salaryPeriods);
        salaryPeriodList.stream().sorted(Comparator.comparing(SalaryPeriod::getStartTime)).reduce((last, cur) -> {
            if (last.getEndTime().compareTo(cur.getStartTime())>=0){
                throw new WMSServiceException("薪资区间不能重叠！ 期间： "+last.getName()+" 与 "+cur.getName()+"的区间重复！");
            }
            return cur;
        });
    }

    private void validateEndTime(String accountBook,SalaryPeriod salaryPeriod)
    {
        SalaryPeriod[] salaryPeriods=this.salaryPeriodDAO.findTable(accountBook,new Condition().addCondition("warehouseId",salaryPeriod.getWarehouseId()).addCondition("endTime",salaryPeriod.getStartTime(), ConditionItem.Relation.GREATER_THAN_OR_EQUAL_TO));
        if(salaryPeriods.length!=0){
            throw new WMSServiceException("期间:"+salaryPeriod.getName()+"的起始时间必须大于所有期间的截至时间:"+salaryPeriods[0].getEndTime());
        }
    }
}
