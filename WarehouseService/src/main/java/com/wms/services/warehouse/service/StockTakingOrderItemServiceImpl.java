package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.StockTakingOrderItemDAO;
import com.wms.utilities.IDChecker;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wms.services.warehouse.datastructures.StockTakingOrderItemAdd;

import java.math.BigDecimal;

@Service
@Transactional
public class StockTakingOrderItemServiceImpl implements StockTakingOrderItemService{

    @Autowired
    StockTakingOrderItemDAO stockTakingOrderItemDAO;
    @Autowired
    StockTakingOrderService stockTakingOrderService;
    @Autowired
    IDChecker idChecker;
    @Autowired
    StockRecordService stockRecordService;
    @Autowired
    StockTakingOrderItemService stockTakingOrderItemService;
    @Autowired
    StorageLocationService storageLocationService;
    @Autowired
    StorageAreaService storageAreaService;
    @Override
    public int[] add(String accountBook, StockTakingOrderItem[] stockTakingOrderItems) throws WMSServiceException {

        for (int i=0;i<stockTakingOrderItems.length;i++) {
            new Validator("单位").notnull().notEmpty().validate(stockTakingOrderItems[i].getUnit());
            new Validator("单位数量").notnull().validate(stockTakingOrderItems[i].getUnitAmount());
        }
        for (int i=0;i<stockTakingOrderItems.length;i++) {
          idChecker.check(StockTakingOrderService.class,accountBook,stockTakingOrderItems[i].getStockTakingOrderId(),"盘点单");
          idChecker.check(PersonService.class,accountBook,stockTakingOrderItems[i].getPersonId(),"盘点人");
          idChecker.check(SupplyService.class,accountBook,stockTakingOrderItems[i].getSupplyId(),"供货信息");
          idChecker.check(StorageLocationService.class,accountBook,stockTakingOrderItems[i].getStorageLocationId(),"库位");
          }
        return stockTakingOrderItemDAO.add(accountBook,stockTakingOrderItems);
    }

    @Override
    public  void update(String accountBook,StockTakingOrderItem[] stockTakingOrderItems) throws WMSServiceException {
        for (int i=0;i<stockTakingOrderItems.length;i++) {
            new Validator("单位").notnull().notEmpty().validate(stockTakingOrderItems[i].getUnit());
            new Validator("单位数量").notnull().validate(stockTakingOrderItems[i].getUnitAmount());
        }
        for (int i=0;i<stockTakingOrderItems.length;i++) {
            idChecker.check(StockTakingOrderService.class,accountBook,stockTakingOrderItems[i].getStockTakingOrderId(),"盘点单");
            idChecker.check(PersonService.class,accountBook,stockTakingOrderItems[i].getPersonId(),"盘点人");
            idChecker.check(SupplyService.class,accountBook,stockTakingOrderItems[i].getSupplyId(),"供货信息");
            idChecker.check(StorageLocationService.class,accountBook,stockTakingOrderItems[i].getStorageLocationId(),"库位");
        }
        stockTakingOrderItemDAO.update(accountBook,stockTakingOrderItems);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
        try {
            stockTakingOrderItemDAO.remove(accountBook, ids);
        }
        catch (Throwable ex){
            throw new WMSServiceException("删除盘点单条目失败，如果盘点单条目已经被引用，需要先删除引用的内容，才能删除该盘点单条目");
        }
    }

    @Override
    public StockTakingOrderItemView[] find(String accountBook, Condition cond) throws WMSServiceException{
        return this.stockTakingOrderItemDAO.find(accountBook, cond);
    }

    @Override
    public void addStockTakingOrderItemSingle(String accountBook, StockTakingOrderItemAdd stockTakingOrderItemAdd)
    {
        int mode=stockTakingOrderItemAdd.getMode();

        //TODO 只显示最新的记录
        StockRecordView[] stockRecord=stockRecordService.find(accountBook,new Condition().addCondition("supplyId",new Integer[]{stockTakingOrderItemAdd.getSupplyId()}).
                addCondition("warehouseId",new Integer[]{stockTakingOrderItemAdd.getWarehouseId()}));

        if(stockRecord.length==0)
        {
            throw new WMSServiceException("没有查到此供货的任何库存信息");
        }


        //把stockRecord改为分库区的数量
        String storageArea;
        for(int i=0;i<stockRecord.length;i++)
        {
            for(int j=i+1;j<stockRecord.length;j++)
            {
             StorageAreaView[] storageAreaViews= storageAreaService.find(accountBook,new Condition().addCondition("storageLocationId",new Integer[]{stockRecord[i].getStorageLocationId()}));

             if(storageAreaViews.length!=1)
             {
                 throw new WMSServiceException("没有找到相关库位信息");
             }
             storageArea=storageAreaViews[0].getName();
            }
        }


        BigDecimal warehouseAmount=new BigDecimal(0);
        StockTakingOrderItem stockTakingOrderItem=new StockTakingOrderItem();
        stockTakingOrderItem.setStockTakingOrderId(stockTakingOrderItemAdd.getStockTakingOrderId());
        stockTakingOrderItem.setPersonId(stockTakingOrderItemAdd.getPersonId());
        stockTakingOrderItem.setSupplyId(stockTakingOrderItemAdd.getSupplyId());
            for(int i=0;i<stockRecord.length;i++)
            {
                //分不同单位单位数量添加所有库位信息条目
                if (mode==0) {
                    //已经建好盘点单
                    stockTakingOrderItem.setComment("各个库区详细信息");
                    stockTakingOrderItem.setStorageLocationId(stockRecord[i].getStorageLocationId());
                    stockTakingOrderItem.setUnitAmount(stockRecord[i].getUnitAmount());
                    stockTakingOrderItem.setUnit(stockRecord[i].getUnit());
                    stockTakingOrderItem.setAmount(stockRecord[i].getAmount());
                    stockTakingOrderItemService.add(accountBook, new StockTakingOrderItem[]{stockTakingOrderItem});
                }
                //得到同一供货、同一仓库内的数量总和
                warehouseAmount=warehouseAmount.add(stockRecord[i].getAmount());
            }
            //添加总数量条目
        stockTakingOrderItem.setComment("总数");
        stockTakingOrderItem.setUnit("无");
        Integer integerNull=null;
        BigDecimal bigDecimalNull=null;
        stockTakingOrderItem.setStorageLocationId(integerNull);
        stockTakingOrderItem.setUnitAmount(bigDecimalNull);
        stockTakingOrderItem.setAmount(warehouseAmount);
        stockTakingOrderItemService.add(accountBook, new StockTakingOrderItem[]{stockTakingOrderItem});



    }



}
