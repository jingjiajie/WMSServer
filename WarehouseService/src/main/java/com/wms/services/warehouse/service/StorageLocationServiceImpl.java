package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.StorageLocationDAO;
import com.wms.utilities.model.StorageArea;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wms.utilities.model.StorageLocation;

import java.util.stream.Stream;

@Service
public class StorageLocationServiceImpl implements StorageLocationService{

    @Autowired
    StorageLocationDAO storageLocationDAO;
    @Autowired
    StorageAreaService storageAreaService;
    @Transactional
    public int[] add(String accountBook, StorageLocation[] storageLocations )throws WMSServiceException {

        if(storageAreaService==null){
            System.out.printf("a");
        }

        for(int i=0;i<storageLocations.length;i++) {

            Validator validator = new Validator("库位名");
            validator.notnull().validate(storageLocations[i].getName());
            Validator validator1 = new Validator("库位代号");
            validator.notnull().validate(storageLocations[i].getNo());

        }
            for(int i=0;i<storageLocations.length;i++){
                Condition cond = new Condition();
                cond.addCondition("name",new String[]{storageLocations[i].getName()});
                if(storageAreaService.find(accountBook,cond).length > 0){
                    throw new WMSServiceException("库区名称重复："+storageLocations[i].getName());
                }
            }

        //外键检测
        Stream.of(storageLocations).forEach(
                (storageLocation)->{
                    if(this.storageAreaService.find(accountBook,
                            new Condition().addCondition("id",storageLocation.getStorageAreaId())).length == 0){
                        throw new WMSServiceException(String.format("库区不存在，请重新提交！(%d)",storageLocation.getStorageAreaId()));
                }}
        );
            return storageLocationDAO.add(accountBook,storageLocations);
    }
    @Transactional
    public void update(String accountBook, StorageLocation[] storageLocations) throws WMSServiceException{

        for(int i=0;i<storageLocations.length;i++) {
            Validator validator = new Validator("库位名");
            validator.notnull().validate(storageLocations[i].getName());
            Validator validator1 = new Validator("库位代号");
            validator.notnull().validate(storageLocations[i].getNo());
            String storageLocationNo = storageLocations[i].getNo();
        }

        for(int i=0;i<storageLocations.length;i++){
            Condition cond = new Condition();
            cond.addCondition("name",new String[]{storageLocations[i].getName()});
            cond.addCondition("id",new Integer[]{storageLocations[i].getId()}, ConditionItem.Relation.NOT_EQUAL);
            if(storageLocationDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("库位名称重复："+storageLocations[i].getName());
            }
        }
            storageLocationDAO.update(accountBook, storageLocations);
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
