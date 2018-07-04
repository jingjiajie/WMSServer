package com.wms.services.salary.Service;

import com.wms.services.WarehouseService;
import com.wms.services.salary.dao.SalaryPeriodDAO;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.SalaryPeriod;
import com.wms.utilities.model.SalaryPeriodView;
import com.wms.utilities.model.SalaryType;
import com.wms.utilities.model.SalaryTypeView;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Service
@Transactional
public class SalaryPeriodServiceImpl implements SalaryPeriodService {
    @Autowired
    SalaryPeriodDAO salaryPeriodDAO;
    @Autowired
    com.wms.services.warehouse.service.WarehouseService warehouseService;



    public int[] add(String accountBook, SalaryPeriod[] salaryPeriods) throws WMSServiceException
    {

        for(int i=0;i<salaryPeriods.length;i++) {
            Validator validator = new Validator("期间型名");
            validator.notnull().notEmpty().validate(salaryPeriods[i].getName());
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
            if(salaryPeriodDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("期间名："+salaryPeriod.getName()+"已经存在!");
            }
        });

        //外键检测
        Stream.of(salaryPeriods).forEach(
                (salaryPeriod)->{
                    if(this.warehouseService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ salaryPeriod.getWarehouseId()})).length == 0){
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",salaryPeriod.getWarehouseId()));
                    }}
        );
        return salaryPeriodDAO.add(accountBook,salaryPeriods);
    }

    @Transactional
    public void update(String accountBook, SalaryPeriod[] salaryPeriods) throws WMSServiceException{
        for(int i=0;i<salaryPeriods.length;i++) {
            Validator validator = new Validator("期间名称");
            validator.notnull().notEmpty().validate(salaryPeriods[i].getName());
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
                }
        );
        for(int i=0;i<salaryPeriods.length;i++){
            Condition cond = new Condition();
            cond.addCondition("name",new String[]{salaryPeriods[i].getName()});
            cond.addCondition("id",new Integer[]{salaryPeriods[i].getId()}, ConditionItem.Relation.NOT_EQUAL);
            if(salaryPeriodDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("期间名称重复："+salaryPeriods[i].getName());
            }
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

    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.salaryPeriodDAO.findCount(database,cond);
    }

}
