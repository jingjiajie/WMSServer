package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.StorageAreaDAO;
import com.wms.services.warehouse.model.StorageLocation;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wms.services.warehouse.model.StorageArea;
@Service
public class StorageAreaServiceImpl implements StorageAreaService{
    @Autowired
    StorageAreaDAO storageAreaDAO;
    @Autowired
    StorageLocationService storageLocationService;
    @Transactional
    public int[] add(String accountBook,StorageArea[] storageAreas) throws WMSServiceException {
        try
        { return storageAreaDAO.add(accountBook,storageAreas);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }
    @Transactional
    public void update(String accountBook, StorageArea[] storageAreas) throws WMSServiceException{
        try {
            storageAreaDAO.update(accountBook, storageAreas);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }
    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
        if(storageLocationService==null)
        {
            System.out.println("storageLocationService为空");
        }
        for(int i=0;i<ids.length;i++)
        {
            int storageAreaID;
            storageAreaID=ids[i];
            StorageLocation[] storageLocations=null;
            Condition condition = Condition.fromJson("{'conditions':[{'key':'storageAreaID','values':["+storageAreaID+"],'relation':'EQUAL'}]}");
            try {
                storageLocations= storageLocationService.find(accountBook, condition);
            } catch (DatabaseNotFoundException ex) {
                throw new WMSServiceException("Accountbook " + accountBook + " not found!");
            }

            if(storageLocations.length>0)
            {
                StorageArea[] storageAreas=null;
                Condition condition1 = Condition.fromJson("{'conditions':[{'key':'id','values':["+storageAreaID+"],'relation':'EQUAL'}],'orders':[{'key':'name','order':'ASC'}]}");
                try {
                    storageAreas= storageAreaDAO.find(accountBook, condition1);

                } catch (DatabaseNotFoundException ex) {
                    throw new WMSServiceException("Accountbook " + accountBook + " not found!");
                }
                throw new WMSServiceException("库位信息 " + storageAreas[0].getName() + " 被引用无法删除");
            }

        }
        try {
            storageAreaDAO.remove(accountBook, ids);
        } catch (DatabaseNotFoundException ex) {
            throw new WMSServiceException("Accountbook " + accountBook + " not found!");
        }
    }
    @Transactional
    public StorageArea[] find(String accountBook, Condition cond) throws WMSServiceException{
        try {
            return this.storageAreaDAO.find(accountBook, cond);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }
}
