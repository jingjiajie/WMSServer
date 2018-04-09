package com.wms.services.warehouse.service;



import com.wms.services.warehouse.dao.WarehouseDAO;
import com.wms.services.warehouse.model.Supplier;
import com.wms.services.warehouse.model.Warehouse;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WarehouseServiceImpl implements WarehouseService{
    @Autowired
    WarehouseDAO warehouseDAO;
    @Transactional
    public int[] add(String accountBook, Warehouse[] warehouses) throws WMSServiceException
    {
        try
        { return warehouseDAO.add(accountBook,warehouses);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }

    @Transactional
    public void update(String accountBook, Warehouse[] warehouses) throws WMSServiceException{

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
