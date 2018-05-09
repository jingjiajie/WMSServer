package com.wms.services.warehouse.service;


import com.sun.xml.internal.bind.v2.TODO;
import com.wms.services.warehouse.dao.StockTakingOrderItemDAO;
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
import com.wms.services.warehouse.datastructures.StockTakingOrderItemAdd;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
    StorageLocationService storageLocationService;
    @Autowired
    StorageAreaService storageAreaService;
    @Autowired
    DeliveryOrderItemService deliveryOrderItemService;
    @Override
    public int[] add(String accountBook, StockTakingOrderItem[] stockTakingOrderItems) throws WMSServiceException {

        for (int i=0;i<stockTakingOrderItems.length;i++) {
            new Validator("单位").notnull().notEmpty().validate(stockTakingOrderItems[i].getUnit());
            new Validator("单位数量").notnull().validate(stockTakingOrderItems[i].getUnitAmount());
        }
        for (int i=0;i<stockTakingOrderItems.length;i++) {
          idChecker.check(StockTakingOrderService.class,accountBook,stockTakingOrderItems[i].getStockTakingOrderId(),"盘点单");
          // idChecker.check(PersonService.class,accountBook,stockTakingOrderItems[i].getPersonId(),"盘点人");TODO
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
            //idChecker.check(PersonService.class,accountBook,stockTakingOrderItems[i].getPersonId(),"盘点人");
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
        new Validator("人员").notnull().validate(stockTakingOrderItemAdd.getPersonId());
        idChecker.check(StockTakingOrder.class,accountBook,stockTakingOrderItemAdd.getStockTakingOrderId(),"盘点单");
        idChecker.check(Supply.class,accountBook,stockTakingOrderItemAdd.getSupplyId(),"供货信息");
        idChecker.check(Warehouse.class,accountBook,stockTakingOrderItemAdd.getWarehouseId()," 仓库");
        int mode=stockTakingOrderItemAdd.getMode();
        Condition condition=new Condition();
        condition.addCondition("supplyId",new Integer[]{stockTakingOrderItemAdd.getSupplyId()}).
                addCondition("warehouseId",new Integer[]{stockTakingOrderItemAdd.getWarehouseId()}).
                addCondition("time",new Timestamp[]{stockTakingOrderItemAdd.getCheckTime()}, ConditionItem.Relation.LESS_THAN).
                addOrder("time", OrderItem.Order.DESC);
        if(stockRecordService.find(accountBook,condition).length==0)
        {
            throw new WMSServiceException("没有查到此供货的任何库存信息");
        }

        //第一条肯定是某个记录的最新一条
        BigDecimal warehouseAmount=new BigDecimal(0);
        while(stockRecordService.find(accountBook,condition).length>0) {
            StockRecordView[] stockRecordTemp = stockRecordService.find(accountBook,condition);
            //如果为所有信息模式，将此信息作为一条盘点单条目保存
            if (mode==0) {
                StockTakingOrderItem stockTakingOrderItem = new StockTakingOrderItem();
                stockTakingOrderItem.setStockTakingOrderId(stockTakingOrderItemAdd.getStockTakingOrderId());
                stockTakingOrderItem.setPersonId(stockTakingOrderItemAdd.getPersonId());
                stockTakingOrderItem.setSupplyId(stockTakingOrderItemAdd.getSupplyId());
                //已经建好盘点单
                stockTakingOrderItem.setComment("库位分单位详细信息");
                stockTakingOrderItem.setStorageLocationId(stockRecordTemp[0].getStorageLocationId());
                stockTakingOrderItem.setUnitAmount(stockRecordTemp[0].getUnitAmount());
                stockTakingOrderItem.setUnit(stockRecordTemp[0].getUnit());
                stockTakingOrderItem.setAmount(stockRecordTemp[0].getAmount());
                stockTakingOrderItem.setRealAmount(stockRecordTemp[0].getAmount());
                //分不同单位单位数量添加所有库位信息条
                stockTakingOrderItemDAO.add(accountBook,new StockTakingOrderItem[]{stockTakingOrderItem});
            }
            //相同单位、单位数量、批号的第一条已经取出，所有增加条件，下次查询没有与刚才相同的记录了
           condition= condition.addCondition("unitAmount",new BigDecimal[]{stockRecordTemp[0].getUnitAmount()}, ConditionItem.Relation.NOT_EQUAL)
                    .addCondition("unit",new java.lang.String[]{stockRecordTemp[0].getUnit()}, ConditionItem.Relation.NOT_EQUAL).addCondition(
                            "batchNo",new java.lang.String[]{stockRecordTemp[0].getBatchNo()}, ConditionItem.Relation.NOT_EQUAL);

            //得到同一供货、同一仓库内的数量总和
            warehouseAmount=warehouseAmount.add(stockRecordTemp[0].getAmount());
        }
        //添加总数量条目
        StockTakingOrderItem stockTakingOrderItem=new StockTakingOrderItem();
        stockTakingOrderItem.setStockTakingOrderId(stockTakingOrderItemAdd.getStockTakingOrderId());
        stockTakingOrderItem.setPersonId(stockTakingOrderItemAdd.getPersonId());
        stockTakingOrderItem.setSupplyId(stockTakingOrderItemAdd.getSupplyId());
        stockTakingOrderItem.setComment("仓库总数");
        stockTakingOrderItem.setUnit("个");
        stockTakingOrderItem.setStorageLocationId(null);
        stockTakingOrderItem.setUnitAmount(new BigDecimal(1));
        stockTakingOrderItem.setAmount(warehouseAmount);
        stockTakingOrderItem.setRealAmount(warehouseAmount);
        stockTakingOrderItemDAO.add(accountBook, new StockTakingOrderItem[]{stockTakingOrderItem});
        //在途数量统计

    }



}
