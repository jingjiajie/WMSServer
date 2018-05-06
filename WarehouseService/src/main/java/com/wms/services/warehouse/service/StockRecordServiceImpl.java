package com.wms.services.warehouse.service;

import com.sun.xml.internal.bind.v2.TODO;
import com.wms.services.warehouse.dao.StockRecordDAO;
import com.wms.services.warehouse.datastructures.TransferStock;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.StockRecord;
import com.wms.utilities.model.StockRecordView;
import com.wms.utilities.model.StorageLocationView;
import com.wms.utilities.model.TransferRecord;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
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
    public void transformStock(String accountBook, TransferStock transferStock)
    {
        new Validator("相关单号").notEmpty().notnull().validate(transferStock.relatedOrderNo);
        if (transferStock.amount==0)
        {
            throw new WMSServiceException("移动的数量不能的0");
        }
        int sourceStockRecordId=transferStock.getSourceStockRecordId();
        int newStockLocationId=transferStock.getNewStockLocationId();
        int amount=transferStock.getAmount();
        String unit=transferStock.getUnit();
        int unitAmount=transferStock.getUnitAmount();
        String relatedOrderNo=transferStock.getRelatedOrderNo();

        //先查出源库存记录和新库位
        StockRecordView[] stockRecordSource= stockRecordDAO.find(accountBook,new Condition().addCondition("id",new int[]{sourceStockRecordId}));
        if(stockRecordSource.length!=1)
        {
            throw new WMSServiceException("请检查输入的源库存id，没有找到相关记录");
        }
        new Validator("数量").min(0).max(stockRecordSource[0].getAmount());
        StorageLocationView[] storageLocationNew= storageLocationService.find(accountBook,new Condition().addCondition("id",new int[]{newStockLocationId}));
        if(storageLocationNew.length!=1)
        {
            throw new WMSServiceException("请检查输入的新库位id，没有找到相关记录");
        }
        //查到新库位上最新的相同供货的记录
        int supplyId=stockRecordSource[0].getSupplyId();
        StockRecordView[] stockRecordViews=stockRecordDAO.find(accountBook, new Condition().addCondition(accountBook, new Condition().addCondition("storageLocationId",new int[]{newStockLocationId})).
                addCondition("supplyId",new int[]{supplyId}).addCondition("unit",new String[]{unit}));//单位数量和相关单号是否需要相同？TODO
        StockRecordView stockRecordNewest= new StockRecordView();
        //新建两条库存记录
        StockRecord stockRecordSourceSave=new StockRecord();
        StockRecord stockRecordNewSave=new StockRecord();
        //第一条和源记录相同 只改变数量
        stockRecordSourceSave.setWarehouseId(stockRecordSource[0].getWarehouseId());
        stockRecordSourceSave.setStorageLocationId(stockRecordSource[0].getStorageLocationId());
        stockRecordSourceSave.setSupplyId(stockRecordSource[0].getSupplyId());
        stockRecordSourceSave.setExpiryDate(stockRecordSource[0].getExpiryDate());
        stockRecordSourceSave.setInventoryDate(stockRecordSource[0].getInventoryDate());
        stockRecordSourceSave.setManufactureDate(stockRecordSource[0].getManufactureDate());
        stockRecordSourceSave.setUnitAmount(stockRecordSource[0].getUnitAmount());
        stockRecordSourceSave.setUnit(stockRecordSource[0].getUnit());
        BigDecimal amountBig = new BigDecimal(amount);
        stockRecordSourceSave.setAmount(stockRecordSource[0].getAmount().subtract(amountBig));
        stockRecordSourceSave.setRelatedOrderNo(stockRecordSource[0].getRelatedOrderNo());
        stockRecordSourceSave.setTime(new Timestamp(System.currentTimeMillis()));

        if(stockRecordViews.length>0) {
            stockRecordNewest = stockRecordViews[0];
            Timestamp newestTime = stockRecordNewest.getTime();
            for (int i = 1; i < stockRecordViews.length; i++) {
                if (newestTime.compareTo(stockRecordViews[i].getTime())==-1) {
                    newestTime = stockRecordViews[i].getTime();
                    stockRecordNewest = stockRecordViews[i];
                }
            }
            //已经找到最新的可以叠加的记录，则第二条为叠加
            //添加一条移位记录
            TransferRecord transferRecord=new TransferRecord();
            transferRecord.setNewStockRecordId(newStockLocationId);
            transferRecord.setSourceStockRecordId(sourceStockRecordId);
            transferRecord.setWarehouseId(stockRecordSource[0].getWarehouseId());
            transformRecordService.add(accountBook,new TransferRecord[]{transferRecord});
            //增加第二条库存记录
            stockRecordNewSave.setAmount(amountBig.add(stockRecordNewest.getAmount()));
            stockRecordNewSave.setUnit(stockRecordNewest.getUnit());
            stockRecordNewSave.setUnitAmount(new BigDecimal(unitAmount));
            stockRecordNewSave.setRelatedOrderNo(relatedOrderNo);
            stockRecordNewSave.setExpiryDate(stockRecordSource[0].getExpiryDate());
            stockRecordNewSave.setInventoryDate(stockRecordSource[0].getInventoryDate());
            stockRecordNewSave.setManufactureDate(stockRecordSource[0].getManufactureDate());
            stockRecordNewSave.setStorageLocationId(newStockLocationId);
            stockRecordNewSave.setWarehouseId(stockRecordSource[0].getWarehouseId());//不变仓库?TODO
            stockRecordNewSave.setSupplyId(supplyId);
            stockRecordNewSave.setTime(new Timestamp(System.currentTimeMillis()));
        }
        //如果没有则直接新建记录
        else
            {
                stockRecordNewSave.setAmount(amountBig);
                stockRecordNewSave.setUnit(unit);
                stockRecordNewSave.setUnitAmount(new BigDecimal(unitAmount));
                stockRecordNewSave.setRelatedOrderNo(relatedOrderNo);
                stockRecordNewSave.setExpiryDate(stockRecordSource[0].getExpiryDate());
                stockRecordNewSave.setInventoryDate(stockRecordSource[0].getInventoryDate());
                stockRecordNewSave.setManufactureDate(stockRecordSource[0].getManufactureDate());
                stockRecordNewSave.setStorageLocationId(newStockLocationId);
                stockRecordNewSave.setWarehouseId(stockRecordSource[0].getWarehouseId());//不变仓库?TODO
                stockRecordNewSave.setSupplyId(supplyId);
                stockRecordNewSave.setTime(new Timestamp(System.currentTimeMillis()));
            }
            stockRecordDAO.add(accountBook,new StockRecord[]{stockRecordNewSave,stockRecordSourceSave});

    }

}
