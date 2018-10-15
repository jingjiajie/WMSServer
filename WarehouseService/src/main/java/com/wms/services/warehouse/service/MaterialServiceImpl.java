package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.MaterialDAO;
import com.wms.utilities.model.*;
import com.wms.services.warehouse.dao.SupplyDAO;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.stream.Stream;

@Service
@Transactional
public class MaterialServiceImpl implements MaterialService {
    @Autowired
    MaterialDAO materialDAO;
    @Autowired
    SupplyService supplyService;
    @Autowired
    WarehouseService warehouseService;

    @Override
    public int[] add(String accountBook, Material[] materials) throws WMSServiceException
    {
        this.validateEntities(accountBook,materials);
        for(int i=0;i<materials.length;i++){
            for(int j=i+1;j<materials.length;j++){
                String no=materials[i].getNo();
                String name=materials[i].getName();
                String productLine=materials[i].getProductLine();
                if(no.equals(materials[j].getNo()) && name.equals(materials[j].getName()) &&productLine.equals(materials[j].getProductLine()))
                {
                    throw new WMSServiceException("物料名：" +name +"物料代号：" +no+ "系列："+productLine+"在添加的列表中重复!");
                }
            }
        }
        Stream.of(materials).forEach((material)->{
            if(materialDAO.find(accountBook,new Condition().addCondition("name",new String[]{material.getName()}).addCondition("no",new String[]{material.getNo()}).addCondition("productLine",new String[]{material.getProductLine()}).addCondition("warehouseId",new Integer[]{material.getWarehouseId()})).length > 0) {
                throw new WMSServiceException("物料名：" + material.getName() +"物料代号：" + material.getNo()+ "系列："+material.getProductLine()+"已经存在!");
            }
        });
        return materialDAO.add(accountBook,materials);
    }

    @Override
    public void update(String accountBook, Material[] materials) throws WMSServiceException
    {
        this.validateEntities(accountBook,materials);
        for(int i=0;i<materials.length;i++){
            for(int j=i+1;j<materials.length;j++){
                String no=materials[i].getNo();
                String name=materials[i].getName();
                String productLine=materials[i].getProductLine();
                if(no.equals(materials[j].getNo()) && name.equals(materials[j].getName()) &&productLine.equals(materials[j].getProductLine()))
                {
                    throw new WMSServiceException("物料名：" +name +"物料代号：" +no+ "系列："+productLine+"在添加的列表中重复!");
                }
            }
        }
        for(int i=0;i<materials.length;i++){
            Condition cond = new Condition();
            cond.addCondition("name",new String[]{materials[i].getName()});
            cond.addCondition("productLine",new String[]{materials[i].getProductLine()});
            cond.addCondition("warehouseId",new Integer[]{materials[i].getWarehouseId()});
            cond.addCondition("no",new String[]{materials[i].getNo()});
            cond.addCondition("id",new Integer[]{materials[i].getId()}, ConditionItem.Relation.NOT_EQUAL);
            if(materialDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("已存在相同系列-相同代号名称物料重复："+materials[i].getName());
            }
        }

        materialDAO.update(accountBook, materials);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException{

        try {
            for (int id : ids) {
                if (materialDAO.find(accountBook, new Condition().addCondition("id", new Integer[]{id})).length == 0) {
                    throw new WMSServiceException(String.format("删除物料不存在，请重新查询！(%d)", id));
                }
            }

            materialDAO.remove(accountBook, ids);
        }
        catch (Throwable ex){
            throw new WMSServiceException("删除物料信息失败，如果物料信息已经被引用，需要先删除引用的内容，才能删除该物料");
        }
    }

    @Override
    public MaterialView[] find(String accountBook, Condition cond) throws WMSServiceException{
        return this.materialDAO.find(accountBook, cond);
    }

    private void validateEntities(String accountBook,Material[] materials) throws WMSServiceException{
        Stream.of(materials).forEach((material -> {
            new Validator("是否启用").min(0).max(1).validate(material.getEnabled());
            new Validator("代号").notEmpty().validate(material.getNo());
            new Validator("物料名称").notEmpty().validate(material.getName());

            if(this.warehouseService.find(accountBook,
                    new Condition().addCondition("id",material.getWarehouseId())).length == 0){
                throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",material.getWarehouseId()));
            }
        }));

        for(int i=0;i<materials.length;i++){
            for(int j=i+1;j<materials.length;j++){
                String serialNo=materials[i].getSerialNo();
                if(serialNo.equals(materials[j].getSerialNo()))
                {
                    throw new WMSServiceException("物料序号在添加的列表中重复!");
                }
            }
        }


    }
    @Override
    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.materialDAO.findCount(database,cond);
    }
}
