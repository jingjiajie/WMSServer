package com.wms.services.warehouse.service;

import com.wms.services.ledger.service.PersonService;
import com.wms.services.warehouse.dao.StockTakingOrderDAO;
import com.wms.services.warehouse.datastructures.StockTakingOrderAndItems;
import com.wms.services.warehouse.datastructures.WarehouseEntryAndItems;
import com.wms.utilities.OrderNoGenerator;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional
public class StockTakingOrderServiceImpl implements StockTakingOrderService{
    @Autowired
    StockTakingOrderDAO stockTakingOrderDAO;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    PersonService personService;
    @Autowired
    OrderNoGenerator orderNoGenerator;
    @Autowired
    StockTakingOrderItemService stockTakingOrderItemService;
    private static final String NO_PREFIX = "P";
    @Override
    public int[] add(String accountBook,StockTakingOrder[] stockTakingOrders) throws WMSServiceException {
        for(int i=0;i<stockTakingOrders.length;i++){
            Condition cond = new Condition();
            cond.addCondition("no",new String[]{stockTakingOrders[i].getNo()});
            if(stockTakingOrderDAO.find(accountBook,cond).length > 0) {
                throw new WMSServiceException("盘点单代号重复：" + stockTakingOrders[i].getNo());
            }
        }

        //外键
        Stream.of(stockTakingOrders).forEach(
                (stockTakingOrder)->{
                    if(this.warehouseService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{stockTakingOrder.getWarehouseId()})).length == 0){
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",stockTakingOrder.getWarehouseId()));}
                      else if(this.personService.find(accountBook,
                              new Condition().addCondition("id",new Integer[]{stockTakingOrder.getCreatePersonId()})).length == 0){
                            throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)",stockTakingOrder.getCreatePersonId()));
                    }
                    if(stockTakingOrder.getLastUpdatePersonId() != null && this.personService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{stockTakingOrder.getLastUpdatePersonId()})).length == 0){
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)",stockTakingOrder.getLastUpdatePersonId()));
                    }
                }
        );
        //生成创建时间
        Stream.of(stockTakingOrders).forEach((stockTakingOrder) -> stockTakingOrder.setCreateTime(new java.sql.Timestamp(System.currentTimeMillis())));
        //生成/检测单号
        Stream.of(stockTakingOrders).forEach((stockTakingOrder) -> {
            //如果单号留空则自动生成
            if (stockTakingOrder.getNo() == null) {
                stockTakingOrder.setNo(this.orderNoGenerator.generateNextNo(accountBook, StockTakingOrderServiceImpl.NO_PREFIX));
            } else { //否则检查单号是否重复
                Condition cond = new Condition();
                cond.addCondition("no", new String[]{stockTakingOrder.getNo()});
                cond.addCondition("warehouseId",stockTakingOrder.getWarehouseId());
                if (stockTakingOrderDAO.find(accountBook, cond).length > 0) {
                    throw new WMSServiceException("盘点单单号重复：" + stockTakingOrder.getNo());
                }
            }
        });

        return stockTakingOrderDAO.add(accountBook,stockTakingOrders);
    }


    @Override
    public void update(String accountBook, StockTakingOrder[] stockTakingOrders) throws WMSServiceException {
        for(int i=0;i<stockTakingOrders.length;i++)
        {
            new Validator("盘点单号").notnull().notEmpty().validate(stockTakingOrders[i].getNo());
        }
        for(int i=0;i<stockTakingOrders.length;i++){
            Condition cond = new Condition();
            cond.addCondition("no",new String[]{stockTakingOrders[i].getNo()}).addCondition("id",new Integer[]{stockTakingOrders[i].getId()}, ConditionItem.Relation.NOT_EQUAL);
            cond.addCondition("warehouseId",stockTakingOrders[i].getWarehouseId());
            cond.addCondition("id",stockTakingOrders[i].getId(), ConditionItem.Relation.NOT_EQUAL);
            if(stockTakingOrderDAO.find(accountBook,cond).length > 0) {
                throw new WMSServiceException("盘点单代号重复：" + stockTakingOrders[i].getNo());
            }
          else if( stockTakingOrderDAO.find(accountBook,new Condition().addCondition("id",new Integer[]{stockTakingOrders[i].getId()})).length!=1)
           {
               throw new WMSServiceException("没找到要修改的盘点单");
           }
        }
        //外键
        Stream.of(stockTakingOrders).forEach(
                (stockTakingOrder)->{
                    if(this.warehouseService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{stockTakingOrder.getWarehouseId()})).length == 0){
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",stockTakingOrder.getWarehouseId()));}
                    else if(this.personService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{stockTakingOrder.getCreatePersonId()})).length == 0){
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)",stockTakingOrder.getCreatePersonId()));
                    }
                    if(stockTakingOrder.getLastUpdatePersonId() != null && this.personService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{stockTakingOrder.getLastUpdatePersonId()})).length == 0){
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)",stockTakingOrder.getLastUpdatePersonId()));
                    }
                }
        );
        for(int i=0;i<stockTakingOrders.length;i++){
            stockTakingOrders[i].setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
        }
        stockTakingOrderDAO.update(accountBook,stockTakingOrders);
    }


    @Override
    public void remove(String accountBook,int ids[]) throws WMSServiceException{
        for (int id : ids) {
            if (stockTakingOrderDAO.find(accountBook, new Condition().addCondition("id", id)).length == 0) {
                throw new WMSServiceException(String.format("删除盘点单不存在，请重新查询！(%d)", id));
            }
        }

        try {
            stockTakingOrderDAO.remove(accountBook, ids);
        }
        catch (Throwable ex){
            throw new WMSServiceException("删除盘点单失败，如果盘点单已经被引用，需要先删除引用的内容，才能删除该盘点单");
        }

    }

    @Override
    public StockTakingOrderView[] find(String accountBook, Condition cond) throws WMSServiceException{
        return this.stockTakingOrderDAO.find(accountBook, cond);
    }


    @Override
    public long findCount(String accountBook, Condition cond) throws WMSServiceException{
        return this.stockTakingOrderDAO.findCount(accountBook,cond);
    }

    @Override
    public List<StockTakingOrderAndItems> getPreviewData(String accountBook, List<Integer> StockTakingOrderIDs) throws WMSServiceException{
        StockTakingOrderView[] stockTakingOrderViews = this.stockTakingOrderDAO.find(accountBook,new Condition().addCondition("id", StockTakingOrderIDs.toArray(), ConditionItem.Relation.IN));
        StockTakingOrderItemView[] itemViews = this.stockTakingOrderItemService.find(accountBook,new Condition().addCondition("stockTakingOrderId", StockTakingOrderIDs.toArray(), ConditionItem.Relation.IN));
        List<StockTakingOrderAndItems> result = new ArrayList<>();
        for(StockTakingOrderView stockTakingOrderView : stockTakingOrderViews){
            StockTakingOrderAndItems stockTakingOrderAndItems= new StockTakingOrderAndItems();
            stockTakingOrderAndItems.setStockTakingOrderView(stockTakingOrderView);
            stockTakingOrderAndItems.setStockTakingOrderItems(new ArrayList<>());
            result.add(stockTakingOrderAndItems);
            for(StockTakingOrderItemView itemView : itemViews){
                if(itemView.getStockTakingOrderId() == stockTakingOrderView.getId()){
                    stockTakingOrderAndItems.getStockTakingOrderItems().add(itemView);
                }
            }
        }
        return result;
    }
}
