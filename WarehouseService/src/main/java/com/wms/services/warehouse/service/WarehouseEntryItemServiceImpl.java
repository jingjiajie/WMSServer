package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.WarehouseEntryItemDAO;
import com.wms.utilities.model.*;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.stream.Stream;

@Service
@Transactional
public class WarehouseEntryItemServiceImpl implements WarehouseEntryItemService {
    @Autowired
    WarehouseEntryItemDAO warehouseEntryItemDAO;
    @Autowired
    WarehouseEntryService warehouseEntryService;
    @Autowired
    StockRecordService stockRecordService;
    @Autowired
    SupplyService supplyService;
    @Autowired
    StorageLocationService storageLocationService;
    @Autowired
    PersonService personService;

    @Override
    public int[] add(String accountBook, WarehouseEntryItem[] warehouseEntryItems) throws WMSServiceException {
        if (warehouseEntryItems.length == 0) return new int[]{};
        WarehouseEntryView warehouseEntryView = this.getWarehouseEntryView(accountBook,warehouseEntryItems);
        //验证字段
        this.validateEntities(accountBook, warehouseEntryItems);
        //增加库存
        Stream.of(warehouseEntryItems).forEach(warehouseEntryItem -> {
            StockRecord stockRecord = new StockRecord();
            stockRecord.setAmount(warehouseEntryItem.getRealAmount());
            stockRecord.setUnit(warehouseEntryItem.getUnit());
            stockRecord.setUnitAmount(warehouseEntryItem.getUnitAmount());
            stockRecord.setInventoryDate(warehouseEntryItem.getInventoryDate());
            stockRecord.setExpiryDate(warehouseEntryItem.getExpiryDate());
            stockRecord.setManufactureDate(warehouseEntryItem.getManufactureDate());
            stockRecord.setRelatedOrderNo(warehouseEntryView.getNo());
            stockRecord.setStorageLocationId(warehouseEntryItem.getStorageLocationId());
            stockRecord.setSupplyId(warehouseEntryItem.getSupplyId());
            stockRecord.setWarehouseId(warehouseEntryView.getWarehouseId());
            //TODO 这里应该用modify
            this.stockRecordService.add(accountBook, new StockRecord[]{stockRecord});
        });
        //添加到数据库中
        int[] ids = this.warehouseEntryItemDAO.add(accountBook, warehouseEntryItems);
        return ids;
    }

    @Override
    public void update(String accountBook, WarehouseEntryItem[] warehouseEntryItems) throws WMSServiceException {
        WarehouseEntryView warehouseEntryView = this.getWarehouseEntryView(accountBook,warehouseEntryItems);
        this.validateEntities(accountBook, warehouseEntryItems);
        Stream.of(warehouseEntryItems).forEach(warehouseEntryItem -> {
            WarehouseEntryItemView[] foundOriItems = this.warehouseEntryItemDAO.find(accountBook,new Condition().addCondition("id",warehouseEntryItem.getId()));
            if(foundOriItems.length == 0) throw new WMSServiceException(String.format("入库单条目不存在，请重新提交！",warehouseEntryItem.getId()));
            WarehouseEntryItemView oriItemView = foundOriItems[0];
            BigDecimal deltaRealAmount = warehouseEntryItem.getRealAmount().subtract(oriItemView.getRealAmount());
            //不用管拒收数量 BigDecimal deltaRefuseAmount = warehouseEntryItem.getRefuseAmount().subtract(oriItemView.getRefuseAmount());
            //送检数量不能改
            BigDecimal deltaInspectionAmount = warehouseEntryItem.getInspectionAmount().subtract(oriItemView.getInspectionAmount());
            if(!deltaInspectionAmount.equals(new BigDecimal(0))){
                throw new WMSServiceException("不允许修改送检数量！");
            }
            //修改实收数量，更新库存
            if(!deltaRealAmount.equals(new BigDecimal(0))){
                //TODO Modify变化数量 不冲抵
//                //TODO 不能用add 冲抵原库存
//                StockRecord stockRecordAgainst = new StockRecord();
//                stockRecordAgainst.setAmount(oriItemView.getRealAmount().multiply(new BigDecimal(-1)));
//                stockRecordAgainst.setUnit(oriItemView.getUnit());
//                stockRecordAgainst.setUnitAmount(oriItemView.getUnitAmount());
//                stockRecordAgainst.setInventoryDate(oriItemView.getInventoryDate());
//                stockRecordAgainst.setExpiryDate(oriItemView.getExpiryDate());
//                stockRecordAgainst.setManufactureDate(oriItemView.getManufactureDate());
//                stockRecordAgainst.setRelatedOrderNo(warehouseEntryView.getNo());
//                stockRecordAgainst.setStorageLocationId(oriItemView.getStorageLocationId());
//                stockRecordAgainst.setSupplyId(oriItemView.getSupplyId());
//                stockRecordAgainst.setWarehouseId(warehouseEntryView.getWarehouseId());
//                this.stockRecordService.add(accountBook, new StockRecord[]{stockRecordAgainst});
//
//                //TODO 不能用add 增加新库存
//                StockRecord newStockRecord = new StockRecord();
//                newStockRecord.setAmount(warehouseEntryItem.getRealAmount());
//                newStockRecord.setUnit(warehouseEntryItem.getUnit());
//                newStockRecord.setUnitAmount(warehouseEntryItem.getUnitAmount());
//                newStockRecord.setInventoryDate(warehouseEntryItem.getInventoryDate());
//                newStockRecord.setExpiryDate(warehouseEntryItem.getExpiryDate());
//                newStockRecord.setManufactureDate(warehouseEntryItem.getManufactureDate());
//                newStockRecord.setRelatedOrderNo(warehouseEntryView.getNo());
//                newStockRecord.setStorageLocationId(warehouseEntryItem.getStorageLocationId());
//                newStockRecord.setSupplyId(warehouseEntryItem.getSupplyId());
//                newStockRecord.setWarehouseId(warehouseEntryView.getWarehouseId());
//                this.stockRecordService.add(accountBook, new StockRecord[]{newStockRecord});
            }
        });
        this.warehouseEntryItemDAO.update(accountBook, warehouseEntryItems);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        try {
            this.warehouseEntryItemDAO.remove(accountBook, ids);
        } catch (Throwable ex) {
            throw new WMSServiceException("删除失败，如果条目已经被引用，需要先删除引用项目");
        }
    }

    @Override
    public WarehouseEntryItemView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.warehouseEntryItemDAO.find(accountBook, cond);
    }

    private void validateEntities(String accountBook, WarehouseEntryItem[] warehouseEntryItems) {
        //数据验证
        Stream.of(warehouseEntryItems).forEach(
                (warehouseEntryItem) -> {
                    new Validator("订单数量").min(0).validate(warehouseEntryItem.getExpectedAmount());
                    new Validator("实收数量").min(0).max(warehouseEntryItem.getExpectedAmount()).validate(warehouseEntryItem.getRealAmount());
                    new Validator("单位数量").min(0).validate(warehouseEntryItem.getUnitAmount());
                    new Validator("已分配送检数量").min(0).max(warehouseEntryItem.getRealAmount()).validate(warehouseEntryItem.getInspectionAmount());
                    new Validator("拒收数量").min(0).max(warehouseEntryItem.getExpectedAmount()).validate(warehouseEntryItem.getRefuseAmount());
                    new Validator("拒收单位").notEmpty().validate(warehouseEntryItem.getRefuseUnit());
                    new Validator("拒收单位数量").min(0).validate(warehouseEntryItem.getRefuseUnitAmount());
                }
        );

        //外键验证
        Stream.of(warehouseEntryItems).forEach(
                (warehouseEntryItem) -> {
                    if (this.warehouseEntryService.find(accountBook,
                            new Condition().addCondition("id", warehouseEntryItem.getWarehouseEntryId())).length == 0) {
                        throw new WMSServiceException(String.format("入库单不存在，请重新提交！(%d)", warehouseEntryItem.getWarehouseEntryId()));
                    } else if (supplyService.find(accountBook,
                            new Condition().addCondition("id", warehouseEntryItem.getSupplyId())).length == 0) {
                        throw new WMSServiceException(String.format("供货信息不存在，请重新提交！(%d)", warehouseEntryItem.getSupplyId()));
                    } else if (storageLocationService.find(accountBook,
                            new Condition().addCondition("id", warehouseEntryItem.getStorageLocationId())).length == 0) {
                        throw new WMSServiceException(String.format("库位不存在，请重新提交！(%d)", warehouseEntryItem.getSupplyId()));
                    } else if (warehouseEntryItem.getPersonId() != null && personService.find(accountBook,
                            new Condition().addCondition("id", warehouseEntryItem.getPersonId())).length == 0) {
                        throw new WMSServiceException(String.format("作业人员不存在，请重新提交！(%d)", warehouseEntryItem.getSupplyId()));
                    }
                }
        );
    }

    private WarehouseEntryView getWarehouseEntryView(String accountBook,WarehouseEntryItem[] warehouseEntryItems){
        //验证入库单号必须全部相同
        Stream.of(warehouseEntryItems).reduce((last, cur) -> {
            if (last.getWarehouseEntryId() != cur.getWarehouseEntryId())
                throw new WMSServiceException("入库单条目所属的入库单必须相同！");
            return cur;
        });
        //获取入库单
        int warehouseEntryID = warehouseEntryItems[0].getWarehouseEntryId();
        final WarehouseEntryView[] warehouseEntryViews = this.warehouseEntryService.find(accountBook, new Condition().addCondition("id", warehouseEntryID));
        if (warehouseEntryViews.length == 0)
            throw new WMSServiceException(String.format("入库单(%d)不存在，请重新提交！", warehouseEntryID));
        WarehouseEntryView warehouseEntryView = warehouseEntryViews[0];
        return warehouseEntryView;
    }
}
