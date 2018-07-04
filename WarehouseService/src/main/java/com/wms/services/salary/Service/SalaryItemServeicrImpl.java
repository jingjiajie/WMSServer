package com.wms.services.salary.Service;

import com.wms.services.salary.dao.SalaryItemDAO;
import com.wms.services.warehouse.service.WarehouseService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.SalaryItem;
import com.wms.utilities.model.SalaryItemView;
import com.wms.utilities.model.SalaryPeriod;
import com.wms.utilities.model.SalaryPeriodView;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Transactional
@Service
public class SalaryItemServeicrImpl  implements SalaryItemService{
    @Autowired
    SalaryItemDAO salaryItemDAO;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    SalaryTypeService salaryTypeService;
    public int[] add(String accountBook, SalaryItem[] salaryItems) throws WMSServiceException
    {

        for(int i=0;i<salaryItems.length;i++) {
            Validator validator = new Validator("薪金项目名称");
            validator.notnull().notEmpty().validate(salaryItems[i].getName());
        }

        for(int i=0;i<salaryItems.length;i++){
            for(int j=i+1;j<salaryItems.length;j++){
                String name=salaryItems[i].getName();
                if(name.equals(salaryItems[j].getName())){throw new WMSServiceException("薪金项目名称"+name+"在添加的列表中重复!");}
            }
        }
        //重复
        Stream.of(salaryItems).forEach((salaryPeriod)->{
            Condition cond = new Condition();
            cond.addCondition("name",new String[]{salaryPeriod.getName()});
            if(salaryItemDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("薪金项目名称："+salaryPeriod.getName()+"已经存在!");
            }
        });
        //外键检测
        Stream.of(salaryItems).forEach(
                (salaryItem)->{
                    if(this.warehouseService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ salaryItem.getWarehouseId()})).length == 0){
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",salaryItem.getWarehouseId()));
                    }
                    if(this.salaryTypeService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ salaryItem.getSalaryTypeId()})).length == 0){
                        throw new WMSServiceException(String.format("类别不存在，请重新提交！(%d)",salaryItem.getWarehouseId()));
                    }}
        );
        return salaryItemDAO.add(accountBook,salaryItems);
    }

    @Transactional
    public void update(String accountBook, SalaryItem[] salaryItems) throws WMSServiceException{
            for(int i=0;i<salaryItems.length;i++) {
                Validator validator = new Validator("薪金项目名称");
                validator.notnull().notEmpty().validate(salaryItems[i].getName());
            }
            for(int i=0;i<salaryItems.length;i++){
                for(int j=i+1;j<salaryItems.length;j++){
                    String name=salaryItems[i].getName();
                    if(name.equals(salaryItems[j].getName())){throw new WMSServiceException("薪金项目名称"+name+"在添加的列表中重复!");}
                }
            }
        Stream.of(salaryItems).forEach(
                (salaryItem)->{
                    if(this.salaryItemDAO.find(accountBook,
                            new Condition().addCondition("id",salaryItem.getId())).length == 0){
                        throw new WMSServiceException(String.format("要修改的项目不存在请确认后修改(%d)",salaryItem.getId()));
                    }
                }
        );
        for(int i=0;i<salaryItems.length;i++){
            Condition cond = new Condition();
            cond.addCondition("name",new String[]{salaryItems[i].getName()});
            cond.addCondition("id",new Integer[]{salaryItems[i].getId()}, ConditionItem.Relation.NOT_EQUAL);
            if(salaryItemDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("项目名称重复："+salaryItems[i].getName());
            }
        }
        //外键检测
        Stream.of(salaryItems).forEach(
                (salaryItem)->{
                    if(this.warehouseService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ salaryItem.getWarehouseId()})).length == 0){
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",salaryItem.getWarehouseId()));
                    }
                    if(this.salaryTypeService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ salaryItem.getSalaryTypeId()})).length == 0){
                        throw new WMSServiceException(String.format("类别不存在，请重新提交！(%d)",salaryItem.getWarehouseId()));
                    }}
        );
       salaryItemDAO.update(accountBook, salaryItems);
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
        for (int id : ids) {
            if (salaryItemDAO.find(accountBook, new Condition().addCondition("id", id)).length == 0) {
                throw new WMSServiceException(String.format("删除项目不存在，请重新查询！(%d)", id));
            }
        }
        try {
            salaryItemDAO.remove(accountBook, ids);
        } catch (Exception e) {
            throw new WMSServiceException("删除供薪金类别失败，如果薪金类别已经被引用，需要先删除引用的内容，才能删除薪金类别！");
        }
    }

    public SalaryItemView[] find(String accountBook, Condition cond) throws WMSServiceException{
        return this.salaryItemDAO.find(accountBook, cond);
    }

    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.salaryItemDAO.findCount(database,cond);
    }


}
