package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.SupplierDAO;
import com.wms.services.warehouse.model.Supplier;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class SupplierServicesImpl implements SupplierServices{
    @Autowired
    SupplierDAO  supplierDAO;
    @Transactional
    public int[] add(String accountBook, Supplier[] suppliers) throws WMSServiceException{
        try{
            return supplierDAO.add(accountBook,suppliers);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }
    @Transactional
    public void update(String accountBook, Supplier[] suppliers) throws WMSServiceException{
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
