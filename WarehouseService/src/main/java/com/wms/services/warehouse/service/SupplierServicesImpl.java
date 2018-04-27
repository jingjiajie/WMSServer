package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.SupplierDAO;
import com.wms.services.warehouse.model.Supplier;
import com.wms.services.warehouse.model.Supply;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.sql.Timestamp;

@Service
public class SupplierServicesImpl implements SupplierServices{
    @Autowired
    SupplierDAO  supplierDAO;
    @Autowired
    SupplyService supplyService;
    @Transactional
    public int[] add(String accountBook, Supplier[] suppliers) throws WMSServiceException
    {
        for (int i=0;i<suppliers.length;i++) {

            Validator validator=new Validator("供应商名称");
            validator.notnull().validate(suppliers[i].getName());

            Validator validator1=new Validator("供应商代号");
            validator1.notnull().validate(suppliers[i].getNo());

            //Validator validator2=new Validator("人员信息");
            //validator2.min(5).validate(suppliers[i].getCreatePersonId());

            if(suppliers[i].getWarehouseId()==0)
            {
                throw new WMSServiceException("仓库信息无法找到！");
            }
            Supplier[] suppliersRepeat=null;
            String supplierName = suppliers[i].getName();
            Condition condition = Condition.fromJson("{'conditions':[{'key':'Name','values':['"+supplierName+"'],'relation':'EQUAL'}],'orders':[{'key':'name','order':'ASC'}]}");
            try {
                suppliersRepeat = supplierDAO.find(accountBook, condition);
            } catch (DatabaseNotFoundException ex) {
                throw new WMSServiceException("Accountbook " + accountBook + " not found!");
            }
            if (suppliersRepeat.length > 0) {
               throw new WMSServiceException("供应商名 " + supplierName + " 已经存在！");
            }
        }
        for (int i=0;i<suppliers.length;i++)
        {
            //suppliers[i].setWarehouseId(1);
            //suppliers[i].setCreatePersonId(19);
            suppliers[i].setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        try
        { return supplierDAO.add(accountBook,suppliers);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }

    }

    @Transactional
    public void update(String accountBook, Supplier[] suppliers) throws WMSServiceException{
        for (int i=0;i<suppliers.length;i++)
        {
            if(suppliers[i].getName()==null)
            {
                throw new WMSServiceException("供应商名不能为空！");
            }
            if(suppliers[i].getNo()==null)
            {
                throw new WMSServiceException("供应商代号不能为空！");
            }
            Supplier[] suppliersRepet;
            String supplierName = suppliers[i].getName();
            Condition condition = Condition.fromJson("{\"conditions\":[{\"key\":\"name\",\"values\":[\"" + supplierName + "\"],\"relation\":\"EQUAL\"}], \"orders\":[{\"key\":\"name\",\"order\":\"ASC\"}]}");
            try{
                suppliersRepet = supplierDAO.find(accountBook,condition);
            }catch (DatabaseNotFoundException ex){
                throw new WMSServiceException("Accountbook "+accountBook+" not found!");
            }
            if(suppliersRepet.length>0)
            {
                if(suppliersRepet[1].getId()!=suppliers[i].getId())
                { throw new WMSServiceException("供应商名 " + supplierName + " 已经存在！");}
            }

                //suppliers[i].setWarehouseId(1);
                //suppliers[i].setCreatePersonId(19);
                suppliers[i].setLastUpdateTime(new Timestamp(System.currentTimeMillis()));

        }
        try {
            supplierDAO.update(accountBook, suppliers);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{

        try {
            supplierDAO.remove(accountBook, ids);
        } catch (DatabaseNotFoundException ex) {
            throw new WMSServiceException("Accountbook " + accountBook + " not found!");
        }
        catch (Throwable ex){
            throw new WMSServiceException("删除供应商失败，如果供应商已经被引用，需要先删除引用的内容，才能删除该供应商");
        }
    }
    @Transactional
    public Supplier[] find(String accountBook, Condition cond) throws WMSServiceException{
        try {
            return this.supplierDAO.find(accountBook, cond);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }
}
