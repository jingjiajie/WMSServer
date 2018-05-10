package com.wms.services.warehouse.service;


import com.wms.services.warehouse.dao.StockRecordDAO;
import com.wms.services.warehouse.datastructures.StockRecordFind;
import com.wms.services.warehouse.datastructures.TransferStock;
import com.wms.utilities.IDChecker;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.datastructures.OrderItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional
public class StockRecordServiceImpl implements StockRecordService {
    @Autowired
    StockRecordDAO stockRecordDAO;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    StorageLocationService storageLocationService;
    @Autowired
    SupplyService supplyService;
    @Autowired
    TransferRecordService transformRecordService;
    @Autowired
    StorageAreaService storageAreaService;
    @Autowired
    IDChecker idChecker;

    @Override
    public int[] add(String accountBook, StockRecord[] stockRecords) throws WMSServiceException {

for(int i=0;i<stockRecords.length;i++) {
    new Validator("数量").notnull().notEmpty().min(0);
    new Validator("单位").notnull().notEmpty();
    new Validator("单位数量").notnull().notEmpty().min(0);

}
        //外键检测
        Stream.of(stockRecords).forEach(
                (stockRecord)->{
                    if(this.warehouseService.find(accountBook,
                            new Condition().addCondition("id",stockRecord.getWarehouseId())).length == 0){
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",stockRecord.getWarehouseId()));
                    }
                    else  if(this.storageLocationService.find(accountBook,
                            new Condition().addCondition("id",stockRecord.getStorageLocationId())).length == 0)
                    {
                        throw new WMSServiceException(String.format("库位不存在，请重新提交！(%d)",stockRecord.getStorageLocationId()));
                    }
                    else if( this.supplyService.find(accountBook,
                            new Condition().addCondition("id",stockRecord.getSupplyId())).length == 0){
                        throw new WMSServiceException(String.format("供货信息不存在，请重新提交！(%d)",stockRecord.getSupplyId()));
                    }
                }
        );

        for (int i=0;i<stockRecords.length;i++)
        {
            stockRecords[i].setTime(new Timestamp(System.currentTimeMillis()));
            stockRecords[i].setBatchNo(stockRecords[i].getInventoryDate().toString());
        }
        return stockRecordDAO.add(accountBook,stockRecords);
    }

@Override
public  void update(String accountBook,StockRecord[] stockRecords) throws WMSServiceException {
    for(int i=0;i<stockRecords.length;i++) {
        new Validator("数量").notnull().notEmpty().min(0);
        new Validator("单位").notnull().notEmpty();
        new Validator("单位数量").notnull().notEmpty().min(0);
    }
        //外键检测
        Stream.of(stockRecords).forEach(
                (stockRecord)->{
                    if(this.warehouseService.find(accountBook,
                            new Condition().addCondition("id",stockRecord.getWarehouseId())).length == 0){
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",stockRecord.getWarehouseId()));
                    }
                    else  if(this.storageLocationService.find(accountBook,
                            new Condition().addCondition("id",stockRecord.getStorageLocationId())).length == 0)
                    {
                        throw new WMSServiceException(String.format("库位不存在，请重新提交！(%d)",stockRecord.getStorageLocationId()));
                    }
                    else if( this.supplyService.find(accountBook,
                            new Condition().addCondition("id",stockRecord.getSupplyId())).length == 0){
                        throw new WMSServiceException(String.format("供货信息不存在，请重新提交！(%d)",stockRecord.getSupplyId()));
                    }
                }
        );

        for (int i=0;i<stockRecords.length;i++)
        {
            stockRecords[i].setTime(new Timestamp(System.currentTimeMillis()));
        }
        stockRecordDAO.update(accountBook,stockRecords);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
        try {
            stockRecordDAO.remove(accountBook, ids);
        }
        catch (Throwable ex){
            throw new WMSServiceException("删除库存记录信息失败，如果库存记录信息已经被引用，需要先删除引用的内容，才能删除该供应商");
        }
    }

    @Override
    public StockRecordView[] find(String accountBook, Condition cond) throws WMSServiceException{
        return this.stockRecordDAO.find(accountBook, cond);
    }


    @Override
    public void RealTransformStock(String accountBook, TransferStock transferStock)
    {
        new Validator("相关单号").notEmpty().notnull().validate(transferStock.relatedOrderNo);
        new Validator("单位数量").notnull().min(0).validate(transferStock.getUnitAmount());
        new Validator("单位").notnull().notEmpty().validate(transferStock.getUnit());
        new Validator("数量").notnull().validate(transferStock.getAmount());
        //new Validator("存货时间").notnull().validate(transferStock.getInventoryDate());

        int sourceStorageLocationId=transferStock.getSourceStorageLocationId();
        int supplyId=transferStock.getSupplyId();
        int newStorageLocationId=transferStock.getNewStorageLocationId();
        BigDecimal amount=transferStock.getAmount();
        String unit=transferStock.getUnit();
        BigDecimal unitAmount=transferStock.getUnitAmount();
        String relatedOrderNo=transferStock.getRelatedOrderNo();

        //先查出最新源库存记录和新库位
        StockRecordView[] stockRecordSource1= stockRecordDAO.find(accountBook,new Condition().addCondition("supplyId",new Integer[]{supplyId}).
                addCondition("storageLocationId",new Integer[]{sourceStorageLocationId}).addCondition("unit",unit).addCondition("unitAmount",unitAmount).
                addCondition("inventoryDate",new String[] {transferStock.getBatchNo()}));

        if(stockRecordSource1.length==0)
        {
            throw new WMSServiceException("没查到符合要求的源库存记录，请检查相关信息！");
        }

        StockRecordView stockRecordNewest1= stockRecordSource1[0] ;
        Timestamp newestTime1 = stockRecordNewest1.getTime();
        for (int i = 1; i < stockRecordSource1.length; i++) {
            if (newestTime1.compareTo(stockRecordSource1[i].getTime())==-1) {
                newestTime1 = stockRecordSource1[i].getTime();
                stockRecordNewest1 = stockRecordSource1[i];
            }
        }

        StockRecordView[] stockRecordSource=new StockRecordView[] {stockRecordNewest1};

        if(stockRecordSource[0].getAvailableAmount().compareTo(amount)==-1)
        {
            throw new WMSServiceException("移动的数量不能大于可用数量！");
        }

        StorageLocationView[] storageLocationNew= storageLocationService.find(accountBook,new Condition().addCondition("id",new Integer[]{newStorageLocationId}));

        if(storageLocationNew.length!=1)
        {
            throw new WMSServiceException("请检查输入的新库位id，没有找到相关记录");
        }

        int sourceStockRecordId=stockRecordSource[0].getId();
        //查到新库位上最新的相同供货的记录
        StockRecordView[] stockRecordViews=stockRecordDAO.find(accountBook, new Condition().addCondition( "storageLocationId",new Integer[]{newStorageLocationId}).
                addCondition("supplyId",new Integer[]{supplyId}).addCondition("unit",new String[]{unit}).addCondition("unitAmount",new BigDecimal[]{unitAmount})
        .addCondition("inventoryDate",new Timestamp[]{transferStock.getInventoryDate()}));
        StockRecordView stockRecordNewest= null;
        //新建两条库存记录
        StockRecord stockRecordSourceSave=new StockRecord();
        StockRecord stockRecordNewSave=new StockRecord();
        //第一条和源记录相同 只改变数量和相关单号

        stockRecordSourceSave.setWarehouseId(stockRecordSource[0].getWarehouseId());
        stockRecordSourceSave.setStorageLocationId(stockRecordSource[0].getStorageLocationId());
        stockRecordSourceSave.setSupplyId(stockRecordSource[0].getSupplyId());
        stockRecordSourceSave.setExpiryDate(stockRecordSource[0].getExpiryDate());
        stockRecordSourceSave.setInventoryDate(stockRecordSource[0].getInventoryDate());
        stockRecordSourceSave.setManufactureDate(stockRecordSource[0].getManufactureDate());
        stockRecordSourceSave.setUnitAmount(stockRecordSource[0].getUnitAmount());
        stockRecordSourceSave.setUnit(stockRecordSource[0].getUnit());
        stockRecordSourceSave.setAmount(stockRecordSource[0].getAmount().subtract(amount));
        stockRecordSourceSave.setAvailableAmount(stockRecordSource[0].getAvailableAmount().subtract(amount));
        stockRecordSourceSave.setRelatedOrderNo(relatedOrderNo);
        stockRecordSourceSave.setBatchNo(stockRecordSource[0].getBatchNo());
        stockRecordSourceSave.setTime(new Timestamp(System.currentTimeMillis()));

        if(stockRecordViews.length>0) {
            stockRecordNewest = stockRecordViews[0];
            Timestamp newestTime = stockRecordNewest.getTime();
            for (int i = 1; i < stockRecordViews.length; i++) {
                if (newestTime.compareTo(stockRecordViews[i].getTime()) == -1) {
                    newestTime = stockRecordViews[i].getTime();
                    stockRecordNewest = stockRecordViews[i];
                }
            }

            //增加第二条库存记录
            //已经找到最新的可以叠加的记录，则第二条为叠加
            stockRecordNewSave.setAmount(amount.add(stockRecordNewest.getAmount()));
            stockRecordNewSave.setAvailableAmount(stockRecordSource[0].getAvailableAmount().add(stockRecordNewest.getAmount()));
            stockRecordNewSave.setUnit(stockRecordNewest.getUnit());
            stockRecordNewSave.setUnitAmount(unitAmount);
            stockRecordNewSave.setRelatedOrderNo(relatedOrderNo);
            stockRecordNewSave.setExpiryDate(stockRecordSource[0].getExpiryDate());
            stockRecordNewSave.setInventoryDate(stockRecordSource[0].getInventoryDate());
            stockRecordNewSave.setManufactureDate(stockRecordSource[0].getManufactureDate());
            stockRecordNewSave.setStorageLocationId(newStorageLocationId);
            stockRecordNewSave.setWarehouseId(stockRecordSource[0].getWarehouseId());
            stockRecordNewSave.setSupplyId(supplyId);
            stockRecordNewSave.setBatchNo(stockRecordSource[0].getBatchNo());
            stockRecordNewSave.setTime(new Timestamp(System.currentTimeMillis()));
        }
        //如果没有数量直接为新数量
        else
            {
                stockRecordNewSave.setAmount(amount);
                stockRecordNewSave.setAvailableAmount(amount);
            }

        stockRecordDAO.add(accountBook,new StockRecord[]{stockRecordSourceSave});
        int[] newStockRecordId =stockRecordDAO.add(accountBook,new StockRecord[]{stockRecordNewSave});
        if(newStockRecordId.length!=1)
        {
            throw new WMSServiceException("添加新库存记录失败！");
        }
        //添加一条移位记录
        TransferRecord transferRecord=new TransferRecord();
        transferRecord.setNewStockRecordId(newStockRecordId[0]);
        transferRecord.setSourceStockRecordId(sourceStockRecordId);
        transferRecord.setWarehouseId(stockRecordSource[0].getWarehouseId());
        transformRecordService.add(accountBook,new TransferRecord[]{transferRecord});
    }

    @Override
    public void addAmount(String accountBook,TransferStock transferStock )
    {
        new Validator("相关单号").notEmpty().notnull().validate(transferStock.relatedOrderNo);
        //new Validator("批号").notnull().notEmpty().validate(transferStock.getBatchNo());
        new Validator("单位数量").notnull().min(0).validate(transferStock.getUnitAmount());
        new Validator("单位").notnull().notEmpty().validate(transferStock.getUnit());
        new Validator("数量").notnull().validate(transferStock.getAmount());
        new Validator("存货时间").notnull().validate(transferStock.getInventoryDate());
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");
        String batchNo=format.format(transferStock.getInventoryDate());
        int sourceStorageLocationId=transferStock.getSourceStorageLocationId();
        Integer[] warehouseId=warehouseIdFind(accountBook,sourceStorageLocationId);//至少能返回一个
        idChecker.check(WarehouseService.class,accountBook,warehouseId[0],"仓库");
        int supplyId=transferStock.getSupplyId();
        BigDecimal amount=transferStock.getAmount();
        String unit=transferStock.getUnit();
        BigDecimal unitAmount=transferStock.getUnitAmount();
        //先查出源库存记录
        StockRecordView[] stockRecordSource= stockRecordDAO.find(accountBook,new Condition().addCondition("supplyId",new Integer[]{supplyId}).
                addCondition("storageLocationId",new Integer[]{sourceStorageLocationId}).addCondition("unit",unit).addCondition("unitAmount",unitAmount)
        .addCondition("inventoryDate",new Timestamp[]{transferStock.getInventoryDate()}).addOrder("time",OrderItem.Order.DESC ));
        // 降序第一条就为最新的

        //如果没找到原纪录或原纪录数量为0且为移出
        if (stockRecordSource.length==0||(stockRecordSource[0].getAvailableAmount().compareTo(new BigDecimal(0)) == 0))
        {
            if (amount.compareTo(new BigDecimal(0)) == -1) {
                throw new WMSServiceException("所选库位无相关物料，无法移出!");
            }
        }

        //其余情况无错误
        StockRecord stockRecord = new StockRecord();
        //如果没有原纪录由于以上判断也不可能为移出所以直接新建一条
        if(stockRecordSource.length==0){
            stockRecord.setUnit(unit);
            stockRecord.setUnitAmount(unitAmount);
            stockRecord.setRelatedOrderNo(transferStock.getRelatedOrderNo());
            stockRecord.setWarehouseId(warehouseId[0]);
            stockRecord.setBatchNo(batchNo);
            stockRecord.setInventoryDate(transferStock.getInventoryDate());
            stockRecord.setStorageLocationId(sourceStorageLocationId);
            stockRecord.setSupplyId(supplyId);
            stockRecord.setTime(new Timestamp(System.currentTimeMillis()));
            stockRecordDAO.add(accountBook, new StockRecord[]{stockRecord});
            stockRecord.setAmount(amount);
            stockRecord.setAvailableAmount(amount);
        }
        //如果有原纪录
        else {
        if(amount.add(stockRecordSource[0].getAvailableAmount()).compareTo(new BigDecimal(0))!=1)
        {
            throw new WMSServiceException("移出数量不能比可用数量大");
        }
        StockRecordView stockRecordNewest= stockRecordSource[0];
        Timestamp newestTime = stockRecordNewest.getTime();
        for (int i = 0; i < stockRecordSource.length; i++) {
            if (newestTime.compareTo(stockRecordSource[i].getTime())==-1) {
                newestTime = stockRecordSource[i].getTime();
                stockRecordNewest = stockRecordSource[i];
            }
        }
        StockRecord stockRecord1=new StockRecord();
        stockRecord1.setAmount(stockRecordNewest.getAmount().add(amount));
        stockRecord1.setAvailableAmount(stockRecordNewest.getAvailableAmount().add(amount));
        stockRecord1.setUnit(unit);
        stockRecord1.setUnitAmount(unitAmount);
        stockRecord1.setRelatedOrderNo(stockRecordNewest.getRelatedOrderNo());
        stockRecord1.setWarehouseId(stockRecordNewest.getWarehouseId());
        stockRecord1.setManufactureDate(stockRecordNewest.getManufactureDate());
        stockRecord1.setExpiryDate(stockRecordNewest.getExpiryDate());
        stockRecord1.setInventoryDate(stockRecordNewest.getInventoryDate());
        stockRecord1.setStorageLocationId(stockRecordNewest.getStorageLocationId());
        stockRecord1.setSupplyId(supplyId);
        stockRecord1.setTime(new Timestamp(System.currentTimeMillis()));
        stockRecordDAO.add(accountBook,new StockRecord[]{stockRecord1});
        }
    }

    @Override
     public  StockRecordView[] find(String accountBook, StockRecordFind stockRecordFind)
    {
        Condition condition=stockRecordFind.getCondition();
         List<StockRecordView> stockRecordViews=new ArrayList<StockRecordView>();
        if(stockRecordFind.getTimestamp()!=null) {
            condition.addCondition("time", new Timestamp[]{stockRecordFind.getTimestamp()}, ConditionItem.Relation.LESS_THAN).
                    addOrder("time", OrderItem.Order.DESC);
        }
        if(stockRecordFind.getStorageLocationId()!=null)
        {condition.addCondition("storageLocationId",new Integer[]{stockRecordFind.getStorageLocationId()});}
        if(stockRecordFind.getSupplyId()!=null)
        { condition.addCondition("supplyId",new Integer[]{stockRecordFind.getSupplyId()});}
        if(stockRecordFind.getWarehouseId()!=null)
        {condition.addCondition("warehouseId",new Integer[]{stockRecordFind.getWarehouseId()});}
        if(stockRecordFind.getUnit()!=null)
        {condition.addCondition("unit",new String[]{stockRecordFind.getUnit()});}
        if(stockRecordFind.getUnitAomunt()!=null)
        {condition.addCondition("unitAmount",new BigDecimal[]{stockRecordFind.getUnitAomunt()});}

        while(stockRecordDAO.find(accountBook,condition).length>0) {
            StockRecordView[] stockRecordTemp = stockRecordDAO.find(accountBook, condition);
            stockRecordViews.add(stockRecordTemp[0]);
            if(stockRecordFind.getStorageLocationId()!=null)
            {condition.addCondition("storageLocationId",new Integer[]{stockRecordFind.getStorageLocationId()}, ConditionItem.Relation.NOT_EQUAL);}
            if(stockRecordFind.getSupplyId()!=null)
            { condition.addCondition("supplyId",new Integer[]{stockRecordFind.getSupplyId()}, ConditionItem.Relation.NOT_EQUAL);}
            if(stockRecordFind.getWarehouseId()!=null)
            {condition.addCondition("warehouseId",new Integer[]{stockRecordFind.getWarehouseId()}, ConditionItem.Relation.NOT_EQUAL);}
            if(stockRecordFind.getUnit()!=null)
            {condition.addCondition("unit",new String[]{stockRecordFind.getUnit()}, ConditionItem.Relation.NOT_EQUAL);}
            if(stockRecordFind.getUnitAomunt()!=null)
            {condition.addCondition("unitAmount",new BigDecimal[]{stockRecordFind.getUnitAomunt()}, ConditionItem.Relation.NOT_EQUAL);}
        }
        StockRecordView[] stockRecordViews1=new StockRecordView[stockRecordViews.size()];
        StockRecordView[] stockRecordViews2=stockRecordViews.toArray(stockRecordViews1);
        return stockRecordViews2;
    }






    private Integer[] warehouseIdFind(String accountBook,int storageLocationId) {
        StorageLocationView[] storageLocationViews = storageLocationService.find(accountBook, new Condition().addCondition("id", new Integer[]{storageLocationId}));
        if (storageLocationViews.length != 1) {
            throw new WMSServiceException("没有找到相关库位！");}
            StorageAreaView[] storageAreaViews = storageAreaService.find(accountBook, new Condition().addCondition("id", new Integer[]{storageLocationViews[0].getStorageAreaId()}));
            if (storageAreaViews.length != 1) {
                throw new WMSServiceException("没有找到相关库区!");
            }
        return    new Integer[] {storageAreaViews[0].getWarehouseId()};
    }

    public void modifyAvailableAmount(String accountBook,TransferStock transferStock){
        //只改变可用数量
        new Validator("实际数量变化值").notnull().validate(transferStock.getModifyAvailableAmount());
        BigDecimal modifyAvailableAmoun=transferStock.getModifyAvailableAmount();
        new Validator("单位数量").notnull().min(0).validate(transferStock.getUnitAmount());
        new Validator("单位").notnull().notEmpty().validate(transferStock.getUnit());
        new Validator("存货时间").notnull().validate(transferStock.getInventoryDate());
        idChecker.check(StorageLocationService.class,accountBook,transferStock.getSourceStorageLocationId(),"库位");
        idChecker.check(SupplyService.class,accountBook,transferStock.getSupplyId(),"供货信息");

        //先查出源库存记录
        StockRecordView[] stockRecordSource= stockRecordDAO.find(accountBook,new Condition().addCondition("supplyId",new Integer[]{transferStock.getSupplyId()}).
                addCondition("storageLocationId",new Integer[]{transferStock.getSourceStorageLocationId()}).addCondition("unit",new String[]{transferStock.getUnit()}).addCondition("unitAmount",transferStock.getUnitAmount())
                .addCondition("inventoryDate",new Timestamp[]{transferStock.getInventoryDate()}).addOrder("time",OrderItem.Order.DESC ));
        if(stockRecordSource.length==0){
            throw new WMSServiceException("没有找到相关库存记录，请检查输入信息");
        }
        StockRecord stockRecord=new StockRecord();
        //复制所有信息
        stockRecord.setId(stockRecordSource[0].getId());
        stockRecord.setStorageLocationId(transferStock.getSourceStorageLocationId());
        stockRecord.setAmount(stockRecordSource[0].getAmount());
        stockRecord.setAvailableAmount(stockRecordSource[0].getAvailableAmount().add(transferStock.getModifyAvailableAmount()));
        stockRecord.setUnit(transferStock.getUnit());
        stockRecord.setUnitAmount(transferStock.getUnitAmount());
        stockRecord.setRelatedOrderNo(stockRecordSource[0].getRelatedOrderNo());
        stockRecord.setWarehouseId(stockRecordSource[0].getWarehouseId());
        stockRecord.setManufactureDate(stockRecordSource[0].getManufactureDate());
        stockRecord.setExpiryDate(stockRecordSource[0].getExpiryDate());
        stockRecord.setInventoryDate(stockRecordSource[0].getInventoryDate());
        stockRecord.setStorageLocationId(stockRecordSource[0].getStorageLocationId());
        stockRecord.setSupplyId(transferStock.getSupplyId());
        stockRecord.setTime(stockRecordSource[0].getTime());
        stockRecordDAO.update(accountBook,new StockRecord[]{stockRecord});
    }

}
