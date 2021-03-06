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
import java.util.ArrayList;
import java.util.List;
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

            if(this.find(accountBook,new Condition()
                    .addCondition("warehouseId",new Integer[]{supplies[i].getWarehouseId()})
                    .addCondition("serialNo",new String[]{supplies[i].getSerialNo()})).length > 0){
                throw new WMSServiceException("供应信息序号重复！对应供货序号："+supplies[i].getSerialNo());
            }

            if(this.find(accountBook,new Condition()
                    .addCondition("warehouseId",new Integer[]{supplies[i].getWarehouseId()})
                    .addCondition("barCodeNo",new String[]{supplies[i].getBarCodeNo()})).length > 0){
                throw new WMSServiceException("供应信息条码号重复！对应供货序号："+supplies[i].getSerialNo());
            }
        }

        int[] ids= supplyDAO.add(accountBook,supplies);

        for(int i=0;i<supplies.length;i++){
            if(this.supplyDAO.findTable(accountBook,new Condition()
                    .addCondition("serialNo",new String[]{supplies[i].getSerialNo()})
                    .addCondition("warehouseId",new Integer[]{supplies[i].getWarehouseId()})).length > 1){
                throw new WMSServiceException("供应信息序号重复！对应供货序号："+supplies[i].getSerialNo());
            }
        }
        List<Supply> updateSuppliesDoneList = new ArrayList<>();
        for (int i=0;i<ids.length;i++)
        {
            Supply[] suppliesDone= supplyDAO.findTable(accountBook,new Condition().addCondition("id",ids[i], ConditionItem.Relation.IN));
                if (suppliesDone[0].getBarCodeNo()==null){
                    int noLength=7;
                    String thNo= String.valueOf(suppliesDone[0].getId());
                    int curLength=noLength-(thNo.length());
                    StringBuffer sb = new StringBuffer();
                    for (int j = 0; j < curLength;j++) {
                        sb.append('0');
                    }
                    sb.append(thNo);
                    suppliesDone[0].setBarCodeNo(sb.toString());
                    updateSuppliesDoneList.add(suppliesDone[0]);
                }
//                else{
//                    new Validator("条码号长度").min(7).validate(suppliesDone[i].getBarCodeNo().length());
//                    String barCarNo=suppliesDone[i].getBarCodeNo();
//                    for (int j = barCarNo.length();--j>=0;){
//                        if (!Character.isDigit(barCarNo.charAt(j))){
//                            throw new WMSServiceException("供货条码号必须为纯数字！出错条码号："+barCarNo);
//                        }
//                    }
//                }
        }

        this.update(accountBook,updateSuppliesDoneList.toArray(new Supply[updateSuppliesDoneList.size()]));

        return ids;
    }

    @Override
    public void update(String accountBook, Supply[] supplies) throws WMSServiceException{

        this.validateEntities(accountBook,supplies);
        for(int i=0;i<supplies.length;i++){

//            String barCarNo=supplies[i].getBarCodeNo();
//            if (supplies[i].getBarCodeNo()!=null){
//                new Validator("条码号长度").min(7).validate(supplies[i].getBarCodeNo().length());
//                for (int j = barCarNo.length(); --j >= 0; ) {
//                    if (!Character.isDigit(barCarNo.charAt(j))) {
//                        throw new WMSServiceException("供货条码号必须为纯数字！出错条码号：" + barCarNo);
//                    }
//                }
//            }

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
            SupplyView[] supplyViews= this.find(accountBook,new Condition()
                    .addCondition("id",new Integer[]{supplies[i].getId()}, ConditionItem.Relation.NOT_EQUAL)
                    .addCondition("warehouseId",new Integer[]{supplies[i].getWarehouseId()})
                    .addCondition("barCodeNo",new String[]{supplies[i].getBarCodeNo()}));
            if(supplyViews.length > 0){
                throw new WMSServiceException("供应信息条码号重复！对应供应商-物料关联条目："+curSupplier[0].getName()+curMaterial[0].getName()+"条码号："+supplies[i].getBarCodeNo());
            }
        }
        for (int i=0;i<supplies.length;i++)
        {
            supplies[i].setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
        }
        supplyDAO.update(accountBook, supplies);

        for(int i=0;i<supplies.length;i++){
            if(this.find(accountBook,new Condition()
                    .addCondition("id",new Integer[]{supplies[i].getId()}, ConditionItem.Relation.NOT_EQUAL)
                    .addCondition("warehouseId",new Integer[]{supplies[i].getWarehouseId()})
                    .addCondition("serialNo",new String[]{supplies[i].getSerialNo()})).length > 0){
                throw new WMSServiceException("供应信息序号重复！对应供货序号："+supplies[i].getSerialNo());
            }

            SupplyView[] supplyViews= this.find(accountBook,new Condition()
                    .addCondition("id",new Integer[]{supplies[i].getId()}, ConditionItem.Relation.NOT_EQUAL)
                    .addCondition("warehouseId",new Integer[]{supplies[i].getWarehouseId()})
                    .addCondition("barCodeNo",new String[]{supplies[i].getBarCodeNo()}));
            if(supplyViews.length > 0){
                throw new WMSServiceException("供应信息条码号重复！对应条码号："+supplies[i].getBarCodeNo());
            }
        }
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
//            new Validator("单托含量").notEmpty().validate(supply.getBarCodeNo());
            new Validator("序号").notEmpty().validate(supply.getSerialNo());
            new Validator("供货安全库存").min(0);
        }));

        for(int i=0;i<supplies.length;i++){
            for(int j=i+1;j<supplies.length;j++){
                int materialId=supplies[i].getMaterialId();
                int supplierId=supplies[i].getSupplierId();
                String serialNo=supplies[i].getSerialNo();

                if(serialNo.equalsIgnoreCase(supplies[j].getSerialNo()))
                {
                    throw new WMSServiceException("供货信息序号在添加的列表中重复!对应供货序号："+supplies[i].getSerialNo());
                }
                if(materialId==supplies[j].getMaterialId()&&supplierId==supplies[j].getSupplierId())
                {
                    throw new WMSServiceException("供应商-物料关联条目在添加的列表中重复!对应供货序号："+supplies[i].getSerialNo());
                }
                if(supplies[i].getBarCodeNo()!=""&&supplies[i].getBarCodeNo()!=null
                        &&supplies[j].getBarCodeNo()!=""&&supplies[j].getBarCodeNo()!=null)
                {
                    if(supplies[i].getBarCodeNo().equals(supplies[j].getBarCodeNo()))
                    {
                        throw new WMSServiceException("供货信息条码号在添加的列表中重复!");
                    }
                }
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

