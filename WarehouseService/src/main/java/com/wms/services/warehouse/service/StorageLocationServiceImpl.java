package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.StorageLocationDAO;
import com.wms.services.warehouse.model.StorageArea;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wms.services.warehouse.model.StorageLocation;
@Service
public class StorageLocationServiceImpl implements StorageLocationService{

    @Autowired
    StorageLocationDAO storageLocationDAO;
    @Transactional
    public int[] add(String accountBook, StorageLocation[] storageLocations )throws WMSServiceException {
        for(int i=0;i<storageLocations.length;)
        {
            String storageLocationNo=storageLocations[i].getNo();
            StorageLocation[] storageLocations1=null;
            Condition condition = Condition.fromJson("{'conditions':[{'key':'no','values':['"+storageLocationNo+"'],'relation':'EQUAL'}],'orders':[{'key':'name','order':'ASC'}]}");
            storageLocations1=storageLocationDAO.find(accountBook,condition);
            if(storageLocations1.length>0)
            {
                throw new WMSServiceException("库位代号 " + storageLocationNo + " 已经存在！");
            }
        }
        try{
            return storageLocationDAO.add(accountBook,storageLocations);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }
    @Transactional
    public void update(String accountBook, StorageLocation[] storageLocations) throws WMSServiceException{

        for(int i=0;i<storageLocations.length;)
        {
            String storageLocationNo=storageLocations[i].getNo();
            StorageLocation[] storageLocations1=null;
            Condition condition = Condition.fromJson("{'conditions':[{'key':'no','values':['"+storageLocationNo+"'],'relation':'EQUAL'}],'orders':[{'key':'name','order':'ASC'}]}");
            storageLocations1=storageLocationDAO.find(accountBook,condition);
            if(storageLocations1.length>0)
            {if(storageLocations1[0].getId()!=storageLocations[i].getId()){
                throw new WMSServiceException("库位代号 " + storageLocationNo + " 已经存在！");}
            }
        }
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
