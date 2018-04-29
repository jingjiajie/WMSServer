package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.WarehouseDAO;
import com.wms.utilities.model.Supply;
import com.wms.utilities.model.Warehouse;
import com.wms.utilities.model.WarehouseView;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.vaildator.Validator;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Service
public class WarehouseServiceImpl implements WarehouseService{
    @Autowired
    WarehouseDAO warehouseDAO;
    @Autowired
    SupplyService supplyService;
    @Transactional
    public int[] add(String accountBook, Warehouse[] warehouses) throws WMSServiceException
    {

       for(int i=0;i<warehouses.length;i++){
           Validator validator=new Validator("仓库名");
           validator.notnull().validate(warehouses[i].getName());
       }
        //重复
        Stream.of(warehouses).forEach((warehouse)->{
            Condition cond = new Condition();
            cond.addCondition("name",new String[]{warehouse.getName()});
            if(warehouseDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("仓库名："+warehouse.getName()+"已经存在!");
            }
        });

         return warehouseDAO.add(accountBook,warehouses);

    }

    @Transactional
    public void update(String accountBook, Warehouse[] warehouses) throws WMSServiceException{
        for(int i=0;i<warehouses.length;i++){
            if(warehouses[i].getName()==null){
                throw new WMSServiceException("仓库名不能为空!");
            }
        }
        try {
            warehouseDAO.update(accountBook, warehouses);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{

        try {
            warehouseDAO.remove(accountBook, ids);
        } catch (Exception e) {
            throw new WMSServiceException("删除供仓库失败，如果仓库已经被引用，需要先删除引用的内容，才能删除该供应商");
        }
    }

    @Transactional
    public WarehouseView[] find(String accountBook, Condition cond) throws WMSServiceException{
            return this.warehouseDAO.find(accountBook, cond);
    }

}
