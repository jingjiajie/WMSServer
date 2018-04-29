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
        for (int i=0;i<materials.length;i++) {
            Validator validator=new Validator("物料名称");
            validator.notnull().validate(materials[i].getName());
            Validator validator1=new Validator("物料代号");
            validator1.notnull().validate(materials[i].getNo());
        }

        Stream.of(materials).forEach((material)->{
            Condition cond = new Condition();
            cond.addCondition("name",new String[]{material.getName()});
            if(materialDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("物料名："+material.getName()+"已经存在!");
            }
        });

        Stream.of(materials).forEach((material)->{
            Condition cond = new Condition();
            cond.addCondition("no",new String[]{material.getNo()});
            if(materialDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("物料代号："+material.getNo()+"已经存在!");
            }
        });

        //外键检测
        Stream.of(materials).forEach(
                (material)->{
                    if(this.warehouseService.find(accountBook,
                            new Condition().addCondition("id",material.getWarehouseId())).length == 0){
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",material.getWarehouseId()));
                    }

                }
        );


        return materialDAO.add(accountBook,materials);
    }

    @Override
    public void update(String accountBook, Material[] materials) throws WMSServiceException{

        for (int i=0;i<materials.length;i++) {
            Validator validator=new Validator("物料名称");
            validator.notnull().validate(materials[i].getName());
            Validator validator1=new Validator("物料代号");
            validator1.notnull().validate(materials[i].getNo());
        }

        for(int i=0;i<materials.length;i++){
            Condition cond = new Condition();
            cond.addCondition("name",new String[]{materials[i].getName()});
            cond.addCondition("id",new Integer[]{materials[i].getId()}, ConditionItem.Relation.NOT_EQUAL);
            if(materialDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("物料名称重复："+materials[i].getName());
            }
        }
        Stream.of(materials).forEach((material)->{
            Condition cond = new Condition();
            cond.addCondition("no",new String[]{material.getNo()});
            if(materialDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("物料代号："+material.getNo()+"已经存在!");
            }
        });

        materialDAO.update(accountBook, materials);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException{

        try {
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
}
