package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.SupplierDAO;
import com.wms.services.warehouse.dao.SupplyDAO;
import com.wms.utilities.model.*;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Service
@Transactional
public class SupplyServiceImpl implements SupplyService {
    @Autowired
    SupplyDAO supplyDAO;
    @Autowired
    SupplierServices supplierServices;
    @Autowired
    MaterialService materialService;
    @Autowired
    PersonService personService;
    @Autowired
    WarehouseService warehouseService;

    @Override
    public int[] add(String accountBook, Supply[] supplies) throws WMSServiceException
    {
        for (int i=0;i<supplies.length;i++) {

            Validator validator=new Validator("供应商ID");
            validator.notnull().validate(supplies[i].getSupplierId());
            Validator validator1=new Validator("物料ID");
            validator1.notnull().validate(supplies[i].getMaterialId());

        }


        //外键检测
        Stream.of(supplies).forEach(
                (supply)->{
                    if(this.warehouseService.find(accountBook,
                            new Condition().addCondition("warehouseId",supply.getWarehouseId())).length == 0){
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",supply.getWarehouseId()));
                    }else if(this.personService.find(accountBook,
                            new Condition().addCondition("personId",supply.getCreatePersonId())).length == 0){
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)",supply.getCreatePersonId()));
                    }else if(this.supplierServices.find(accountBook,
                            new Condition().addCondition("supplierId",supply.getSupplierId())).length == 0){
                        throw new WMSServiceException(String.format("供货商不存在，请重新提交！(%d)",supply.getSupplierId()));
                    } else if(this.materialService.find(accountBook,
                            new Condition().addCondition("materialId",supply.getMaterialId())).length == 0){
                        throw new WMSServiceException(String.format("物料不存在，请重新提交！(%d)",supply.getMaterialId()));
                    }if(supply.getLastUpdatePersonId() != null && this.personService.find(accountBook,
                            new Condition().addCondition("lsatUpdateId",supply.getLastUpdatePersonId())).length == 0){
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)",supply.getLastUpdatePersonId()));
                    }
                }
        );

        for(int i=0;i<supplies.length;i++){
            MaterialView[] curMsterial =this.materialService.find(accountBook, new Condition().addCondition("materialId",supplies[i].getMaterialId()));
            SupplierView[] curSupplier =this.supplierServices.find(accountBook, new Condition().addCondition("supplierId",supplies[i].getMaterialId()));

            Condition cond = new Condition();
            cond.addCondition("supplierId",new Integer[]{supplies[i].getSupplierId()});
            cond.addCondition("materialId",new Integer[]{supplies[i].getMaterialId()});


            if(supplyDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("供应商-物料关联条目重复："+curSupplier[0].getName()+curMsterial[0].getName());
            }
        }

        for (int i=0;i<supplies.length;i++)
        {
            supplies[i].setCreateTime(new Timestamp(System.currentTimeMillis()));
            supplies[i].setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
        }
        return supplyDAO.add(accountBook,supplies);
    }

    @Override
    public void update(String accountBook, Supply[] supplies) throws WMSServiceException{

        for (int i=0;i<supplies.length;i++) {
            Validator validator=new Validator("供应商ID");
            validator.notnull().validate(supplies[i].getSupplierId());
            Validator validator1=new Validator("物料ID");
            validator1.notnull().validate(supplies[i].getMaterialId());
        }

        for(int i=0;i<supplies.length;i++){
            MaterialView[] curMsterial =this.materialService.find(accountBook, new Condition().addCondition("materialId",supplies[i].getMaterialId()));
            SupplierView[] curSupplier =this.supplierServices.find(accountBook, new Condition().addCondition("supplierId",supplies[i].getMaterialId()));
            Condition cond = new Condition();
            cond.addCondition("supplierId",new Integer[]{supplies[i].getSupplierId()});
            cond.addCondition("materialId",new Integer[]{supplies[i].getMaterialId()});
            cond.addCondition("materialId",new Integer[]{supplies[i].getId()}, ConditionItem.Relation.NOT_EQUAL);


            if(supplyDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("供应商-物料关联条目重复："+curSupplier[0].getName()+curMsterial[0].getName());
            }
        }
        for (int i=0;i<supplies.length;i++)
        {
            supplies[i].setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
        }
        supplyDAO.update(accountBook, supplies);

    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException{

        try {
            supplyDAO.remove(accountBook, ids);
        }
        catch (Throwable ex){
            throw new WMSServiceException("删除供货信息失败，如果供货信息已经被引用，需要先删除引用的内容，才能删除该供货信息");
        }
    }

    @Override
    public SupplyView[] find(String accountBook, Condition cond) throws WMSServiceException{
        return this.supplyDAO.find(accountBook, cond);
    }
}

