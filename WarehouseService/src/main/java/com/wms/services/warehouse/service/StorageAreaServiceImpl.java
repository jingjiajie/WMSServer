package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.StorageAreaDAO;
import com.wms.utilities.model.StorageArea;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.StorageAreaView;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Stream;

@Service
public class StorageAreaServiceImpl implements StorageAreaService{
    @Autowired
    StorageAreaDAO storageAreaDAO;
    @Autowired
    WarehouseService warehouseService;

    @Transactional
    public int[] add(String accountBook,StorageArea[] storageAreas) throws WMSServiceException {
        for(int i=0;i<storageAreas.length;i++)
        {
            Validator validator=new Validator("库区名称");
            validator.notnull().validate(storageAreas[i].getName());
            Validator validator1=new Validator("库区代号");
            validator1.notnull().validate(storageAreas[i].getNo());
        }
        for(int i=0;i<storageAreas.length;i++){
            Condition cond = new Condition();
            cond.addCondition("name",new String[]{storageAreas[i].getName()});
            if(storageAreaDAO.find(accountBook,cond).length > 0) {
                throw new WMSServiceException("库区名称重复：" + storageAreas[i].getName());
            }
                Condition cond1 = new Condition();
                cond1.addCondition("no",new String[]{storageAreas[i].getNo()});
            if(storageAreaDAO.find(accountBook,cond1).length > 0) {
                throw new WMSServiceException("库区代号重复：" + storageAreas[i].getNo());
            }
        }
        //外键
        Stream.of(storageAreas).forEach(
                (storageArea)->{
                    if(this.warehouseService.find(accountBook,
                            new Condition().addCondition("id",storageArea.getWarehouseId())).length == 0){
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",storageArea.getWarehouseId()));
                    }
                }
        );
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

        Stream.of(storageAreas).forEach(
                (storageArea)->{
                    if(this.storageAreaDAO.find(accountBook,
                            new Condition().addCondition("id",storageArea.getId())).length == 0){
                        throw new WMSServiceException(String.format("要修改的项目不存在请确认后修改(%d)",storageArea.getId()));
                    }
                }
        );

        for(int i=0;i<storageAreas.length;i++){
            Condition cond = new Condition();
            cond.addCondition("name",new String[]{storageAreas[i].getName()});
            cond.addCondition("id",new Integer[]{storageAreas[i].getId()}, ConditionItem.Relation.NOT_EQUAL);
            if(storageAreaDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("库区名称重复："+storageAreas[i].getName());
            }
            Condition cond1 = new Condition();
            cond1.addCondition("no",new String[]{storageAreas[i].getNo()});
            cond1.addCondition("id",new Integer[]{storageAreas[i].getId()}, ConditionItem.Relation.NOT_EQUAL);
            if(storageAreaDAO.find(accountBook,cond1).length > 0) {
                throw new WMSServiceException("库区代号重复：" + storageAreas[i].getNo());
            }
        }
        //外键
        Stream.of(storageAreas).forEach(
                (storageArea)->{
                    if(this.warehouseService.find(accountBook,
                            new Condition().addCondition("id",storageArea.getWarehouseId())).length == 0){
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",storageArea.getWarehouseId()));
                    }
                }
        );
            storageAreaDAO.update(accountBook, storageAreas);
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{

        for (int id : ids) {
            if (storageAreaDAO.find(accountBook, new Condition().addCondition("id", id)).length == 0) {
                throw new WMSServiceException(String.format("删除库区不存在，请重新查询！(%d)", id));
            }
        }

        try {
            storageAreaDAO.remove(accountBook, ids);
        }
        catch (Throwable ex){
            throw new WMSServiceException("删除库取信息失败，如果供库区已经被引用，需要先删除引用的内容，才能删除该供库区信息");
        }
    }

    @Transactional
    public StorageAreaView[] find(String accountBook, Condition cond) throws WMSServiceException{
        try {
            return this.storageAreaDAO.find(accountBook, cond);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }

    @Override
    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.storageAreaDAO.findCount(database,cond);
    }
}
