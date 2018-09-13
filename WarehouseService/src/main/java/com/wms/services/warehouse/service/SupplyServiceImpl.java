package com.wms.services.warehouse.service;

import com.wms.services.ledger.service.PersonService;
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
        this.validateEntities(accountBook,supplies);


        for(int i=0;i<supplies.length;i++){
            MaterialView[] curMaterial =this.materialService.find(accountBook, new Condition().addCondition("id",new Integer[]{supplies[i].getMaterialId()}));
            SupplierView[] curSupplier =this.supplierServices.find(accountBook, new Condition().addCondition("id",new Integer[]{supplies[i].getSupplierId()}));

            Condition cond = new Condition();
            cond.addCondition("supplierId",new Integer[]{supplies[i].getSupplierId()});
            cond.addCondition("materialId",new Integer[]{supplies[i].getMaterialId()});
            cond.addCondition("warehouseId",new Integer[]{supplies[i].getWarehouseId()});


            if(supplyDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("供应商-物料关联条目重复："+curSupplier[0].getName()+curMaterial[0].getName());
            }
        }

        for (int i=0;i<supplies.length;i++)
        {
            if (supplies[i].getBarCodeNo().length()==0){
                int noLength=7;
                String thNo= String.valueOf(supplies[i].getId());
                int curLength=noLength-(thNo.length());
                StringBuffer sb = new StringBuffer();
                for (int j = 0; j < curLength;j++) {
                    sb.append('0');
                }
                sb.append(thNo);
                supplies[i].setBarCodeNo(sb.toString());
            }

            supplies[i].setCreateTime(new Timestamp(System.currentTimeMillis()));
            supplies[i].setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
        }
        return supplyDAO.add(accountBook,supplies);
    }

    @Override
    public void update(String accountBook, Supply[] supplies) throws WMSServiceException{

        this.validateEntities(accountBook,supplies);
        for(int i=0;i<supplies.length;i++){
            MaterialView[] curMaterial =this.materialService.find(accountBook, new Condition().addCondition("id",new Integer[]{supplies[i].getMaterialId()}));
            SupplierView[] curSupplier =this.supplierServices.find(accountBook, new Condition().addCondition("id",new Integer[]{supplies[i].getSupplierId()}));
            Condition cond = new Condition();
            cond.addCondition("supplierId",new Integer[]{supplies[i].getSupplierId()});
            cond.addCondition("materialId",new Integer[]{supplies[i].getMaterialId()});
            cond.addCondition("warehouseId",new Integer[]{supplies[i].getWarehouseId()});
            cond.addCondition("id",new Integer[]{supplies[i].getId()}, ConditionItem.Relation.NOT_EQUAL);

            if(supplyDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("供应商-物料关联条目重复："+curSupplier[0].getName()+curMaterial[0].getName());
            }
            SupplyView[] supplyViews= this.find(accountBook,new Condition().addCondition("barCodeNo",new String[]{supplies[i].getBarCodeNo()}));
            if(supplyViews.length > 0){
                throw new WMSServiceException("供应信息条码号重复！对应供应商-物料关联条目："+curSupplier[0].getName()+curMaterial[0].getName()+"条码号："+supplies[i].getBarCodeNo());
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
    @Override
    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.supplyDAO.findCount(database,cond);
    }

    private void validateEntities(String accountBook,Supply[] supplies) throws WMSServiceException{
        Stream.of(supplies).forEach((supply -> {
            new Validator("供货商ID").notEmpty().validate(supply.getSupplierId());
            new Validator("物料ID").notEmpty().validate(supply.getMaterialId());
            new Validator("单托含量").notEmpty().validate(supply.getTrayCapacity());
        }));

        for(int i=0;i<supplies.length;i++){
            for(int j=i+1;j<supplies.length;j++){
                int materialId=supplies[i].getMaterialId();
                int supplierId=supplies[i].getSupplierId();
                //String barCodeNo=supplies[i].getBarCodeNo();
                if(materialId==supplies[j].getMaterialId()&&supplierId==supplies[j].getSupplierId())
                {
                    throw new WMSServiceException("供应商-物料关联条目在添加的列表中重复!");
                }/*
                if(barCodeNo.equals(supplies[j].getBarCodeNo()))
                {
                    throw new WMSServiceException("供货信息条码号在添加的列表中重复!");
                }*/
            }
        }
        //外键检测
        Stream.of(supplies).forEach(
                (supply)->{
                    if(this.warehouseService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{supply.getWarehouseId()})).length == 0){
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",supply.getWarehouseId()));
                    }else if(this.personService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{supply.getCreatePersonId()})).length == 0){
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)",supply.getCreatePersonId()));
                    }else if(this.supplierServices.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{supply.getSupplierId()})).length == 0){
                        throw new WMSServiceException(String.format("供货商不存在，请重新提交！(%d)",supply.getSupplierId()));
                    } else if(this.materialService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{supply.getMaterialId()})).length == 0){
                        throw new WMSServiceException(String.format("物料不存在，请重新提交！(%d)",supply.getMaterialId()));
                    }if(supply.getLastUpdatePersonId() != null && this.personService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{supply.getLastUpdatePersonId()})).length == 0){
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)",supply.getLastUpdatePersonId()));
                    }
                }
        );
    }
}

