package com.wms.services.warehouse.service;

import com.wms.services.settlement.service.TrayService;
import com.wms.services.warehouse.dao.StorageLocationDAO;
import com.wms.services.warehouse.datastructures.StorageLocationLess;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import com.wms.utilities.model.StorageLocationView;

import javax.validation.Valid;

@Service
public class StorageLocationServiceImpl implements StorageLocationService{

    @Autowired
    StorageLocationDAO storageLocationDAO;
    @Autowired
    StorageAreaService storageAreaService;
    @Autowired
    TrayService trayService;
    @Transactional
    public int[] add(String accountBook, StorageLocation[] storageLocations )throws WMSServiceException {

        for(int i=0;i<storageLocations.length;i++) {
            new Validator("库位名").notnull().validate(storageLocations[i].getName());
            new Validator("库位代号").notnull().validate(storageLocations[i].getNo());
            //new Validator("库位长度").notnull().greaterThan(0).validate(storageLocations[i].getLength());
            //new Validator("库位宽度").notnull().greaterThan(0).validate(storageLocations[i].getWidth());
            //new Validator("长度内边距").notnull().min(0).validate(storageLocations[i].getLengthPadding());
            // new Validator("宽度内边距").notnull().min(0).validate(storageLocations[i].getWidthPadding());
            //new Validator("余留面积").notnull().min(0).validate(storageLocations[i].getReservedArea());
            //new Validator("最高码放层数").notnull().greaterThan(0).validate(storageLocations[i].getPiles());
            this.validate(accountBook,storageLocations[i]);
            if(storageLocations[i].getEnabled()!=0&&storageLocations[i].getEnabled()!=1){
                throw new WMSServiceException("是否启用只能为0和1！");
            }
        }

        String storageAreaNo;
        StorageAreaView[] storageAreaViews=null;
        //外键检测
        for(int i=0;i<storageLocations.length;i++) {
            storageAreaViews = this.storageAreaService.find(accountBook,
                    new Condition().addCondition("id", storageLocations[i].getStorageAreaId()));
            if (storageAreaViews.length == 0) {
                throw new WMSServiceException(String.format("库区不存在，请重新提交！(%d)", storageLocations[i].getStorageAreaId()));
            }
            storageAreaNo=storageAreaViews[0].getNo();
            if(storageLocations[i].getNo().length()<=storageAreaNo.length()){throw new WMSServiceException("库位编码"+storageLocations[i].getNo()+"不符合要求，必须以库区编码为开头!");}
            if(!storageLocations[i].getNo().substring(0,storageAreaNo.length()).equalsIgnoreCase(storageAreaNo)) {
                throw new WMSServiceException("库位编码"+storageLocations[i].getNo()+"不符合要求，必须以库区编码"+storageAreaNo+"为开头");
            }
        }


            for(int i=0;i<storageLocations.length;i++){
                Condition cond = new Condition();
                cond.addCondition("name",new String[]{storageLocations[i].getName()});
                StorageLocationView[] storageLocationViews=storageLocationDAO.find(accountBook,cond);
                StorageAreaView[] storageAreaViews1=storageAreaService.find(accountBook,new Condition().addCondition("id",storageLocations[i].getStorageAreaId()));
                int warehouseId=storageAreaViews1[0].getWarehouseId();
                for(int j=0;j<storageLocationViews.length;j++){
                    if(storageLocationViews[j].getWarehouseId()==warehouseId)
                    {throw new WMSServiceException("库位名称重复：" + storageLocations[i].getName());}
                }
                Condition cond1 = new Condition();
                cond.addCondition("no",new String[]{storageLocations[i].getName()});
                StorageLocationView[] storageLocationViews1=storageLocationDAO.find(accountBook,cond);
                StorageAreaView[] storageAreaViews2=storageAreaService.find(accountBook,new Condition().addCondition("id",storageLocations[i].getStorageAreaId()));
                int warehouseId1=storageAreaViews2[0].getWarehouseId();
                for(int j=0;j<storageLocationViews1.length;j++){
                    if(storageLocationViews[j].getWarehouseId()==warehouseId1)
                    {throw new WMSServiceException("库位编码重复：" + storageLocations[i].getNo());}
                }
            }

        for(int i=0;i<storageLocations.length;i++){
            for(int j=i+1;j<storageLocations.length;j++){
                String no=storageLocations[i].getNo();
                if(no.equalsIgnoreCase(storageLocations[j].getNo())){throw new WMSServiceException("库位代号"+no+"在添加的列表中重复!");}
            }
        }
        for(int i=0;i<storageLocations.length;i++){
            for(int j=i+1;j<storageLocations.length;j++){
                String name=storageLocations[i].getName();
                if(name.equalsIgnoreCase(storageLocations[j].getName())){throw new WMSServiceException("库位名称"+name+"在添加的列表中重复!");}
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
            //TODO
            //new Validator("库位长度").notnull().greaterThan(0).validate(storageLocations[i].getLength());
            //new Validator("库位宽度").notnull().greaterThan(0).validate(storageLocations[i].getWidth());
            //new Validator("长度内边距").notnull().min(0).validate(storageLocations[i].getLengthPadding());
            //new Validator("宽度内边距").notnull().min(0).validate(storageLocations[i].getWidthPadding());
           // new Validator("余留面积").notnull().min(0).validate(storageLocations[i].getReservedArea());
           // new Validator("最高码放层数").notnull().greaterThan(0).validate(storageLocations[i].getPiles());
            this.validate(accountBook,storageLocations[i]);
            validator.notnull().validate(storageLocations[i].getNo());
            if(storageLocations[i].getEnabled()!=0&&storageLocations[i].getEnabled()!=1){
                throw new WMSServiceException("是否启用只能为0和1！");
            }
        }

        for(int i=0;i<storageLocations.length;i++){
            for(int j=i+1;j<storageLocations.length;j++){
                String no=storageLocations[i].getNo();
                if(no.equalsIgnoreCase(storageLocations[j].getNo())){throw new WMSServiceException("库位代号"+no+"在添加的列表中重复!");}
            }
        }
        for(int i=0;i<storageLocations.length;i++){
            for(int j=i+1;j<storageLocations.length;j++){
                String name=storageLocations[i].getName();
                if(name.equalsIgnoreCase(storageLocations[j].getName())){throw new WMSServiceException("库位名称"+name+"在添加的列表中重复!");}
            }
        }
        String storageAreaNo;
        StorageAreaView[] storageAreaViews=null;
        for(int i=0;i<storageLocations.length;i++) {
            storageAreaViews = this.storageAreaService.find(accountBook,
                    new Condition().addCondition("id", storageLocations[i].getStorageAreaId()));
            if (storageAreaViews.length == 0) {
                throw new WMSServiceException(String.format("库区不存在，请重新提交！(%d)", storageLocations[i].getStorageAreaId()));
            }
            storageAreaNo=storageAreaViews[0].getNo();
            if(storageLocations[i].getNo().length()<=storageAreaNo.length()){throw new WMSServiceException("库位编码"+storageLocations[i].getNo()+"不符合要求，必须以库区编码为开头!");}
            if(!storageLocations[i].getNo().substring(0,storageAreaNo.length()).equalsIgnoreCase(storageAreaNo)) {
                throw new WMSServiceException("库位编码"+storageLocations[i].getNo()+"不符合要求，必须以库区编码"+storageAreaNo+"为开头");
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
/*
        for(int i=0;i<storageLocations.length;i++){
            Condition cond = new Condition();
            cond.addCondition("name",new String[]{storageLocations[i].getName()});
            cond.addCondition("id",new Integer[]{storageLocations[i].getId()}, ConditionItem.Relation.NOT_EQUAL);
            StorageAreaView[] storageAreaViews1=storageAreaService.find(accountBook,new Condition().addCondition("id",storageLocations[i].getStorageAreaId()));
            int warehouseId=storageAreaViews1[0].getWarehouseId();
            cond.addCondition("warehouseId",warehouseId);
            if(storageLocationDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("库位名称重复："+storageLocations[i].getName());
            }
            Condition cond1= new Condition();
            cond1.addCondition("no",new String[]{storageLocations[i].getNo()});
            cond1.addCondition("id",new Integer[]{storageLocations[i].getId()}, ConditionItem.Relation.NOT_EQUAL);
            cond.addCondition("warehouseId",warehouseId);
            if(storageLocationDAO.find(accountBook,cond1).length > 0){
                throw new WMSServiceException("库位代号重复："+storageLocations[i].getNo());
            }

        }*/
        for(int i=0;i<storageLocations.length;i++){
            StorageAreaView[] storageAreaViews1=storageAreaService.find(accountBook,new Condition().addCondition("id",storageLocations[i].getStorageAreaId()));
            int warehouseId=storageAreaViews1[0].getWarehouseId();
            Condition cond = new Condition();
            cond.addCondition("name",new String[]{storageLocations[i].getName()}).addCondition("warehouseId",warehouseId);
            cond.addCondition("id",storageLocations[i].getId(), ConditionItem.Relation.NOT_EQUAL);
            StorageLocationView[] storageLocationViews=storageLocationDAO.find(accountBook,cond);
            if(storageLocationViews.length!=0){throw new WMSServiceException("库位名称重复：" + storageLocations[i].getName());}
            /*
            for(int j=0;j<storageLocationViews.length;j++){
                if(storageLocationViews[j].getWarehouseId()==warehouseId)
                {throw new WMSServiceException("库位名称重复：" + storageLocations[i].getName());}
            }*/
            Condition cond1 = new Condition();
            cond1.addCondition("no",new String[]{storageLocations[i].getNo()}).addCondition("warehouseId",warehouseId);
            cond1.addCondition("id",storageLocations[i].getId(), ConditionItem.Relation.NOT_EQUAL);
            StorageLocationView[] storageLocationViews1=storageLocationDAO.find(accountBook,cond);
            if(storageLocationViews1.length!=0)
                {throw new WMSServiceException("库位编码重复：" + storageLocations[i].getNo());}

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

    private void validate(String accountBook,StorageLocation storageLocation){
        StorageAreaView[] storageAreaViews1=storageAreaService.find(accountBook,new Condition().addCondition("id",storageLocation.getStorageAreaId()));
        int warehouseId=storageAreaViews1[0].getWarehouseId();
        CommonData[] commonDataLength=trayService.find(accountBook,new Condition().addCondition("key","Tray_Length"+String.valueOf(warehouseId)));
        CommonData[] commonDataWidth=trayService.find(accountBook,new Condition().addCondition("key","Tray_Width"+String.valueOf(warehouseId)));
        if(storageLocation.getWidth()==null||storageLocation.getWidthPadding()==null&&storageLocation.getLength()==null&&storageLocation.getLength()==null){return;}
        if(commonDataLength.length==1){
            String lengthStr=commonDataLength[0].getValue().substring(11,commonDataLength[0].getValue().length()-1);
            if(storageLocation.getLength().add(storageLocation.getLengthPadding().negate()).compareTo(new BigDecimal(lengthStr))<0){
                throw new WMSServiceException("库位："+storageLocation.getName()+"长度-长度边距不能小于本仓库托位长度！");
            }
        }
        if(commonDataWidth.length==1){
            String widthStr=commonDataWidth[0].getValue().substring(11,commonDataWidth[0].getValue().length()-1);
            if(storageLocation.getWidth().add(storageLocation.getWidthPadding().negate()).compareTo(new BigDecimal(widthStr))<0){
                throw new WMSServiceException("库位："+storageLocation.getName()+"宽度-宽度边距不能小于本仓库托位宽度！");
            }
        }
    }

    @Transactional
    public Object[] findLess(String accountBook, Condition cond) throws WMSServiceException{
        StorageLocationView[] storageLocationViews= this.storageLocationDAO.find(accountBook,cond);
        List<StorageLocationLess> list=new ArrayList<>();
        for(int i=0;i<storageLocationViews.length;i++){
            StorageLocationLess storageLocationLess=new StorageLocationLess();
            storageLocationLess.setId(storageLocationViews[i].getId());
            storageLocationLess.setName(storageLocationViews[i].getName());
            storageLocationLess.setNo(storageLocationViews[i].getNo());
            storageLocationLess.setWarehouseId(storageLocationViews[i].getWarehouseId());
            list.add(storageLocationLess);
        }
        StorageLocationLess[] storageLocationLesses=null;
        return list.toArray();
    }
}
