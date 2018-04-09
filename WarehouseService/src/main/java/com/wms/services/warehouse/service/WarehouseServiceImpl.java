package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.WarehouseDAO;
import com.wms.services.warehouse.model.Supplier;
import com.wms.services.warehouse.model.Supply;
import com.wms.services.warehouse.model.Warehouse;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
           if(warehouses[i].getName()==null){
               throw new WMSServiceException("仓库名不能为空!");
           }
       }
        try
        { return warehouseDAO.add(accountBook,warehouses);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
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
        for(int i=0;i<ids.length;i++){
            Condition condition = Condition.fromJson("{'conditions':[{'key':'warehouseId','values':["+ids[i]+"],'relation':'EQUAL'}]}");
           Supply[] supplies=null;
           supplies=supplyService.find(accountBook,condition);
           if(supplies.length>0){
               Condition condition1 = Condition.fromJson("{'conditions':[{'key':'id','values':["+ids[i]+"],'relation':'EQUAL'}]}");
               Warehouse[] warehouses=null;
               warehouses=warehouseDAO.find(accountBook,condition1);
               throw new WMSServiceException(warehouses[0].getName()+"被引用，无法删除");
           }
        }
        try {
            warehouseDAO.remove(accountBook, ids);
        } catch (DatabaseNotFoundException ex) {
            throw new WMSServiceException("Accountbook " + accountBook + " not found!");
        }
    }

    @Transactional
    public Warehouse[] find(String accountBook, Condition cond) throws WMSServiceException{
        try {
            return this.warehouseDAO.find(accountBook, cond);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }

}
