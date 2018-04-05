package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.StorgeAreaDAO;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wms.services.warehouse.model.StorageArea;
@Service
public class StorgeAreaServiceImpl implements StorageAreaService{
    @Autowired
    StorgeAreaDAO storgeAreaDAO;
    @Transactional
    public int[] add(String accountBook,StorageArea[] storageAreas) throws WMSServiceException {
        try{
            return storgeAreaDAO.add(accountBook,storageAreas);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }
    @Transactional
    public void update(String accountBook, StorageArea[] storageAreas) throws WMSServiceException{
        try {
            storgeAreaDAO.update(accountBook, storageAreas);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }
    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
        try {
            storgeAreaDAO.remove(accountBook, ids);
        } catch (DatabaseNotFoundException ex) {
            throw new WMSServiceException("Accountbook " + accountBook + " not found!");
        }
    }
    @Transactional
    public StorageArea[] find(String accountBook, Condition cond) throws WMSServiceException{
        try {
            return this.storgeAreaDAO.find(accountBook, cond);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }
}
