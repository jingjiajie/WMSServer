package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.StorageLocationDAO;
import com.wms.services.warehouse.dao.SupplierDAO;
import com.wms.services.warehouse.model.StorageLocation;
import com.wms.services.warehouse.model.Supplier;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StorageLocationServiceImpl implements StorageLocationService{

    @Autowired
    StorageLocationDAO storageLocationDAO;
    @Transactional
    public int[] add(String accountBook, StorageLocation[] storageLocations )throws WMSServiceException {
        try{
            return storageLocationDAO.add(accountBook,storageLocations);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }
    @Transactional
    public void update(String accountBook, StorageLocation[] storageLocations) throws WMSServiceException{
        try {
            storageLocationDAO.update(accountBook, storageLocations);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
        try {
            storageLocationDAO.remove(accountBook, ids);
        } catch (DatabaseNotFoundException ex) {
            throw new WMSServiceException("Accountbook " + accountBook + " not found!");
        }
    }
    @Transactional
    public StorageLocation[] find(String accountBook, Condition cond) throws WMSServiceException{
        try {
            return this.storageLocationDAO.find(accountBook, cond);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }
}
