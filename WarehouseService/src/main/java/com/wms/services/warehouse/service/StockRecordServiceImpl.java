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

        int sourceStorageLocationId=transferStock.getSourceStorageLocationId();
        int supplyId=transferStock.getSupplyId();
        int newStorageLocationId=transferStock.getNewStorageLocationId();
        BigDecimal amount=transferStock.getAmount();
        String unit=transferStock.getUnit();
        BigDecimal unitAmount=transferStock.getUnitAmount();
        String relatedOrderNo=transferStock.getRelatedOrderNo();
        idChecker.check(StorageLocationService.class,accountBook,newStorageLocationId,"库位");
        //先查出最新源库存记录和新库位
        //StockRecordView[] stockRecordSource1= stockRecordDAO.find(accountBook,new Condition().addCondition("supplyId",new Integer[]{supplyId}).
         // addCondition("storageLocationId",new Integer[]{sourceStorageLocationId}).addCondition("unit",unit).addCondition("unitAmount",unitAmount));

        StockRecordFind stockRecordFind=new StockRecordFind();
        stockRecordFind.setSupplyId(supplyId);
        stockRecordFind.setStorageLocationId(sourceStorageLocationId);
        stockRecordFind.setUnit(unit);
        stockRecordFind.setUnitAmount(unitAmount);
        StockRecordView[]   stockRecordSource1=this.find(accountBook,stockRecordFind);
        if(stockRecordSource1.length==0)
        {
            throw new WMSServiceException("没查到符合要求的源库存记录，请检查相关信息！");
        }

        //进行排序
        for(int i=0;i<stockRecordSource1.length;i++)
        {
            for(int j=i+1;i<stockRecordSource1.length;j++)
            {
                if(stockRecordSource1[i].getInventoryDate().compareTo(stockRecordSource1[j].getInventoryDate())>=0)
                {
                    StockRecordView temp=new StockRecordView();
                    stockRecordSource1[i]=stockRecordSource1[j];
                    stockRecordSource1[j]=temp;
                }
            }
        }
        //排序之后最后一条为最久的
        BigDecimal amountAvailableAll=BigDecimal.ZERO;
        int iNeed=-1;
        for(int i=stockRecordSource1.length-1;i>=0;i--){
            amountAvailableAll=amountAvailableAll.add(stockRecordSource1[i].getAvailableAmount());
            //如果加到某个记录够移出数量 则跳出并记录下i
            if(amountAvailableAll.add(transferStock.getAmount()).compareTo(BigDecimal.ZERO)>=0){
                iNeed=i;
                break;}
        }
        if(iNeed==-1){ throw new WMSServiceException("可用数量不足，无法进行移出操作!"); }

        for(int i=stockRecordSource1.length-1;i>=iNeed;i--){
            StockRecord stockRecordNewSave=new StockRecord();
            if(i>iNeed)
            {
                StockRecord stockRecord = new StockRecord();
                stockRecord.setUnit(unit);
                stockRecord.setUnitAmount(unitAmount);
                stockRecord.setRelatedOrderNo(transferStock.getRelatedOrderNo());
                stockRecord.setWarehouseId(stockRecordSource1[i].getWarehouseId());
                stockRecord.setBatchNo(stockRecordSource1[i].getBatchNo());
                stockRecord.setInventoryDate(stockRecordSource1[i].getInventoryDate());
                stockRecord.setStorageLocationId(sourceStorageLocationId);
                stockRecord.setSupplyId(supplyId);
                stockRecord.setTime(new Timestamp(System.currentTimeMillis()));
                stockRecord.setAmount(stockRecordSource1[i].getAmount().subtract(stockRecordSource1[i].getAvailableAmount()));
                stockRecord.setAvailableAmount(BigDecimal.ZERO);
                stockRecordDAO.add(accountBook, new StockRecord[]{stockRecord});

                //查到新库位上最新的相同供货的记录
                StockRecordView[] stockRecordViews=stockRecordDAO.find(accountBook, new Condition().addCondition( "storageLocationId",new Integer[]{newStorageLocationId}).
                        addCondition("supplyId",new Integer[]{supplyId}).addCondition("unit",new String[]{unit}).addCondition("unitAmount",new BigDecimal[]{unitAmount}).
                        addCondition("batchNo",new String[]{stockRecordSource1[i].getBatchNo()}).addOrder("time",OrderItem.Order.DESC)
                );

                //已经找到最新的可以叠加的记录，则第二条为叠加
                if(stockRecordViews.length>0){
                    stockRecordNewSave.setAmount(stockRecordViews[0].getAmount().add(stockRecordSource1[i].getAvailableAmount()));
                    stockRecordNewSave.setAvailableAmount(stockRecordViews[0].getAvailableAmount().add(stockRecordSource1[i].getAvailableAmount()));
                    stockRecordNewSave.setUnitAmount(unitAmount);
                    stockRecordNewSave.setRelatedOrderNo(relatedOrderNo);
                    stockRecordNewSave.setExpiryDate(stockRecordSource1[i].getExpiryDate());
                    stockRecordNewSave.setInventoryDate(stockRecordSource1[i].getInventoryDate());
                    stockRecordNewSave.setManufactureDate(stockRecordSource1[i].getManufactureDate());
                    stockRecordNewSave.setStorageLocationId(newStorageLocationId);
                    stockRecordNewSave.setWarehouseId(stockRecordSource1[i].getWarehouseId());
                    stockRecordNewSave.setSupplyId(supplyId);
                    stockRecordNewSave.setBatchNo(stockRecordSource1[i].getBatchNo());
                    stockRecordNewSave.setTime(new Timestamp(System.currentTimeMillis()));
                }
                else
                {
                    stockRecordNewSave.setAmount(amount);
                    stockRecordNewSave.setAvailableAmount(amount);
                    stockRecordNewSave.setUnit(unit);
                    stockRecordNewSave.setUnitAmount(unitAmount);
                    stockRecordNewSave.setRelatedOrderNo(relatedOrderNo);
                    stockRecordNewSave.setExpiryDate(stockRecordSource1[i].getExpiryDate());
                    stockRecordNewSave.setInventoryDate(stockRecordSource1[i].getInventoryDate());
                    stockRecordNewSave.setManufactureDate(stockRecordSource1[i].getManufactureDate());
                    stockRecordNewSave.setStorageLocationId(newStorageLocationId);
                    stockRecordNewSave.setWarehouseId(stockRecordSource1[i].getWarehouseId());
                    stockRecordNewSave.setSupplyId(supplyId);
                    stockRecordNewSave.setBatchNo(stockRecordSource1[i].getBatchNo());
                    stockRecordNewSave.setTime(new Timestamp(System.currentTimeMillis()));
                }
            }
            else if(i==iNeed){
                StockRecord stockRecord = new StockRecord();
                stockRecord.setUnit(unit);
                stockRecord.setUnitAmount(unitAmount);
                stockRecord.setRelatedOrderNo(transferStock.getRelatedOrderNo());
                stockRecord.setWarehouseId(stockRecordSource1[i].getWarehouseId());
                stockRecord.setBatchNo(stockRecordSource1[i].getBatchNo());
                stockRecord.setInventoryDate(stockRecordSource1[i].getInventoryDate());
                stockRecord.setStorageLocationId(sourceStorageLocationId);
                stockRecord.setSupplyId(supplyId);
                stockRecord.setTime(new Timestamp(System.currentTimeMillis()));
                stockRecord.setAmount(stockRecordSource1[i].getAmount().subtract(amountAvailableAll.subtract(transferStock.getAmount())));
                stockRecord.setAvailableAmount(stockRecordSource1[i].getAvailableAmount().subtract(amountAvailableAll.subtract(transferStock.getAmount())));
                stockRecordDAO.add(accountBook, new StockRecord[]{stockRecord});

                //查到新库位上最新的相同供货的记录
                StockRecordView[] stockRecordViews=stockRecordDAO.find(accountBook, new Condition().addCondition( "storageLocationId",new Integer[]{newStorageLocationId}).
                        addCondition("supplyId",new Integer[]{supplyId}).addCondition("unit",new String[]{unit}).addCondition("unitAmount",new BigDecimal[]{unitAmount}).
                        addCondition("batchNo",new String[]{stockRecordSource1[i].getBatchNo()}).addOrder("time",OrderItem.Order.DESC)
                );
                //已经找到最新的可以叠加的记录，则第二条为叠加
                if(stockRecordViews.length>0){
                    stockRecordNewSave.setAmount(stockRecordViews[0].getAmount().add(amountAvailableAll.subtract(transferStock.getAmount())));
                    stockRecordNewSave.setAvailableAmount(stockRecordViews[0].getAvailableAmount().add(amountAvailableAll.subtract(transferStock.getAmount())));
                    stockRecordNewSave.setRelatedOrderNo(relatedOrderNo);
                    stockRecordNewSave.setExpiryDate(stockRecordSource1[i].getExpiryDate());
                    stockRecordNewSave.setInventoryDate(stockRecordSource1[i].getInventoryDate());
                    stockRecordNewSave.setManufactureDate(stockRecordSource1[i].getManufactureDate());
                    stockRecordNewSave.setStorageLocationId(newStorageLocationId);
                    stockRecordNewSave.setWarehouseId(stockRecordSource1[i].getWarehouseId());
                    stockRecordNewSave.setSupplyId(supplyId);
                    stockRecordNewSave.setBatchNo(stockRecordSource1[i].getBatchNo());
                    stockRecordNewSave.setTime(new Timestamp(System.currentTimeMillis()));
                }
                else
                {
                    stockRecordNewSave.setAmount(amountAvailableAll.subtract(transferStock.getAmount()));
                    stockRecordNewSave.setAvailableAmount(amountAvailableAll.subtract(transferStock.getAmount()));
                    stockRecordNewSave.setUnit(unit);
                    stockRecordNewSave.setUnitAmount(unitAmount);
                    stockRecordNewSave.setRelatedOrderNo(relatedOrderNo);
                    stockRecordNewSave.setExpiryDate(stockRecordSource1[i].getExpiryDate());
                    stockRecordNewSave.setInventoryDate(stockRecordSource1[i].getInventoryDate());
                    stockRecordNewSave.setManufactureDate(stockRecordSource1[i].getManufactureDate());
                    stockRecordNewSave.setStorageLocationId(newStorageLocationId);
                    stockRecordNewSave.setWarehouseId(stockRecordSource1[i].getWarehouseId());
                    stockRecordNewSave.setSupplyId(supplyId);
                    stockRecordNewSave.setBatchNo(stockRecordSource1[i].getBatchNo());
                    stockRecordNewSave.setTime(new Timestamp(System.currentTimeMillis()));
                }

                int[] newStockRecordId =stockRecordDAO.add(accountBook,new StockRecord[]{stockRecordNewSave});
                if(newStockRecordId.length!=1)
                {
                    throw new WMSServiceException("添加新库存记录失败！");
                }
                //添加一条移位记录
                TransferRecord transferRecord=new TransferRecord();
                transferRecord.setNewStockRecordId(newStockRecordId[0]);
                transferRecord.setSourceStockRecordId(stockRecordSource1[i].getStorageLocationId());
                transferRecord.setWarehouseId(stockRecordSource1[0].getWarehouseId());
                transformRecordService.add(accountBook,new TransferRecord[]{transferRecord});
            }
        }
    }

    @Override
    public void addAmount(String accountBook,TransferStock transferStock )
    {
        new Validator("相关单号").notEmpty().notnull().validate(transferStock.relatedOrderNo);
        new Validator("单位数量").notnull().min(0).validate(transferStock.getUnitAmount());
        new Validator("单位").notnull().notEmpty().validate(transferStock.getUnit());
        new Validator("数量").notnull().validate(transferStock.getAmount());
        int sourceStorageLocationId=transferStock.getSourceStorageLocationId();
        int supplyId=transferStock.getSupplyId();
        String batchNo="";
        Integer[] warehouseId=warehouseIdFind(accountBook,sourceStorageLocationId);//至少能返回一个
        idChecker.check(WarehouseService.class,accountBook,warehouseId[0],"仓库");
        idChecker.check(StorageLocationService.class,accountBook,sourceStorageLocationId,"库位");
        idChecker.check(SupplyService.class,accountBook,supplyId,"供货");
        if(transferStock.getAmount().compareTo(new BigDecimal(0)) >=0){
            new Validator("存货日期").notnull().validate(transferStock.getInventoryDate());
            batchNo=this.batchTransfer(transferStock.getInventoryDate());}
        BigDecimal amount=transferStock.getAmount();
        String unit=transferStock.getUnit();
        BigDecimal unitAmount=transferStock.getUnitAmount();

        //先查出源库存记录
        StockRecordFind stockRecordFind=new StockRecordFind();
        stockRecordFind.setSupplyId(supplyId);
        stockRecordFind.setStorageLocationId(sourceStorageLocationId);
        stockRecordFind.setUnit(unit);
        stockRecordFind.setUnitAmount(unitAmount);
        if(transferStock.getAmount().compareTo(new BigDecimal(0)) >=0){
            stockRecordFind.setInventaryDate(transferStock.getInventoryDate());
        }
        StockRecordView[] stockRecordSource= stockRecordDAO.find(accountBook,new Condition().addCondition("supplyId",new Integer[]{supplyId}).
                addCondition("storageLocationId",new Integer[]{sourceStorageLocationId}).addCondition("unit",unit).addCondition("unitAmount",unitAmount)
        .addCondition("batchNo",new String[]{this.batchTransfer(transferStock.getInventoryDate())}).addOrder("time",OrderItem.Order.DESC ));

        //根据以上条件如果为增加只应该有一条 如果为减少则应该为所有批次
        stockRecordSource=this.find(accountBook,stockRecordFind);

        //移入的情况
        if(transferStock.getAmount().compareTo(new BigDecimal(0)) >=0){
            // 没有相关记录，就新建一条,这种记录没有生产日期和失效日期
            if(stockRecordSource.length==0) {
                StockRecord stockRecord = new StockRecord();
                stockRecord.setUnit(unit);
                stockRecord.setUnitAmount(unitAmount);
                stockRecord.setRelatedOrderNo(transferStock.getRelatedOrderNo());
                stockRecord.setWarehouseId(warehouseId[0]);
                stockRecord.setBatchNo(batchNo);
                stockRecord.setInventoryDate(transferStock.getInventoryDate());
                stockRecord.setStorageLocationId(sourceStorageLocationId);
                stockRecord.setSupplyId(supplyId);
                stockRecord.setTime(new Timestamp(System.currentTimeMillis()));
                stockRecord.setAmount(amount);
                stockRecord.setAvailableAmount(amount);
                stockRecordDAO.add(accountBook, new StockRecord[]{stockRecord});
            }
            //找到一条记录，则可以合并
            else if(stockRecordSource.length==1){
                StockRecord stockRecord = new StockRecord();
                stockRecord.setUnit(unit);
                stockRecord.setUnitAmount(unitAmount);
                stockRecord.setRelatedOrderNo(transferStock.getRelatedOrderNo());
                stockRecord.setWarehouseId(warehouseId[0]);
                stockRecord.setBatchNo(batchNo);
                stockRecord.setInventoryDate(transferStock.getInventoryDate());
                stockRecord.setStorageLocationId(sourceStorageLocationId);
                stockRecord.setSupplyId(supplyId);
                stockRecord.setTime(new Timestamp(System.currentTimeMillis()));
                stockRecord.setAmount(stockRecordSource[0].getAmount().add(amount));
                stockRecord.setAvailableAmount(stockRecordSource[0].getAmount().add(amount));
                stockRecordDAO.add(accountBook, new StockRecord[]{stockRecord});
            }
            else
            {
                throw new WMSServiceException("查询库存记录出现问题，请检查输入条件!");
            }
        }
        //如果为移出
        else
        {
            if (stockRecordSource.length==0)
            {
                throw new WMSServiceException("无相关库存记录，无法移出!");
            }
            //首先找到最久的库存记录
            for(int i=0;i<stockRecordSource.length;i++)
            {
                for(int j=i+1;i<stockRecordSource.length;j++)
                {
                    if(stockRecordSource[i].getInventoryDate().compareTo(stockRecordSource[j].getInventoryDate())>=0)
                    {
                        StockRecordView temp=new StockRecordView();
                        stockRecordSource[i]=stockRecordSource[j];
                        stockRecordSource[j]=temp;
                    }
                }
            }
            //排序之后最后一条为最久的
            BigDecimal amountAvailableAll= BigDecimal.ZERO;
            int iNeed=-1;
            for(int i=stockRecordSource.length-1;i>=0;i--){
                 amountAvailableAll=amountAvailableAll.add(stockRecordSource[i].getAvailableAmount());
                 //如果加到某个记录够移出数量 则跳出并记录下i
                 if(amountAvailableAll.add(transferStock.getAmount()).compareTo(BigDecimal.ZERO)>=0){
                     iNeed=i;
                     break;}
            }
            if(iNeed==-1){ throw new WMSServiceException("可用数量不足，无法进行移出操作!"); }
            for(int i=stockRecordSource.length-1;i>=iNeed;i--){
                if(i>iNeed)
                {
                    StockRecord stockRecord = new StockRecord();
                    stockRecord.setUnit(unit);
                    stockRecord.setUnitAmount(unitAmount);
                    stockRecord.setRelatedOrderNo(transferStock.getRelatedOrderNo());
                    stockRecord.setWarehouseId(warehouseId[0]);
                    stockRecord.setBatchNo(stockRecordSource[i].getBatchNo());
                    stockRecord.setInventoryDate(stockRecordSource[i].getInventoryDate());
                    stockRecord.setStorageLocationId(sourceStorageLocationId);
                    stockRecord.setSupplyId(supplyId);
                    stockRecord.setTime(new Timestamp(System.currentTimeMillis()));
                    stockRecord.setAmount(stockRecordSource[i].getAmount().subtract(stockRecordSource[i].getAvailableAmount()));
                    stockRecord.setAvailableAmount(new BigDecimal(0));
                    stockRecordDAO.add(accountBook, new StockRecord[]{stockRecord});
                }
                else{
                    StockRecord stockRecord = new StockRecord();
                    stockRecord.setUnit(unit);
                    stockRecord.setUnitAmount(unitAmount);
                    stockRecord.setRelatedOrderNo(transferStock.getRelatedOrderNo());
                    stockRecord.setWarehouseId(warehouseId[0]);
                    stockRecord.setBatchNo(stockRecordSource[i].getBatchNo());
                    stockRecord.setInventoryDate(stockRecordSource[i].getInventoryDate());
                    stockRecord.setStorageLocationId(sourceStorageLocationId);
                    stockRecord.setSupplyId(supplyId);
                    stockRecord.setTime(new Timestamp(System.currentTimeMillis()));
                    stockRecord.setAmount(stockRecordSource[i].getAmount().subtract(amountAvailableAll.add(transferStock.getAmount())));
                    stockRecord.setAvailableAmount(stockRecordSource[i].getAvailableAmount().subtract(amountAvailableAll.add(transferStock.getAmount())));
                    stockRecordDAO.add(accountBook, new StockRecord[]{stockRecord});
                }
            }
        }
    }

    @Override
     public  StockRecordView[] find(String accountBook, StockRecordFind stockRecordFind)
    {
        Condition condition=new Condition();
         List<StockRecordView> stockRecordViews=new ArrayList<StockRecordView>();
        StockRecordView[] stockRecordViews2=null;
        if(stockRecordFind.getTimeEnd()!=null) {
            condition.addCondition("time", new Timestamp[]{stockRecordFind.getTimeEnd()}, ConditionItem.Relation.LESS_THAN);
            if(stockRecordFind.getTimeStart()!=null){
                condition.addCondition("time",new Timestamp[]{stockRecordFind.getTimeStart()},ConditionItem.Relation.GREATER_THAN);}
        }
        condition.addOrder("time", OrderItem.Order.DESC);
        if(stockRecordFind.getSupplyId()!=null)
        { condition.addCondition("supplyId",new Integer[]{stockRecordFind.getSupplyId()});}

        if(stockRecordFind.getStorageLocationId()!=null)
        {condition.addCondition("storageLocationId",new Integer[]{stockRecordFind.getStorageLocationId()});}

        if(stockRecordFind.getWarehouseId()!=null)
        {condition.addCondition("warehouseId",new Integer[]{stockRecordFind.getWarehouseId()});}

        if(stockRecordFind.getUnit()!=null)
        {condition.addCondition("unit",new String[]{stockRecordFind.getUnit()});}

        if(stockRecordFind.getUnitAmount()!=null)
        {condition.addCondition("unitAmount",new BigDecimal[]{stockRecordFind.getUnitAmount()});}

        //同一天批号相同，所以用批号查询？
        if(stockRecordFind.getInventaryDate()!=null) {
            condition.addCondition("batchNo",new String[]{this.batchTransfer(stockRecordFind.getInventaryDate())}); }

        if(stockRecordDAO.find(accountBook, condition).length == 0)
        {throw new WMSServiceException("此条件下无库存记录！");}

        if(stockRecordFind.getReturnMode().equals("newest")) {
            while (stockRecordDAO.find(accountBook, condition).length > 0) {
                StockRecordView[] stockRecordTemp = stockRecordDAO.find(accountBook, condition);
                stockRecordViews.add(stockRecordTemp[0]);
                if (stockRecordFind.getStorageLocationId() != null) {
                    condition.addCondition("storageLocationId", new Integer[]{stockRecordFind.getStorageLocationId()}, ConditionItem.Relation.NOT_EQUAL);
                }
                if (stockRecordFind.getSupplyId() != null) {
                    condition.addCondition("supplyId", new Integer[]{stockRecordFind.getSupplyId()}, ConditionItem.Relation.NOT_EQUAL);
                }
                if (stockRecordFind.getWarehouseId() != null) {
                    condition.addCondition("warehouseId", new Integer[]{stockRecordFind.getWarehouseId()}, ConditionItem.Relation.NOT_EQUAL);
                }
                if (stockRecordFind.getUnit() != null) {
                    condition.addCondition("unit", new String[]{stockRecordFind.getUnit()}, ConditionItem.Relation.NOT_EQUAL);
                }
                if (stockRecordFind.getUnitAmount() != null) {
                    condition.addCondition("unitAmount", new BigDecimal[]{stockRecordFind.getUnitAmount()}, ConditionItem.Relation.NOT_EQUAL);
                }
                if(stockRecordFind.getInventaryDate()!=null) {
                    condition.addCondition("batchNo",new String[]{this.batchTransfer(stockRecordFind.getInventaryDate())},ConditionItem.Relation.NOT_EQUAL ); }
            }
            StockRecordView[] stockRecordViews1 = new StockRecordView[stockRecordViews.size()];
            stockRecordViews2 = stockRecordViews.toArray(stockRecordViews1);
        }
        else
        {
            stockRecordViews2=stockRecordDAO.find(accountBook, condition);
        }
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
    public void RealTransferStockUnitFlexible(String accountBook,TransferStock transferStock)
    {
        new Validator("相关单号").notEmpty().notnull().validate(transferStock.relatedOrderNo);
        new Validator("单位数量").notnull().min(0).validate(transferStock.getUnitAmount());
        new Validator("单位").notnull().notEmpty().validate(transferStock.getUnit());
        new Validator("数量").notnull().validate(transferStock.getAmount());
        new Validator("新单位").notnull().notEmpty().validate(transferStock.getNewUnit());
        new Validator("新单位数量").notnull().min(0).validate(transferStock.getNewUnitAmount());

        int sourceStorageLocationId=transferStock.getSourceStorageLocationId();
        int supplyId=transferStock.getSupplyId();
        int newStorageLocationId=transferStock.getNewStorageLocationId();
        BigDecimal amount=transferStock.getAmount();
        String unit=transferStock.getUnit();
        String newUnit=transferStock.getNewUnit();
        BigDecimal newUnitAmount=transferStock.getNewUnitAmount();
        BigDecimal unitAmount=transferStock.getUnitAmount();
        String relatedOrderNo=transferStock.getRelatedOrderNo();
        idChecker.check(StorageLocationService.class,accountBook,newStorageLocationId,"库位");

        //先查出最新源库存记录和新库位
        StockRecordFind stockRecordFind=new StockRecordFind();
        stockRecordFind.setSupplyId(supplyId);
        stockRecordFind.setStorageLocationId(sourceStorageLocationId);
        stockRecordFind.setUnit(unit);
        stockRecordFind.setUnitAmount(unitAmount);
        StockRecordView[]   stockRecordSource1=this.find(accountBook,stockRecordFind);
        if(stockRecordSource1.length==0)
        {
            throw new WMSServiceException("没查到符合要求的源库存记录，请检查相关信息！");
        }

        //进行排序
        for(int i=0;i<stockRecordSource1.length;i++)
        {
            for(int j=i+1;i<stockRecordSource1.length;j++)
            {
                if(stockRecordSource1[i].getInventoryDate().compareTo(stockRecordSource1[j].getInventoryDate())>=0)
                {
                    StockRecordView temp=new StockRecordView();
                    stockRecordSource1[i]=stockRecordSource1[j];
                    stockRecordSource1[j]=temp;
                }
            }
        }
        //排序之后最后一条为最久的
        BigDecimal amountAvailableAll=BigDecimal.ZERO;
        int iNeed=-1;
        for(int i=stockRecordSource1.length-1;i>=0;i--){
            amountAvailableAll=amountAvailableAll.add(stockRecordSource1[i].getAvailableAmount());
            //如果加到某个记录够移出数量 则跳出并记录下i
            if(amountAvailableAll.add(transferStock.getAmount()).compareTo(BigDecimal.ZERO)>=0){
                iNeed=i;
                break;}
        }
        if(iNeed==-1){ throw new WMSServiceException("可用数量不足，无法进行移出操作!"); }

        for(int i=stockRecordSource1.length-1;i>=iNeed;i--){
            StockRecord stockRecordNewSave=new StockRecord();
            if(i>iNeed)
            {
                StockRecord stockRecord = new StockRecord();
                stockRecord.setUnit(unit);
                stockRecord.setUnitAmount(unitAmount);
                stockRecord.setRelatedOrderNo(transferStock.getRelatedOrderNo());
                stockRecord.setWarehouseId(stockRecordSource1[i].getWarehouseId());
                stockRecord.setBatchNo(stockRecordSource1[i].getBatchNo());
                stockRecord.setInventoryDate(stockRecordSource1[i].getInventoryDate());
                stockRecord.setStorageLocationId(sourceStorageLocationId);
                stockRecord.setSupplyId(supplyId);
                stockRecord.setTime(new Timestamp(System.currentTimeMillis()));
                stockRecord.setAmount(stockRecordSource1[i].getAmount().subtract(stockRecordSource1[i].getAvailableAmount()));
                stockRecord.setAvailableAmount(BigDecimal.ZERO);
                stockRecordDAO.add(accountBook, new StockRecord[]{stockRecord});

                //查到新库位上最新的相同供货的记录
                StockRecordView[] stockRecordViews=stockRecordDAO.find(accountBook, new Condition().addCondition( "storageLocationId",new Integer[]{newStorageLocationId}).
                        addCondition("supplyId",new Integer[]{supplyId}).addCondition("unit",new String[]{newUnit}).addCondition("unitAmount",new BigDecimal[]{newUnitAmount}).
                        addCondition("batchNo",new String[]{stockRecordSource1[i].getBatchNo()}).addOrder("time",OrderItem.Order.DESC)
                );

                //已经找到最新的可以叠加的记录，则第二条为叠加
                if(stockRecordViews.length>0){
                    stockRecordNewSave.setAmount(stockRecordViews[0].getAmount().add(stockRecordSource1[i].getAvailableAmount()));
                    stockRecordNewSave.setAvailableAmount(stockRecordViews[0].getAvailableAmount().add(stockRecordSource1[i].getAvailableAmount()));
                    stockRecordNewSave.setUnitAmount(newUnitAmount);
                    stockRecordNewSave.setUnit(newUnit);
                    stockRecordNewSave.setRelatedOrderNo(relatedOrderNo);
                    stockRecordNewSave.setExpiryDate(stockRecordSource1[i].getExpiryDate());
                    stockRecordNewSave.setInventoryDate(stockRecordSource1[i].getInventoryDate());
                    stockRecordNewSave.setManufactureDate(stockRecordSource1[i].getManufactureDate());
                    stockRecordNewSave.setStorageLocationId(newStorageLocationId);
                    stockRecordNewSave.setWarehouseId(stockRecordSource1[i].getWarehouseId());
                    stockRecordNewSave.setSupplyId(supplyId);
                    stockRecordNewSave.setBatchNo(stockRecordSource1[i].getBatchNo());
                    stockRecordNewSave.setTime(new Timestamp(System.currentTimeMillis()));
                }
                else
                {
                    stockRecordNewSave.setAmount(amount);
                    stockRecordNewSave.setAvailableAmount(amount);
                    stockRecordNewSave.setUnit(newUnit);
                    stockRecordNewSave.setUnitAmount(newUnitAmount);
                    stockRecordNewSave.setRelatedOrderNo(relatedOrderNo);
                    stockRecordNewSave.setExpiryDate(stockRecordSource1[i].getExpiryDate());
                    stockRecordNewSave.setInventoryDate(stockRecordSource1[i].getInventoryDate());
                    stockRecordNewSave.setManufactureDate(stockRecordSource1[i].getManufactureDate());
                    stockRecordNewSave.setStorageLocationId(newStorageLocationId);
                    stockRecordNewSave.setWarehouseId(stockRecordSource1[i].getWarehouseId());
                    stockRecordNewSave.setSupplyId(supplyId);
                    stockRecordNewSave.setBatchNo(stockRecordSource1[i].getBatchNo());
                    stockRecordNewSave.setTime(new Timestamp(System.currentTimeMillis()));
                }
            }
            else if(i==iNeed){
                StockRecord stockRecord = new StockRecord();
                stockRecord.setUnit(unit);
                stockRecord.setUnitAmount(unitAmount);
                stockRecord.setRelatedOrderNo(transferStock.getRelatedOrderNo());
                stockRecord.setWarehouseId(stockRecordSource1[i].getWarehouseId());
                stockRecord.setBatchNo(stockRecordSource1[i].getBatchNo());
                stockRecord.setInventoryDate(stockRecordSource1[i].getInventoryDate());
                stockRecord.setStorageLocationId(sourceStorageLocationId);
                stockRecord.setSupplyId(supplyId);
                stockRecord.setTime(new Timestamp(System.currentTimeMillis()));
                stockRecord.setAmount(stockRecordSource1[i].getAmount().subtract(amountAvailableAll.subtract(transferStock.getAmount())));
                stockRecord.setAvailableAmount(stockRecordSource1[i].getAvailableAmount().subtract(amountAvailableAll.subtract(transferStock.getAmount())));
                stockRecordDAO.add(accountBook, new StockRecord[]{stockRecord});

                //查到新库位上最新的相同供货的记录
                StockRecordView[] stockRecordViews=stockRecordDAO.find(accountBook, new Condition().addCondition( "storageLocationId",new Integer[]{newStorageLocationId}).
                        addCondition("supplyId",new Integer[]{supplyId}).addCondition("unit",new String[]{newUnit}).addCondition("unitAmount",new BigDecimal[]{newUnitAmount}).
                        addCondition("batchNo",new String[]{stockRecordSource1[i].getBatchNo()}).addOrder("time",OrderItem.Order.DESC)
                );
                //已经找到最新的可以叠加的记录，则第二条为叠加
                if(stockRecordViews.length>0){
                    stockRecordNewSave.setAmount(stockRecordViews[0].getAmount().add(amountAvailableAll.subtract(transferStock.getAmount())));
                    stockRecordNewSave.setAvailableAmount(stockRecordViews[0].getAvailableAmount().add(amountAvailableAll.subtract(transferStock.getAmount())));
                    stockRecordNewSave.setRelatedOrderNo(relatedOrderNo);
                    stockRecordNewSave.setUnit(newUnit);
                    stockRecordNewSave.setUnitAmount(newUnitAmount);
                    stockRecordNewSave.setExpiryDate(stockRecordSource1[i].getExpiryDate());
                    stockRecordNewSave.setInventoryDate(stockRecordSource1[i].getInventoryDate());
                    stockRecordNewSave.setManufactureDate(stockRecordSource1[i].getManufactureDate());
                    stockRecordNewSave.setStorageLocationId(newStorageLocationId);
                    stockRecordNewSave.setWarehouseId(stockRecordSource1[i].getWarehouseId());
                    stockRecordNewSave.setSupplyId(supplyId);
                    stockRecordNewSave.setBatchNo(stockRecordSource1[i].getBatchNo());
                    stockRecordNewSave.setTime(new Timestamp(System.currentTimeMillis()));
                }
                else
                {
                    stockRecordNewSave.setAmount(amountAvailableAll.subtract(transferStock.getAmount()));
                    stockRecordNewSave.setAvailableAmount(amountAvailableAll.subtract(transferStock.getAmount()));
                    stockRecordNewSave.setUnit(newUnit);
                    stockRecordNewSave.setUnitAmount(newUnitAmount);
                    stockRecordNewSave.setRelatedOrderNo(relatedOrderNo);
                    stockRecordNewSave.setExpiryDate(stockRecordSource1[i].getExpiryDate());
                    stockRecordNewSave.setInventoryDate(stockRecordSource1[i].getInventoryDate());
                    stockRecordNewSave.setManufactureDate(stockRecordSource1[i].getManufactureDate());
                    stockRecordNewSave.setStorageLocationId(newStorageLocationId);
                    stockRecordNewSave.setWarehouseId(stockRecordSource1[i].getWarehouseId());
                    stockRecordNewSave.setSupplyId(supplyId);
                    stockRecordNewSave.setBatchNo(stockRecordSource1[i].getBatchNo());
                    stockRecordNewSave.setTime(new Timestamp(System.currentTimeMillis()));
                }

                int[] newStockRecordId =stockRecordDAO.add(accountBook,new StockRecord[]{stockRecordNewSave});
                if(newStockRecordId.length!=1)
                {
                    throw new WMSServiceException("添加新库存记录失败！");
                }
                //添加一条移位记录
                TransferRecord transferRecord=new TransferRecord();
                transferRecord.setNewStockRecordId(newStockRecordId[0]);
                transferRecord.setSourceStockRecordId(stockRecordSource1[i].getStorageLocationId());
                transferRecord.setWarehouseId(stockRecordSource1[0].getWarehouseId());
                transformRecordService.add(accountBook,new TransferRecord[]{transferRecord});
            }
        }
    }

    private String batchTransfer(Timestamp timestamp)
    {
        Timestamp timestamp1 =timestamp;
        SimpleDateFormat format =  new SimpleDateFormat("yyyyMMdd");
        String batchNo=format.format(timestamp1);
        return batchNo;
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
                .addCondition("batchNo",new String[]{this.batchTransfer(transferStock.getInventoryDate())}).addOrder("time",OrderItem.Order.DESC ));
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
/*
   public void RealTransferStockUnitFlexible(String accountBook, TransferStock transferStock) {
       new Validator("相关单号").notEmpty().notnull().validate(transferStock.relatedOrderNo);
       new Validator("单位数量").notnull().min(0).validate(transferStock.getUnitAmount());
       new Validator("单位").notnull().notEmpty().validate(transferStock.getUnit());
       new Validator("数量").notnull().validate(transferStock.getAmount());
       new Validator("新单位").notnull().validate(transferStock.getNewUnit());
       new Validator("新单位数量").notnull().notEmpty().validate(transferStock.getNewUnitAmount());

       int sourceStorageLocationId = transferStock.getSourceStorageLocationId();
       int supplyId = transferStock.getSupplyId();
       int newStorageLocationId = transferStock.getNewStorageLocationId();
       BigDecimal amount = transferStock.getAmount();
       String unit = transferStock.getUnit();
       BigDecimal unitAmount = transferStock.getUnitAmount();
       String relatedOrderNo = transferStock.getRelatedOrderNo();

       //先查出最新源库存记录和新库位
       StockRecordView[] stockRecordSource1 = stockRecordDAO.find(accountBook, new Condition().addCondition("supplyId", new Integer[]{supplyId}).
               addCondition("storageLocationId", new Integer[]{sourceStorageLocationId}).addCondition("unit", unit).addCondition("unitAmount", unitAmount).
               addCondition("inventoryDate", new String[]{transferStock.getBatchNo()}));
       StockRecordFind stockRecordFind = new StockRecordFind();
       stockRecordFind.setSupplyId(supplyId);
       stockRecordFind.setStorageLocationId(sourceStorageLocationId);
       stockRecordFind.setUnit(unit);
       stockRecordFind.setUnitAmount(unitAmount);
       stockRecordSource1 = this.find(accountBook, stockRecordFind);
       if (stockRecordSource1.length == 0) {
           throw new WMSServiceException("没查到符合要求的源库存记录，请检查相关信息！");
       }


       StockRecordView stockRecordBatchNoOldest = stockRecordSource1[0];
       Timestamp newestTime1 = stockRecordBatchNoOldest.getInventoryDate();
       for (int i = 1; i < stockRecordSource1.length; i++) {
           if (newestTime1.compareTo(stockRecordSource1[i].getTime()) == 1) {
               newestTime1 = stockRecordSource1[i].getTime();
               stockRecordBatchNoOldest = stockRecordSource1[i];
           }
       }


       if (stockRecordSource1.length == 0) {
           throw new WMSServiceException("没查到符合要求的源库存记录，请检查相关信息！");
       }

       StockRecordView[] stockRecordSource = new StockRecordView[]{stockRecordBatchNoOldest};

       if (stockRecordSource[0].getAvailableAmount().compareTo(amount) == -1) {
           throw new WMSServiceException("移动的数量不能大于可用数量！");
       }

       StorageLocationView[] storageLocationNew = storageLocationService.find(accountBook, new Condition().addCondition("id", new Integer[]{newStorageLocationId}));

       if (storageLocationNew.length != 1) {
           throw new WMSServiceException("请检查输入的新库位id，没有找到相关记录");
       }
       int sourceStockRecordId = stockRecordSource[0].getId();

       //新建两条库存记录
       StockRecord stockRecordSourceSave = new StockRecord();
       StockRecord stockRecordNewSave = new StockRecord();
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

       //查到新库位上最新的相同供货的记录
       StockRecordView[] stockRecordViews = stockRecordDAO.find(accountBook, new Condition().addCondition("storageLocationId", new Integer[]{newStorageLocationId}).
               addCondition("supplyId", new Integer[]{supplyId}).addCondition("unit", new String[]{transferStock.getNewUnit()}).addCondition("unitAmount", new BigDecimal[]{transferStock.getNewUnitAmount()}).
               addCondition("batchNo", new String[]{stockRecordBatchNoOldest.getBatchNo()})
       );

       StockRecordView stockRecordNewest = null;
       if (stockRecordViews.length > 0) {
           stockRecordNewest = stockRecordViews[0];
           Timestamp newestTime = stockRecordNewest.getTime();
           for (int i = 1; i < stockRecordViews.length; i++) {
               if (newestTime.compareTo(stockRecordViews[i].getTime()) == -1) {
                   newestTime = stockRecordViews[i].getTime();
                   stockRecordNewest = stockRecordViews[i];
               }
           }

           //增加第二条库存记录 需要改变单位和单位数量
           //已经找到最新的可以叠加的记录，则第二条为叠加
           stockRecordNewSave.setAmount(amount.add(stockRecordNewest.getAmount()));
           stockRecordNewSave.setAvailableAmount(stockRecordSource[0].getAvailableAmount().add(stockRecordNewest.getAmount()));
           stockRecordNewSave.setUnit(stockRecordNewest.getUnit());
           stockRecordNewSave.setUnitAmount(unitAmount);
           stockRecordNewSave.setRelatedOrderNo(relatedOrderNo);
           stockRecordNewSave.setExpiryDate(stockRecordSource[0].getExpiryDate());
           stockRecordNewSave.setInventoryDate(stockRecordNewest.getInventoryDate());
           stockRecordNewSave.setManufactureDate(stockRecordSource[0].getManufactureDate());
           stockRecordNewSave.setStorageLocationId(newStorageLocationId);
           stockRecordNewSave.setWarehouseId(stockRecordSource[0].getWarehouseId());
           stockRecordNewSave.setSupplyId(supplyId);
           stockRecordNewSave.setBatchNo(stockRecordSource[0].getBatchNo());
           stockRecordNewSave.setTime(new Timestamp(System.currentTimeMillis()));
       }
       //如果没有数量直接为新数量
       else {
           stockRecordNewSave.setAmount(amount);
           stockRecordNewSave.setAvailableAmount(amount);
           stockRecordNewSave.setUnit(stockRecordNewest.getUnit());
           stockRecordNewSave.setUnitAmount(unitAmount);
           stockRecordNewSave.setRelatedOrderNo(relatedOrderNo);
           stockRecordNewSave.setExpiryDate(stockRecordSource[0].getExpiryDate());
           stockRecordNewSave.setInventoryDate(stockRecordNewest.getInventoryDate());
           stockRecordNewSave.setManufactureDate(stockRecordSource[0].getManufactureDate());
           stockRecordNewSave.setStorageLocationId(newStorageLocationId);
           stockRecordNewSave.setWarehouseId(stockRecordSource[0].getWarehouseId());
           stockRecordNewSave.setSupplyId(supplyId);
           stockRecordNewSave.setBatchNo(stockRecordSource[0].getBatchNo());
           stockRecordNewSave.setTime(new Timestamp(System.currentTimeMillis()));
       }

       stockRecordDAO.add(accountBook, new StockRecord[]{stockRecordSourceSave});
       int[] newStockRecordId = stockRecordDAO.add(accountBook, new StockRecord[]{stockRecordNewSave});
       if (newStockRecordId.length != 1) {
           throw new WMSServiceException("添加新库存记录失败！");
       }
       //添加一条移位记录
       TransferRecord transferRecord = new TransferRecord();
       transferRecord.setNewStockRecordId(newStockRecordId[0]);
       transferRecord.setSourceStockRecordId(sourceStockRecordId);
       transferRecord.setWarehouseId(stockRecordSource[0].getWarehouseId());
       transformRecordService.add(accountBook, new TransferRecord[]{transferRecord});
   }
   */
}
