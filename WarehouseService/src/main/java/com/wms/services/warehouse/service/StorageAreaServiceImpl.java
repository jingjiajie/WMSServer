package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.StorageAreaDAO;
import com.wms.services.warehouse.model.StorageArea;
import com.wms.services.warehouse.model.StorageLocation;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StorageAreaServiceImpl implements StorageAreaService{
    @Autowired
    StorageAreaDAO storageAreaDAO;

    @Transactional
    public int[] add(String accountBook,StorageArea[] storageAreas) throws WMSServiceException {
        for(int i=0;i<storageAreas.length;i++)
        {
            Validator validator=new Validator("库区名称");
            validator.notnull().validate(storageAreas[i].getName());
            Validator validator1=new Validator("库区代号");
            validator1.notnull().validate(storageAreas[i].getName());
        }
        for(int i=0;i<storageAreas.length;i++){
            Condition cond = new Condition();
            cond.addCondition("name",new String[]{storageAreas[i].getName()});
            //cond.addCondition("no",new String[]{storageAreas[i].getNo()});
            if(storageAreaDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("库区名称重复："+storageAreas[i].getName());
            }
        }

         return storageAreaDAO.add(accountBook,storageAreas);
    }

    @Transactional
    public void update(String accountBook, StorageArea[] storageAreas) throws WMSServiceException{
        for(int i=0;i<storageAreas.length;i++)
        {
            Validator validator=new Validator("库区名称");
            validator.notnull().validate(storageAreas[i].getName());
            Validator validator1=new Validator("库区代号");
            validator1.notnull().validate(storageAreas[i].getName());
        }
        for(int i=0;i<storageAreas.length;i++){
            Condition cond = new Condition();
            cond.addCondition("name",new String[]{storageAreas[i].getName()});
            cond.addCondition("id",new Integer[]{storageAreas[i].getId()}, ConditionItem.Relation.NOT_EQUAL);
            if(storageAreaDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("库区名称重复："+storageAreas[i].getName());
            }
        }
            storageAreaDAO.update(accountBook, storageAreas);

    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
        try {
            storageAreaDAO.remove(accountBook, ids);
        }
        catch (Throwable ex){
            throw new WMSServiceException("删除库位信息失败，如果供库位已经被引用，需要先删除引用的内容，才能删除该供库位信息");
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
