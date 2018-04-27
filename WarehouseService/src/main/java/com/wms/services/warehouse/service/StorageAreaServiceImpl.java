package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.StorageAreaDAO;
import com.wms.services.warehouse.model.StorageArea;
import com.wms.services.warehouse.model.StorageLocation;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StorageAreaServiceImpl implements StorageAreaService{
    @Autowired
    StorageAreaDAO storageAreaDAO;
    @Autowired
    StorageLocationService storageLocationService;
    @Transactional
    public int[] add(String accountBook,StorageArea[] storageAreas) throws WMSServiceException {
        for(int i=0;i<storageAreas.length;i++)
        {
            if(storageAreas[i].getName()==null)
            {
                throw new WMSServiceException("库区名不能为空！");
            }
            if(storageAreas[i].getNo()==null)
            {
                throw new WMSServiceException("库区代号不能为空！");
            }
            String storageAreaNo=storageAreas[i].getNo();
            StorageArea[] storageAreas1=null;
            Condition condition = Condition.fromJson("{'conditions':[{'key':'no','values':['"+storageAreaNo+"'],'relation':'EQUAL'}],'orders':[{'key':'name','order':'ASC'}]}");
            storageAreas1=storageAreaDAO.find(accountBook,condition);
            if(storageAreas1.length>0)
            {
                throw new WMSServiceException("库区代号 " + storageAreaNo + " 已经存在！");
            }
        }
        try
        { return storageAreaDAO.add(accountBook,storageAreas);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }

    @Transactional
    public void update(String accountBook, StorageArea[] storageAreas) throws WMSServiceException{
        for(int j=0;j<storageAreas.length;j++)
        {
            if(storageAreas[j].getName()==null)
            {
                throw new WMSServiceException("库区名不能为空！");
            }
            if(storageAreas[j].getNo()==null)
            {
                throw new WMSServiceException("库区代号不能为空！");
            }
            StorageArea[] storageAreas1=null;
            String storageAreaNoUpdate=storageAreas[j].getNo();
            Condition condition = Condition.fromJson("{'conditions':[{'key':'no','values':['"+storageAreaNoUpdate+"'],'relation':'EQUAL'}],'orders':[{'key':'name','order':'ASC'}]}");
            storageAreas1=storageAreaDAO.find(accountBook,condition);
            if(storageAreas1.length>0)
            {
                if(storageAreas1[0].getId()!=storageAreas[j].getId())
                throw new WMSServiceException("库区代号 " + storageAreaNoUpdate + " 已经存在！");
            }
        }
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
