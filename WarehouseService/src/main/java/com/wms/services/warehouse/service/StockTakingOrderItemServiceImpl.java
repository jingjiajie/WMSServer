package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.StockTakingOrderItemDAO;
import com.wms.utilities.IDChecker;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.StockRecord;
import com.wms.utilities.model.StockRecordView;
import com.wms.utilities.model.StockTakingOrderItem;
import com.wms.utilities.model.StockTakingOrderItemView;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wms.services.warehouse.datastructures.StockTakingOrderItemAdd;
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

        //TODO
        StockRecordView[] stockRecord=stockRecordService.find(accountBook,new Condition().addCondition("supplyId",new Integer[]{stockTakingOrderItemAdd.getSupplyId()}).
                addCondition("warehouseId",new Integer[]{stockTakingOrderItemAdd.getWarehouseId()}));

        if(stockRecord.length==0)
        {
            throw new WMSServiceException("没有查到此供货的任何库存信息");
        }
        //添加所有信息条目
        if (mode==0){


        }
        //只添加总数量条目
        if(mode==1){


        }
    }



}
