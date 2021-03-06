package com.wms.services.warehouse.service;
import com.wms.services.ledger.service.PersonService;
import com.wms.services.warehouse.datastructures.StockRecordFind;
import com.wms.services.warehouse.datastructures.StockTakingItemDelete;
import com.wms.services.warehouse.datastructures.StockTakingOrderItemAddSingle;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wms.services.warehouse.dao.StockTakingOrderItemDAO;
import com.wms.utilities.IDChecker;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import com.wms.services.warehouse.datastructures.StockTakingOrderItemAdd;
import com.wms.utilities.model.StockTakingOrderItemView;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.SessionFactory;

@Service
@Transactional
public class StockTakingOrderItemServiceImpl implements StockTakingOrderItemService {

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
    @Autowired
    DeliveryOrderService deliveryOrderService;
    @Autowired
    SupplyService supplyService;
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public int[] add(String accountBook, StockTakingOrderItem[] stockTakingOrderItems) throws WMSServiceException {

        for (int i = 0; i < stockTakingOrderItems.length; i++) {
            new Validator("单位").notnull().notEmpty().validate(stockTakingOrderItems[i].getUnit());
            new Validator("单位数量").notnull().validate(stockTakingOrderItems[i].getUnitAmount());
            new Validator("数量").notnull().validate(stockTakingOrderItems[i].getAmount());
            new Validator("实际数量").notnull().validate(stockTakingOrderItems[i].getRealAmount());
        }
        for (int i = 0; i < stockTakingOrderItems.length; i++) {
            idChecker.check(StockTakingOrderService.class, accountBook, stockTakingOrderItems[i].getStockTakingOrderId(), "盘点单");
            //idChecker.check(PersonService.class,accountBook,stockTakingOrderItems[i].getPersonId(),"盘点人");TODO
            idChecker.check(SupplyService.class, accountBook, stockTakingOrderItems[i].getSupplyId(), "供货信息");
            if(stockTakingOrderItems[i].getStorageLocationId()!=null){
            idChecker.check(StorageLocationService.class, accountBook, stockTakingOrderItems[i].getStorageLocationId(), "库位");}
            this.updateStockTakingOrder(accountBook,stockTakingOrderItems[i].getStockTakingOrderId(),stockTakingOrderItems[i].getPersonId());
        }
        return stockTakingOrderItemDAO.add(accountBook, stockTakingOrderItems);
    }

    @Override
    public void update(String accountBook, StockTakingOrderItem[] stockTakingOrderItems) throws WMSServiceException {
        for (int i = 0; i < stockTakingOrderItems.length; i++) {
            new Validator("单位").notnull().notEmpty().validate(stockTakingOrderItems[i].getUnit());
            new Validator("单位数量").notnull().validate(stockTakingOrderItems[i].getUnitAmount());
            new Validator("数量").notnull().validate(stockTakingOrderItems[i].getAmount());
            new Validator("实际数量").notnull().validate(stockTakingOrderItems[i].getRealAmount());
        }
        for (int i = 0; i < stockTakingOrderItems.length; i++) {
            idChecker.check(StockTakingOrderService.class, accountBook, stockTakingOrderItems[i].getStockTakingOrderId(), "盘点单");
            //idChecker.check(PersonService.class,accountBook,stockTakingOrderItems[i].getPersonId(),"盘点人");TODO
            idChecker.check(SupplyService.class, accountBook, stockTakingOrderItems[i].getSupplyId(), "供货信息");
            if(stockTakingOrderItems[i].getStorageLocationId()!=null){
            idChecker.check(StorageLocationService.class, accountBook, stockTakingOrderItems[i].getStorageLocationId(), "库位");}
            this.updateStockTakingOrder(accountBook,stockTakingOrderItems[i].getStockTakingOrderId(),stockTakingOrderItems[i].getPersonId());
        }
        stockTakingOrderItemDAO.update(accountBook, stockTakingOrderItems);
    }

    @Override
    public void remove(String accountBook, StockTakingItemDelete stockTakingItemDelete) throws WMSServiceException {
        int[] ids=stockTakingItemDelete.getDeleteIds();
        for (int id : ids) {
            if (stockTakingOrderItemDAO.find(accountBook, new Condition().addCondition("id", id)).length == 0) {
                throw new WMSServiceException(String.format("删除盘点单条目不存在，请重新查询！(%d)", id));
            }
        }
        StockTakingOrderItemView[] stockTakingOrderItemViews= stockTakingOrderItemDAO.find(accountBook,new Condition().addCondition("id",new Integer[]{ids[0]}));
        try {
            stockTakingOrderItemDAO.remove(accountBook, ids);
            //删除的时候每次只能删除同一个盘点单的
           this.updateStockTakingOrder(accountBook,stockTakingOrderItemViews[0].getStockTakingOrderId(),stockTakingItemDelete.getPersonId());
        } catch (Throwable ex) {
            throw new WMSServiceException("删除盘点单条目失败，如果盘点单条目已经被引用，需要先删除引用的内容，才能删除该盘点单条目");
        }
    }

    @Override
    public void remove(String accountBook, int[] ids )throws WMSServiceException {
        for (int id : ids) {
            if (stockTakingOrderItemDAO.find(accountBook, new Condition().addCondition("id", id)).length == 0) {
                throw new WMSServiceException(String.format("删除盘点单条目不存在，请重新查询！(%d)", id));
            }
        }
        try {
            stockTakingOrderItemDAO.remove(accountBook, ids);
        } catch (Throwable ex) {
            throw new WMSServiceException("删除盘点单条目失败，如果盘点单条目已经被引用，需要先删除引用的内容，才能删除该盘点单条目");
        }
    }


    @Override
    public StockTakingOrderItemView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.stockTakingOrderItemDAO.find(accountBook, cond);
    }

    private int addStockTakingOrder(String accountBook,int personId,int warehouseId,String comment){
        StockTakingOrder stockTakingOrder=new StockTakingOrder();
        stockTakingOrder.setCreateTime(new Timestamp(System.currentTimeMillis()));
        stockTakingOrder.setCreatePersonId(personId);
        stockTakingOrder.setWarehouseId(warehouseId);
        stockTakingOrder.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
        stockTakingOrder.setLastUpdatePersonId(personId);
        SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd");
        stockTakingOrder.setDescription(tempDate.format(new Date(System.currentTimeMillis()))+"-"+comment);
        int[] ids=stockTakingOrderService.add(accountBook,new StockTakingOrder[]{stockTakingOrder});
        if(ids.length!=1){
            throw new WMSServiceException("添加盘点单出错！");
        }
        return ids[0];
    }

    //提供仓库即可
    @Override
    public void addStockTakingOrderItemAll(String accountBook, StockTakingOrderItemAdd stockTakingOrderItemAdd) {
        new Validator("人员").notnull().validate(stockTakingOrderItemAdd.getPersonId());
        //idChecker.check(StockTakingOrderService.class, accountBook, stockTakingOrderItemAdd.getStockTakingOrderId(), "盘点单");
        idChecker.check(com.wms.services.warehouse.service.WarehouseService.class, accountBook, stockTakingOrderItemAdd.getWarehouseId(), " 仓库");
        StockRecordFind stockRecordFind=new StockRecordFind();
        stockRecordFind.setWarehouseId(stockTakingOrderItemAdd.getWarehouseId());
        stockRecordFind.setTimeEnd(stockTakingOrderItemAdd.getCheckTime());
        stockTakingOrderItemAdd.setAddMode("all");
        stockTakingOrderItemAdd.setStockTakingOrderId(this.addStockTakingOrder(accountBook,stockTakingOrderItemAdd.getPersonId(),stockTakingOrderItemAdd.getWarehouseId(),"详细数目"));
        this.addItemToDatabase(accountBook,stockRecordService.findCheckWarehouse(accountBook,stockRecordFind,stockTakingOrderItemAdd.getStockTakingOrderId()),stockTakingOrderItemAdd,"详细数目");
        stockTakingOrderItemAdd.setStockTakingOrderId(this.addStockTakingOrder(accountBook,stockTakingOrderItemAdd.getPersonId(),stockTakingOrderItemAdd.getWarehouseId(),"仓库总数"));
        this.addItemToDatabase(accountBook,stockRecordService.findCheckWarehouseAmountAll(accountBook,stockRecordFind,stockTakingOrderItemAdd.getStockTakingOrderId()),stockTakingOrderItemAdd,"仓库总数");
        stockTakingOrderItemAdd.setStockTakingOrderId(this.addStockTakingOrder(accountBook,stockTakingOrderItemAdd.getPersonId(),stockTakingOrderItemAdd.getWarehouseId(),"在途数量"));
        this.addItemToDatabase(accountBook,stockRecordService.findLoadingWarehouse(accountBook,stockRecordFind,stockTakingOrderItemAdd.getStockTakingOrderId()),stockTakingOrderItemAdd,"在途数量");
        stockTakingOrderItemAdd.setStockTakingOrderId(this.addStockTakingOrder(accountBook,stockTakingOrderItemAdd.getPersonId(),stockTakingOrderItemAdd.getWarehouseId(),"合格品数量"));
        this.addItemToDatabase(accountBook,stockRecordService.findQualifiedWarehouse(accountBook,stockRecordFind,stockTakingOrderItemAdd.getStockTakingOrderId()),stockTakingOrderItemAdd,"合格品数量");
        stockTakingOrderItemAdd.setStockTakingOrderId(this.addStockTakingOrder(accountBook,stockTakingOrderItemAdd.getPersonId(),stockTakingOrderItemAdd.getWarehouseId(),"不合格品数量"));
        this.addItemToDatabase(accountBook,stockRecordService.findUnqualifiedWarehouse(accountBook,stockRecordFind,stockTakingOrderItemAdd.getStockTakingOrderId()),stockTakingOrderItemAdd,"不合格品数量");

        //this.updateStockTakingOrder(accountBook,stockTakingOrderItemAdd.getStockTakingOrderId(),stockTakingOrderItemAdd.getPersonId());
    }

    //提供供货添加查出每条供货的库存记录
    @Override
    public void addStockTakingOrderItemSingle(String accountBook, int[] supplyIds,StockTakingOrderItemAdd stockTakingOrderItemAdd) {
        new Validator("人员").notnull().validate(stockTakingOrderItemAdd.getPersonId());
        idChecker.check(StockTakingOrderService.class, accountBook, stockTakingOrderItemAdd.getStockTakingOrderId(), "盘点单");
        idChecker.check(com.wms.services.warehouse.service.WarehouseService.class, accountBook, stockTakingOrderItemAdd.getWarehouseId(), " 仓库");
        for(int supplyId:supplyIds){idChecker.check(SupplyService.class, accountBook, supplyId, "供货信息");
            if(supplyService.find(accountBook,new Condition().addCondition("id",supplyId).addCondition("warehouseId",new Integer[]{stockTakingOrderItemAdd.getWarehouseId()})).length==0)
            {throw new WMSServiceException("输入的供货id"+supplyId+"不属于输入的仓库！");}
        }
        int mode = stockTakingOrderItemAdd.getMode();
        StockRecordFind stockRecordFind=new StockRecordFind();
        stockRecordFind.setWarehouseId(stockTakingOrderItemAdd.getWarehouseId());
        stockRecordFind.setTimeEnd(stockTakingOrderItemAdd.getCheckTime());
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("(");
        for(int i=0;i<supplyIds.length;i++){ stringBuffer.append(supplyIds[i]);
            if(i!=supplyIds.length-1)
            {
                stringBuffer.append(",");
            }
        }
        stringBuffer.append(")");
        this.addItemToDatabase(accountBook,stockRecordService.findCheckSupply(accountBook,stockRecordFind,stringBuffer.toString(),stockTakingOrderItemAdd.getStockTakingOrderId()),stockTakingOrderItemAdd,"详细数目");
        this.addItemToDatabase(accountBook,stockRecordService.findCheckSupplyAmountAll(accountBook,stockRecordFind,stringBuffer.toString(),stockTakingOrderItemAdd.getStockTakingOrderId()),stockTakingOrderItemAdd,"仓库总数");
        this.addItemToDatabase(accountBook,stockRecordService.findLoadingSupply(accountBook,stockRecordFind,stringBuffer.toString(),stockTakingOrderItemAdd.getStockTakingOrderId()),stockTakingOrderItemAdd,"在途数量");
        this.addItemToDatabase(accountBook,stockRecordService.findQualifiedSupply(accountBook,stockRecordFind,stringBuffer.toString(),stockTakingOrderItemAdd.getStockTakingOrderId()),stockTakingOrderItemAdd,"合格品数量");
        this.addItemToDatabase(accountBook,stockRecordService.findUnqualifiedSupply(accountBook,stockRecordFind,stringBuffer.toString(),stockTakingOrderItemAdd.getStockTakingOrderId()),stockTakingOrderItemAdd,"不合格品数量");
        this.updateStockTakingOrder(accountBook,stockTakingOrderItemAdd.getStockTakingOrderId(),stockTakingOrderItemAdd.getPersonId());
    }

        //接收盘点单条目数据并添加
        private void addItemToDatabase(String accountBook,Object[] stockRecordSource1,StockTakingOrderItemAdd stockTakingOrderItemAdd,String comment)
        {
            List<StockTakingOrderItem> stockTakingOrderItemList=new ArrayList<>();
        for(int i=0;i<stockRecordSource1.length;i++){
            Object[] objects = (Object[]) stockRecordSource1[i];
            StockTakingOrderItem stockTakingOrderItem = new StockTakingOrderItem();
            stockTakingOrderItem.setStockTakingOrderId(stockTakingOrderItemAdd.getStockTakingOrderId());
            stockTakingOrderItem.setPersonId(stockTakingOrderItemAdd.getPersonId());
            stockTakingOrderItem.setComment(comment);
                if (comment.equals("详细数目")) {
                    stockTakingOrderItem.setUnit((String) objects[5]);
                    stockTakingOrderItem.setStorageLocationId((Integer) objects[2]);
                    stockTakingOrderItem.setUnitAmount((BigDecimal) objects[6]);
                } else {
                    stockTakingOrderItem.setUnit("个");
                    stockTakingOrderItem.setStorageLocationId(null);
                    stockTakingOrderItem.setUnitAmount(new BigDecimal(1));
                }
            if(comment.equals("在途数量"))
            {   stockTakingOrderItem.setSupplyId((int) objects[2]);
                stockTakingOrderItem.setAmount((BigDecimal) objects[objects.length-1]);
                stockTakingOrderItem.setRealAmount((BigDecimal) objects[objects.length-1]);}
            else if(comment.equals("详细数目")||comment.equals("仓库总数")){
                stockTakingOrderItem.setSupplyId((int) objects[3]);
                stockTakingOrderItem.setAmount((BigDecimal) objects[26]);
                stockTakingOrderItem.setRealAmount((BigDecimal) objects[26]);}
            else if(comment.equals("合格品数量")) {
                stockTakingOrderItem.setSupplyId((int) objects[3]);
//                if(objects[27]==null){
                stockTakingOrderItem.setAmount((BigDecimal) objects[26]);
               stockTakingOrderItem.setRealAmount((BigDecimal) objects[26]);
//            }
//                else{stockTakingOrderItem.setAmount((BigDecimal) objects[27]);
//                    stockTakingOrderItem.setRealAmount((BigDecimal) objects[27]);}
            }
            else if(comment.equals("不合格品数量")){
                stockTakingOrderItem.setAmount((BigDecimal) objects[1]);
                stockTakingOrderItem.setRealAmount((BigDecimal)objects[1]);
                stockTakingOrderItem.setSupplyId((int)objects[0]);
            }
            stockTakingOrderItemList.add(stockTakingOrderItem);
        }
            StockTakingOrderItem[] stockTakingOrderItems=null;
            stockTakingOrderItems = (StockTakingOrderItem[])  Array.newInstance(StockTakingOrderItem.class,stockTakingOrderItemList.size());
            stockTakingOrderItemList.toArray(stockTakingOrderItems);
            stockTakingOrderItemDAO.add(accountBook, stockTakingOrderItems);
    }

    public void setRealAmount(String accountBook,StockTakingOrderItem stockTakingOrderItem)
    {
        idChecker.check(PersonService.class,accountBook,stockTakingOrderItem.getPersonId()," 人员");
        new Validator("盘点单条目id").notnull().validate(stockTakingOrderItem.getId());
        idChecker.check(StockTakingOrderItemService.class,accountBook,stockTakingOrderItem.getId(),"盘点单条目");
        new Validator("真实数量").notnull().min(0).validate(stockTakingOrderItem.getRealAmount());
        StockTakingOrderItemView[] stockTakingOrderItemViews=stockTakingOrderItemDAO.find(accountBook,new Condition().addCondition("id",stockTakingOrderItem.getId()));
        if(stockTakingOrderItemViews.length!=1){throw new WMSServiceException("没有找到要修改的盘点单条目"); }
        StockTakingOrderItem stockTakingOrderItemSave=new StockTakingOrderItem();
        stockTakingOrderItemSave.setPersonId(stockTakingOrderItemViews[0].getPersonId());
        stockTakingOrderItemSave.setUnitAmount(stockTakingOrderItemViews[0].getUnitAmount());
        stockTakingOrderItemSave.setUnit(stockTakingOrderItemViews[0].getUnit());
        stockTakingOrderItemSave.setComment(stockTakingOrderItemViews[0].getComment());
        stockTakingOrderItemSave.setStorageLocationId(stockTakingOrderItemViews[0].getStorageLocationId());
        stockTakingOrderItemSave.setSupplyId(stockTakingOrderItemViews[0].getSupplyId());
        stockTakingOrderItemSave.setAmount(stockTakingOrderItemViews[0].getAmount());
        stockTakingOrderItemSave.setStockTakingOrderId(stockTakingOrderItemViews[0].getStockTakingOrderId());
        stockTakingOrderItemSave.setId(stockTakingOrderItemViews[0].getId());
        stockTakingOrderItemSave.setRealAmount(stockTakingOrderItem.getRealAmount());
        stockTakingOrderItemDAO.update(accountBook,new StockTakingOrderItem[]{stockTakingOrderItemSave});
        this.updateStockTakingOrder(accountBook,stockTakingOrderItemSave.getStockTakingOrderId(),stockTakingOrderItem.getPersonId());
    }

    private void updateStockTakingOrder( String accountBook,int stockTakingOrderId ,int lastUpdatePersonId){
        idChecker.check(StockTakingOrderService.class,accountBook,stockTakingOrderId,"盘点单");
       //TODO idChecker.check(PersonService.class,accountBook,lastUpdatePersonId," 人员");
       StockTakingOrderView[] stockTakingOrderViews= stockTakingOrderService.find(accountBook,new Condition().addCondition("id",new Integer[]{stockTakingOrderId}));
        if(stockTakingOrderViews.length==0){
            throw new WMSServiceException("没有找到要更新的盘点单！");
        }
        StockTakingOrder stockTakingOrder=new StockTakingOrder();
        stockTakingOrder.setId(stockTakingOrderViews[0].getId());
        stockTakingOrder.setCreatePersonId(stockTakingOrderViews[0].getCreatePersonId());
        stockTakingOrder.setCreateTime(stockTakingOrderViews[0].getCreateTime());
        stockTakingOrder.setDescription(stockTakingOrderViews[0].getDescription());
        stockTakingOrder.setNo(stockTakingOrderViews[0].getNo());
        stockTakingOrder.setWarehouseId(stockTakingOrderViews[0].getWarehouseId());
        stockTakingOrder.setLastUpdatePersonId(lastUpdatePersonId);
        stockTakingOrder.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
        stockTakingOrderService.update(accountBook,new StockTakingOrder[]{stockTakingOrder});
    }

    @Override
    public long findCount(String accountBook, Condition cond) throws WMSServiceException{
        return this.stockTakingOrderItemDAO.findCount(accountBook,cond);
    }
}