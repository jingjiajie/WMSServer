package com.wms.services.warehouse.service;


import com.wms.services.warehouse.dao.SafetyStockDAO;
import com.wms.utilities.IDChecker;
import com.wms.utilities.OrderNoGenerator;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.SafetyStock;
import com.wms.utilities.model.SafetyStockView;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;


@Service
@Transactional
public class SafetyStockServiceImpl implements SafetyStockService{
    @Autowired
    SafetyStockDAO safetyStockDAO;
    @Autowired
    OrderNoGenerator orderNoGenerator;
    @Autowired
    StorageLocationService storageLocationService;
    @Autowired
    SupplyService supplyService;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    IDChecker idChecker;

    private static final String NO_PREFIX = "R";

    @Override
    public int[] add(String accountBook, SafetyStock[] safetyStocks) throws WMSServiceException {
        this.validateEntities(accountBook,safetyStocks);
        for(int i=0;i<safetyStocks.length;i++){
            Condition cond = new Condition();
            cond.addCondition("warehouseId",new Integer[]{safetyStocks[i].getWarehouseId()});
            cond.addCondition("supplyId",new Integer[]{safetyStocks[i].getSupplyId()});
            cond.addCondition("targetStorageLocationId",new Integer[]{safetyStocks[i].getTargetStorageLocationId()});
            cond.addCondition("sourceStorageLocationId",new Integer[]{safetyStocks[i].getSourceStorageLocationId()});
            cond.addCondition("unit",new String[]{safetyStocks[i].getUnit()});
            cond.addCondition("unitAmount",new BigDecimal[]{safetyStocks[i].getUnitAmount()});
            cond.addCondition("type",new Integer[]{safetyStocks[i].getType()});
            SafetyStockView[] safetyStockViews=this.find(accountBook,cond);
            if(safetyStockViews.length > 0){
                throw new WMSServiceException("供货商名称:"+safetyStockViews[0].getSupplierName()+"，物料名称："+safetyStockViews[0].getMaterialName()+"已存在相同安全库存信息！");
            }
        }

        int[]ids= safetyStockDAO.add(accountBook, safetyStocks);
        if (safetyStocks[0].getType()==SafetyStockService.TYPE_READY) {
            for (int i = 0; i < safetyStocks.length; i++) {
                Condition cond = new Condition();
                cond.addCondition("warehouseId", new Integer[]{safetyStocks[i].getWarehouseId()});
                cond.addCondition("supplyId", new Integer[]{safetyStocks[i].getSupplyId()});
                cond.addCondition("targetStorageLocationId", new Integer[]{safetyStocks[i].getTargetStorageLocationId()});
                cond.addCondition("unit", new String[]{safetyStocks[i].getUnit()});
                cond.addCondition("unitAmount", new BigDecimal[]{safetyStocks[i].getUnitAmount()});
                cond.addCondition("type", new Integer[]{safetyStocks[i].getType()});
                SafetyStockView[] safetyStockViews = this.find(accountBook, cond);

                List<SafetyStockView> taxItemList = Arrays.asList(safetyStockViews);
                taxItemList.stream().sorted(Comparator.comparing(SafetyStockView::getAmountMin)).reduce((last, cur) -> {
                    if (last.getAmountMin().compareTo(cur.getAmountMin()) != 0) {
                        throw new WMSServiceException("供货商名称:" + last.getSupplierName() + "，物料名称：" + last.getMaterialName() + "在目标库位：" + last.getTargetStorageLocationName() + "上设置了不一致的安全库存数量！");
                    }
                    return cur;
                });
            }
        }
        return ids;
    }

    @Override
    public void update(String accountBook, SafetyStock[] safetyStocks) throws WMSServiceException {
        this.validateEntities(accountBook,safetyStocks);
        for(int i=0;i<safetyStocks.length;i++){
            Condition cond = new Condition();
            cond.addCondition("warehouseId",new Integer[]{safetyStocks[i].getWarehouseId()});
            cond.addCondition("supplyId",new Integer[]{safetyStocks[i].getSupplyId()});
            cond.addCondition("targetStorageLocationId",new Integer[]{safetyStocks[i].getTargetStorageLocationId()});
            cond.addCondition("sourceStorageLocationId",new Integer[]{safetyStocks[i].getSourceStorageLocationId()});
            cond.addCondition("unit",new String[]{safetyStocks[i].getUnit()});
            cond.addCondition("unitAmount",new BigDecimal[]{safetyStocks[i].getUnitAmount()});
            cond.addCondition("type",new Integer[]{safetyStocks[i].getType()});
            cond.addCondition("id",new Integer[]{safetyStocks[i].getId()}, ConditionItem.Relation.NOT_EQUAL);
            if(this.find(accountBook,cond).length > 0){
                throw new WMSServiceException("已存在相同安全库存信息！");
            }
        }
        safetyStockDAO.update(accountBook, safetyStocks);
        if (safetyStocks[0].getType()==SafetyStockService.TYPE_READY) {
            for (int i = 0; i < safetyStocks.length; i++) {
                Condition cond = new Condition();
                cond.addCondition("warehouseId", new Integer[]{safetyStocks[i].getWarehouseId()});
                cond.addCondition("supplyId", new Integer[]{safetyStocks[i].getSupplyId()});
                cond.addCondition("targetStorageLocationId", new Integer[]{safetyStocks[i].getTargetStorageLocationId()});
                cond.addCondition("unit", new String[]{safetyStocks[i].getUnit()});
                cond.addCondition("unitAmount", new BigDecimal[]{safetyStocks[i].getUnitAmount()});
                cond.addCondition("type", new Integer[]{safetyStocks[i].getType()});
                SafetyStockView[] safetyStockViews = this.find(accountBook, cond);

                List<SafetyStockView> taxItemList = Arrays.asList(safetyStockViews);
                taxItemList.stream().sorted(Comparator.comparing(SafetyStockView::getAmountMin)).reduce((last, cur) -> {
                    if (last.getAmountMin().compareTo(cur.getAmountMin()) != 0) {
                        throw new WMSServiceException("供货商名称:" + last.getSupplierName() + "，物料名称：" + last.getMaterialName() + "在目标库位：" + last.getTargetStorageLocationName() + "上设置了不一致的安全库存数量！");
                    }
                    return cur;
                });
            }
        }
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        for (int id : ids) {
            if (safetyStockDAO.find(accountBook, new Condition().addCondition("id", id)).length == 0) {
                throw new WMSServiceException(String.format("删除安全库存信息不存在，请重新查询！(%d)", id));
            }
        }

        try {
            safetyStockDAO.remove(accountBook, ids);
        } catch (Throwable ex) {
            throw new WMSServiceException("删除安全库存信息失败，如果安全库存信息已经被引用，需要先删除引用该安全库存信息的内容，才能删除该安全库存信息");
        }
    }

    @Override
    public SafetyStockView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return safetyStockDAO.find(accountBook, cond);
    }


    @Override
    public long findCount(String accountBook, Condition cond) throws WMSServiceException{
        return this.safetyStockDAO.findCount(accountBook,cond);
    }

    private void validateEntities(String accountBook, SafetyStock[] safetyStocks) {
        Stream.of(safetyStocks).forEach((safetyStock -> {
            //数据验证
            new Validator("状态").min(0).max(1).validate(safetyStock.getType());
            new Validator("单位").notnull().validate(safetyStock.getUnit());
            new Validator("单位数量").greaterThan(0).validate(safetyStock.getUnitAmount());
            new Validator("源单位").notnull().validate(safetyStock.getSourceUnit());
            new Validator("源单位数量").greaterThan(0).validate(safetyStock.getSourceUnitAmount());

            if(safetyStock.getAmountMax().compareTo(safetyStock.getAmountMin())<0){
                SafetyStockView safetyStockView= this.find(accountBook,new Condition().addCondition("id",safetyStock.getId()))[0];
                throw new WMSServiceException("供货商名称:" + safetyStockView.getSupplierName() + "，物料名称：" + safetyStockView.getMaterialName()+"目标库位："+safetyStockView.getTargetStorageLocationName()+"设置安全库存数小于上架峰值！");
            }
            //验证外键
            this.idChecker.check(StorageLocationService.class, accountBook, safetyStock.getTargetStorageLocationId(), "目标库位");
            this.idChecker.check(StorageLocationService.class, accountBook, safetyStock.getSourceStorageLocationId(), "源库位");
            this.idChecker.check(SupplyService.class, accountBook, safetyStock.getSupplyId(), "关联供货信息");
            if (this.warehouseService.find(accountBook,
                    new Condition().addCondition("id", safetyStock.getWarehouseId())).length == 0) {
                throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)", safetyStock.getWarehouseId()));
            }
        }));

        //数据验证
        Stream.of(safetyStocks).forEach(
                (safetyStock) -> {
                    //new Validator("安全库存数量(个)").min(0).validate(safetyStock.getAmount());
                    new Validator("单位").notnull().validate(safetyStock.getUnit());
                    new Validator("单位数量").min(0).validate(safetyStock.getUnitAmount());
                    new Validator("类型").min(0).max(2).validate(safetyStock.getType());
                    if(safetyStock.getTargetStorageLocationId()==safetyStock.getSourceStorageLocationId()){
                        throw new WMSServiceException("安全库存库位与移出库位不能相同！");
                    }
                }
        );

        for(int i=0;i<safetyStocks.length;i++){
            for(int j=i+1;j<safetyStocks.length;j++){
                int supplyId=safetyStocks[i].getSupplyId();
                int targetStorageLocationId=safetyStocks[i].getTargetStorageLocationId();
                int sourceStorageLocationId=safetyStocks[i].getSourceStorageLocationId();
                String unit=safetyStocks[i].getUnit();
                BigDecimal unitAmount=safetyStocks[i].getUnitAmount();
                int type=safetyStocks[i].getType();
                if(supplyId==safetyStocks[j].getSupplyId()
                        &&targetStorageLocationId==safetyStocks[j].getTargetStorageLocationId()
                        &&sourceStorageLocationId==safetyStocks[j].getSourceStorageLocationId()
                        && unitAmount.equals(safetyStocks[j].getUnitAmount())
                        &&unit.equals(safetyStocks[j].getUnit())
                        &&type==safetyStocks[j].getType())
                {
                    throw new WMSServiceException("安全库存信息在添加的列表中重复!");
                }
            }
        }
    }
}
