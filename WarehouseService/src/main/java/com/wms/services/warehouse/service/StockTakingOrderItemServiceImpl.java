package com.wms.services.warehouse.service;
import com.wms.services.warehouse.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import com.wms.services.warehouse.datastructures.StockTakingOrderItemAdd;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public int[] add(String accountBook, StockTakingOrderItem[] stockTakingOrderItems) throws WMSServiceException {

        for (int i = 0; i < stockTakingOrderItems.length; i++) {
            new Validator("单位").notnull().notEmpty().validate(stockTakingOrderItems[i].getUnit());
            new Validator("单位数量").notnull().validate(stockTakingOrderItems[i].getUnitAmount());
        }
        for (int i = 0; i < stockTakingOrderItems.length; i++) {
            idChecker.check(StockTakingOrderService.class, accountBook, stockTakingOrderItems[i].getStockTakingOrderId(), "盘点单");
            // idChecker.check(PersonService.class,accountBook,stockTakingOrderItems[i].getPersonId(),"盘点人");TODO
            idChecker.check(SupplyService.class, accountBook, stockTakingOrderItems[i].getSupplyId(), "供货信息");
            idChecker.check(StorageLocationService.class, accountBook, stockTakingOrderItems[i].getStorageLocationId(), "库位");
        }
        return stockTakingOrderItemDAO.add(accountBook, stockTakingOrderItems);
    }

    @Override
    public void update(String accountBook, StockTakingOrderItem[] stockTakingOrderItems) throws WMSServiceException {
        for (int i = 0; i < stockTakingOrderItems.length; i++) {
            new Validator("单位").notnull().notEmpty().validate(stockTakingOrderItems[i].getUnit());
            new Validator("单位数量").notnull().validate(stockTakingOrderItems[i].getUnitAmount());
        }
        for (int i = 0; i < stockTakingOrderItems.length; i++) {
            idChecker.check(StockTakingOrderService.class, accountBook, stockTakingOrderItems[i].getStockTakingOrderId(), "盘点单");
            //idChecker.check(PersonService.class,accountBook,stockTakingOrderItems[i].getPersonId(),"盘点人");
            idChecker.check(SupplyService.class, accountBook, stockTakingOrderItems[i].getSupplyId(), "供货信息");
            idChecker.check(StorageLocationService.class, accountBook, stockTakingOrderItems[i].getStorageLocationId(), "库位");
        }
        stockTakingOrderItemDAO.update(accountBook, stockTakingOrderItems);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
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

    @Override
    public void addStockTakingOrderItemAll(String accountBook, StockTakingOrderItemAdd stockTakingOrderItemAdd) {

        SupplyView[] supplyView=supplyService.find(accountBook,new Condition());
        if(supplyView.length==0){throw new WMSServiceException("无任何供货记录！");}
       // Integer[] supplyIdAll=new Integer[supplyView.length];
        for(int i=0;i<supplyView.length;i++){
            StockTakingOrderItemAdd stockTakingOrderItemAddAll=new StockTakingOrderItemAdd();
            stockTakingOrderItemAddAll.setStockTakingOrderId(stockTakingOrderItemAdd.getStockTakingOrderId());
            stockTakingOrderItemAddAll.setMode(stockTakingOrderItemAdd.getMode());
            stockTakingOrderItemAddAll.setWarehouseId(stockTakingOrderItemAdd.getWarehouseId());
            stockTakingOrderItemAddAll.setCheckTime(stockTakingOrderItemAdd.getCheckTime());
            stockTakingOrderItemAddAll.setPersonId(stockTakingOrderItemAdd.getPersonId());
            stockTakingOrderItemAddAll.setSupplyId(supplyView[i].getId());
            this.addStockTakingOrderItemSingle(accountBook,stockTakingOrderItemAddAll);
        }
    }

    @Override
    public void addStockTakingOrderItemSingle(String accountBook, StockTakingOrderItemAdd stockTakingOrderItemAdd) {
        new Validator("人员").notnull().validate(stockTakingOrderItemAdd.getPersonId());
        idChecker.check(StockTakingOrder.class, accountBook, stockTakingOrderItemAdd.getStockTakingOrderId(), "盘点单");
        idChecker.check(Supply.class, accountBook, stockTakingOrderItemAdd.getSupplyId(), "供货信息");
        idChecker.check(Warehouse.class, accountBook, stockTakingOrderItemAdd.getWarehouseId(), " 仓库");
        int mode = stockTakingOrderItemAdd.getMode();
        Condition condition = new Condition();
        condition.addCondition("supplyId", new Integer[]{stockTakingOrderItemAdd.getSupplyId()}).
                addCondition("warehouseId", new Integer[]{stockTakingOrderItemAdd.getWarehouseId()}).
                addCondition("time", new Timestamp[]{stockTakingOrderItemAdd.getCheckTime()}, ConditionItem.Relation.LESS_THAN).
                addOrder("time", OrderItem.Order.DESC);
        if (stockRecordService.find(accountBook, condition).length == 0) {
            throw new WMSServiceException("没有查到此供货的任何库存信息");
        }

        //第一条肯定是某个记录的最新一条
        BigDecimal warehouseAmount = new BigDecimal(0);
        while (stockRecordService.find(accountBook, condition).length > 0) {
            StockRecordView[] stockRecordTemp = stockRecordService.find(accountBook, condition);
            //如果为所有信息模式，将此信息作为一条盘点单条目保存
            if (mode == 0) {
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
                stockTakingOrderItemDAO.add(accountBook, new StockTakingOrderItem[]{stockTakingOrderItem});
            }
            //相同单位、单位数量、批号的第一条已经取出，所有增加条件，下次查询没有与刚才相同的记录了
            condition = condition.addCondition("unitAmount", new BigDecimal[]{stockRecordTemp[0].getUnitAmount()}, ConditionItem.Relation.NOT_EQUAL)
                    .addCondition("unit", new java.lang.String[]{stockRecordTemp[0].getUnit()}, ConditionItem.Relation.NOT_EQUAL).addCondition(
                            "batchNo", new java.lang.String[]{stockRecordTemp[0].getBatchNo()}, ConditionItem.Relation.NOT_EQUAL);

            //得到同一供货、同一仓库内的数量总和
            warehouseAmount = warehouseAmount.add(stockRecordTemp[0].getAmount());
        }
        //添加总数量条目
        StockTakingOrderItem stockTakingOrderItem = new StockTakingOrderItem();
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

        DeliveryOrderItemView[] deliveryOrderItemViews = null;
        deliveryOrderItemViews = deliveryOrderItemService.find(accountBook, new Condition().addCondition("supplyId", new Integer[]{stockTakingOrderItemAdd.getSupplyId()}).
                addCondition("loadingTime", new Timestamp[]{stockTakingOrderItemAdd.getCheckTime()}, ConditionItem.Relation.LESS_THAN).addCondition("state", 0, ConditionItem.Relation.NOT_EQUAL));
        List<DeliveryOrderItemView> deliveryOrderItemViewList=new ArrayList<DeliveryOrderItemView>();
        for(int i=0;i<deliveryOrderItemViews.length;i++)
        {
            int delivery=deliveryOrderItemViews[i].getDeliveryOrderId();
            DeliveryOrderView[] deliveryOrderViews=deliveryOrderService.find(accountBook,new Condition().addCondition("id",new Integer[]{delivery}));
            if(deliveryOrderViews.length!=1){
                throw new WMSServiceException("没有查到相关出库单！");
            }
            if(deliveryOrderViews[0].getState()!=4)
            {
                deliveryOrderItemViewList.add(deliveryOrderItemViews[i]);
            }
        }
        DeliveryOrderItemView[] deliveryOrderItemCheckTemp=new DeliveryOrderItemView[deliveryOrderItemViewList.size()];
        DeliveryOrderItemView[] deliveryOrderItemCheck=deliveryOrderItemViewList.toArray(deliveryOrderItemCheckTemp);
        BigDecimal wayAmount=BigDecimal.ZERO;
        for(int i=0;i<deliveryOrderItemCheck.length;i++){
            wayAmount.add(deliveryOrderItemCheck[i].getRealAmount());
        }
        StockTakingOrderItem stockTakingOrderItemWay = new StockTakingOrderItem();
        stockTakingOrderItemWay.setStockTakingOrderId(stockTakingOrderItemAdd.getStockTakingOrderId());
        stockTakingOrderItemWay.setPersonId(stockTakingOrderItemAdd.getPersonId());
        stockTakingOrderItemWay.setSupplyId(stockTakingOrderItemAdd.getSupplyId());
        stockTakingOrderItemWay.setComment("在途数量");
        stockTakingOrderItemWay.setUnit("个");
        stockTakingOrderItemWay.setStorageLocationId(null);
        stockTakingOrderItemWay.setUnitAmount(new BigDecimal(1));
        stockTakingOrderItemWay.setAmount(wayAmount);
        stockTakingOrderItemWay.setRealAmount(wayAmount);
        stockTakingOrderItemDAO.add(accountBook, new StockTakingOrderItem[]{stockTakingOrderItemWay});
        this.updateStockTakingOrder(accountBook,stockTakingOrderItemAdd.getStockTakingOrderId(),stockTakingOrderItemAdd.getPersonId());
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
        idChecker.check(PersonService.class,accountBook,lastUpdatePersonId," 人员");
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
}