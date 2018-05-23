package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.StorageLocationDAO;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.StorageLocationView;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wms.utilities.model.StorageLocation;
import com.wms.utilities.model.StorageAreaView;
import java.util.stream.Stream;
import com.wms.utilities.model.StorageLocationView;
@Service
public class StorageLocationServiceImpl implements StorageLocationService{

    @Autowired
    StorageLocationDAO storageLocationDAO;
    @Autowired
    StorageAreaService storageAreaService;
    @Transactional
    public int[] add(String accountBook, StorageLocation[] storageLocations )throws WMSServiceException {

        for(int i=0;i<storageLocations.length;i++) {
            new Validator("库位名").notnull().validate(storageLocations[i].getName());
            new Validator("库位代号").notnull().validate(storageLocations[i].getNo());
            if(storageLocations[i].getEnabled()!=0&&storageLocations[i].getEnabled()!=1){
                throw new WMSServiceException("是否启用只能为0和1！");
            }
        }
            for(int i=0;i<storageLocations.length;i++){
                Condition cond = new Condition();
                cond.addCondition("name",new String[]{storageLocations[i].getName()});
                if(storageLocationDAO.find(accountBook,cond).length > 0) {
                    throw new WMSServiceException("库位名称重复：" + storageLocations[i].getName());
                }
                    Condition cond1= new Condition();
                    cond1.addCondition("no",new String[]{storageLocations[i].getNo()});
                    if(storageLocationDAO.find(accountBook,cond1).length > 0){
                        throw new WMSServiceException("库位代号重复："+storageLocations[i].getNo());
                }
            }
        String storageAreaName;
        StorageAreaView[] storageAreaViews=null;
        //外键检测
        for(int i=0;i<storageLocations.length;i++) {
            storageAreaViews = this.storageAreaService.find(accountBook,
                    new Condition().addCondition("id", storageLocations[i].getStorageAreaId()));
            if (storageAreaViews.length == 0) {
                throw new WMSServiceException(String.format("库区不存在，请重新提交！(%d)", storageLocations[i].getStorageAreaId()));
            }
            storageAreaName=storageAreaViews[0].getName();
            if(storageLocations[i].getName().length()<=storageAreaName.length()){throw new WMSServiceException("库位名"+storageLocations[i].getName()+"不符合要求，必须以库区名为开头!");}
            if(!storageLocations[i].getName().substring(0,storageAreaName.length()).equals(storageAreaName)) {
                throw new WMSServiceException("库位名"+storageLocations[i].getName()+"不符合要求，必须以库区名"+storageAreaName+"为开头");
            }
        }
        return storageLocationDAO.add(accountBook,storageLocations);
    }

    @Transactional
    public void update(String accountBook, StorageLocation[] storageLocations) throws WMSServiceException{

        for(int i=0;i<storageLocations.length;i++) {
            Validator validator = new Validator("库位名");
            validator.notnull().validate(storageLocations[i].getName());
            Validator validator1 = new Validator("库位代号");
            validator.notnull().validate(storageLocations[i].getNo());
            if(storageLocations[i].getEnabled()!=0&&storageLocations[i].getEnabled()!=1){
                throw new WMSServiceException("是否启用只能为0和1！");
            }
        }
        String storageAreaName;
        StorageAreaView[] storageAreaViews=null;
        for(int i=0;i<storageLocations.length;i++) {
            storageAreaViews = this.storageAreaService.find(accountBook,
                    new Condition().addCondition("id", storageLocations[i].getStorageAreaId()));
            if (storageAreaViews.length == 0) {
                throw new WMSServiceException(String.format("库区不存在，请重新提交！(%d)", storageLocations[i].getStorageAreaId()));
            }
            storageAreaName=storageAreaViews[0].getName();
            if(storageLocations[i].getName().length()<=storageAreaName.length()){throw new WMSServiceException("库位名"+storageLocations[i].getName()+"不符合要求，必须以库区名为开头!");}
            if(!storageLocations[i].getName().substring(0,storageAreaName.length()).equals(storageAreaName)) {
                throw new WMSServiceException("库位名"+storageLocations[i].getName()+"不符合要求，必须以库区名"+storageAreaName+"为开头");
            }
        }
        Stream.of(storageLocations).forEach(
                (storageArea)->{
                    if(this.storageLocationDAO.find(accountBook,
                            new Condition().addCondition("id",storageArea.getId())).length == 0){
                        throw new WMSServiceException(String.format("要修改的项目不存在请确认后修改(%d)",storageArea.getId()));
                    }
                }
        );

        for(int i=0;i<storageLocations.length;i++){
            Condition cond = new Condition();
            cond.addCondition("name",new String[]{storageLocations[i].getName()});
            cond.addCondition("id",new Integer[]{storageLocations[i].getId()}, ConditionItem.Relation.NOT_EQUAL);
            if(storageLocationDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("库位名称重复："+storageLocations[i].getName());
            }
            Condition cond1= new Condition();
            cond1.addCondition("no",new String[]{storageLocations[i].getNo()});
            cond1.addCondition("id",new Integer[]{storageLocations[i].getId()}, ConditionItem.Relation.NOT_EQUAL);
            if(storageLocationDAO.find(accountBook,cond1).length > 0){
                throw new WMSServiceException("库位代号重复："+storageLocations[i].getNo());
            }

        }
            storageLocationDAO.update(accountBook, storageLocations);
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
        for (int id : ids) {
            if (storageLocationDAO.find(accountBook, new Condition().addCondition("id", id)).length == 0) {
                throw new WMSServiceException(String.format("删除库位不存在，请重新查询！(%d)", id));
            }
        }
        try {
            storageLocationDAO.remove(accountBook, ids);
        } catch (Exception ex) {
            throw new WMSServiceException("删除库位信息失败，如果供库位已经被引用，需要先删除引用的内容，才能删除该供库位信息");
        }
    }

    @Transactional
    public StorageLocationView[] find(String accountBook, Condition cond) throws WMSServiceException{
            return this.storageLocationDAO.find(accountBook, cond);
    }

    @Override
    @Transactional
    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.storageLocationDAO.findCount(database,cond);
    }
}
