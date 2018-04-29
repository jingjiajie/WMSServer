package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.SupplierDAO;
import com.wms.utilities.model.Supplier;
import com.wms.utilities.model.SupplierView;
import com.wms.utilities.model.Supply;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.sql.Timestamp;
import java.util.stream.Stream;

@Service
@Transactional
public class SupplierServicesImpl implements SupplierServices{
    @Autowired
    SupplierDAO  supplierDAO;
    @Autowired
    SupplyService supplyService;
    @Autowired
    PersonService personService;
    @Autowired
    WarehouseService warehouseService;
@Override
    public int[] add(String accountBook, Supplier[] suppliers) throws WMSServiceException
    {
        for (int i=0;i<suppliers.length;i++) {
            Validator validator=new Validator("供应商名称");
            validator.notnull().validate(suppliers[i].getName());
            Validator validator1=new Validator("供应商代号");
            validator1.notnull().validate(suppliers[i].getNo());
        }

        Stream.of(suppliers).forEach((supplier)->{
            Condition cond = new Condition();
            cond.addCondition("name",new String[]{supplier.getName()});
            if(supplierDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("供应商名："+supplier.getName()+"已经存在!");
            }
        });
        Stream.of(suppliers).forEach((supplier)->{
            Condition cond = new Condition();
            cond.addCondition("no",new String[]{supplier.getNo()});
            if(supplierDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("供应代号："+supplier.getNo()+"已经存在!");
            }
        });

        //外键检测
        Stream.of(suppliers).forEach(
                (supplier)->{
                    if(this.warehouseService.find(accountBook,
                            new Condition().addCondition("id",supplier.getWarehouseId())).length == 0){
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",supplier.getWarehouseId()));
                    }
                    else  if(this.personService.find(accountBook,
                            new Condition().addCondition("id",supplier.getCreatePersonId())).length == 0)
                    {
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)",supplier.getCreatePersonId()));
                    }
                    if(supplier.getLastUpdatePersonId() != null && this.personService.find(accountBook,
                            new Condition().addCondition("id",supplier.getLastUpdatePersonId())).length == 0){
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)",supplier.getLastUpdatePersonId()));
                    }
                }
        );

        for (int i=0;i<suppliers.length;i++)
        {
            suppliers[i].setCreateTime(new Timestamp(System.currentTimeMillis()));
            suppliers[i].setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
        }

        return supplierDAO.add(accountBook,suppliers);
    }

@Override
    public void update(String accountBook, Supplier[] suppliers) throws WMSServiceException{

    for (int i=0;i<suppliers.length;i++) {
        Validator validator=new Validator("供应商名称");
        validator.notnull().validate(suppliers[i].getName());
        Validator validator1=new Validator("供应商代号");
        validator1.notnull().validate(suppliers[i].getNo());
    }

    for(int i=0;i<suppliers.length;i++){
        Condition cond = new Condition();
        cond.addCondition("name",new String[]{suppliers[i].getName()});
        cond.addCondition("id",new Integer[]{suppliers[i].getId()}, ConditionItem.Relation.NOT_EQUAL);
        if(supplierDAO.find(accountBook,cond).length > 0){
            throw new WMSServiceException("供应商名称重复："+suppliers[i].getName());
        }
    }
    Stream.of(suppliers).forEach((supplier)->{
        Condition cond = new Condition();
        cond.addCondition("no",new String[]{supplier.getNo()});
        if(supplierDAO.find(accountBook,cond).length > 0){
            throw new WMSServiceException("供应代号："+supplier.getNo()+"已经存在!");
        }
    });
        for (int i=0;i<suppliers.length;i++)
        {
                suppliers[i].setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
                //
                suppliers[i].setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
            supplierDAO.update(accountBook, suppliers);
    }

@Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
        try {
            supplierDAO.remove(accountBook, ids);
        }
        catch (Throwable ex){
            throw new WMSServiceException("删除供应商失败，如果供应商已经被引用，需要先删除引用的内容，才能删除该供应商");
        }
    }

    @Override
    public SupplierView[] find(String accountBook, Condition cond) throws WMSServiceException{
            return this.supplierDAO.find(accountBook, cond);
    }
}
